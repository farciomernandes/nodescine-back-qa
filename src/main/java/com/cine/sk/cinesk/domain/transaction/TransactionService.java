package com.cine.sk.cinesk.domain.transaction;

import com.cine.sk.cinesk.domain.auth.enums.Role;
import com.cine.sk.cinesk.domain.email.EmailService;
import com.cine.sk.cinesk.domain.movie.Movie;
import com.cine.sk.cinesk.domain.movie.MovieRepository;
import com.cine.sk.cinesk.domain.movie.enhanced.EnhancedFilmDTO;
import com.cine.sk.cinesk.domain.movie.genre.GenreDTO;
import com.cine.sk.cinesk.domain.transaction.payment.*;
import com.cine.sk.cinesk.domain.user.User;
import com.cine.sk.cinesk.domain.user.dto.TransactionDTO;
import com.cine.sk.cinesk.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final MovieRepository movieRepository;
    private final UserService userService;
    private final PaymentService paymentService;
    private final EmailService emailService;

    private static final BigDecimal TAX_RATE = new BigDecimal("0.2");

    private User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autorizado ou não encontrado");
        }
        String email = auth.getName();
        return userService.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autorizado ou não encontrado"));
    }

    public Transaction getById(Long id) {
        User user = currentUser();

        Transaction tx = transactionRepository.findById(id)
                .filter(t -> t.getUser() != null && t.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transação não encontrada"));
        return tx;
    }

    public Transaction findByTransactionId(String transactionId) {
        return transactionRepository.findByTransactionId(transactionId).orElse(null);
    }



    public List<TransactionDTO> getMy() {
        User user = currentUser();
        return findTransactionByUser(user).stream().map(this::transactionToDTOsoVai).toList();
    }

    public TransactionResponse createMock(CreateTransactionDTO transaction) {
        User user = currentUser();

        Movie movie = movieRepository.findById(transaction.getMovieId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Filme não encontrado"));

        Transaction transactionToSave = Transaction.builder()
                .user(user)
                .movie(movie)
                .amount(movie.getPrice())
                .transactionId(UUID.randomUUID().toString())
                .date(OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                .status(OrderStatusEnum.PENDING)
                .build();
        var tx = transactionRepository.save(transactionToSave);

        // Aqui seria colocado order se fosse a oficinal
        /*this.emailService.sendEmail(currentUser().getEmail(),
                "Nordescine - Transação iniciada" + movie.getTitle() + "  R$ " + movie.getPrice(),
                "✅ Email de confirmação de transação enviado com sucesso, aguardando confirmação da operadora."
        );*/
        return TransactionResponse.builder()
                .id(tx.getId())
                .transactionId(tx.getTransactionId())
                .amount(tx.getAmount())
                .status(tx.getStatus())
                .date(tx.getDate())
                .movieId(tx.getMovie().getId())
                .build();

    }

    public Transaction create(CreateTransactionDTO transaction) {
        User user = currentUser();

        Movie movie = movieRepository.findById(transaction.getMovieId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Filme não encontrado"));

        var order = OrderDTO.builder().total(movie.getPrice()).build();
        var userPayment = UserPaymentDTO.builder().cpf(user.getCpf())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();

        var address = AddressDTO.builder().street(user.getAddress())
                .neighborhood(user.getProvince())
                .complement(user.getComplement())
                .zipCode(user.getPostalCode())
                .number(user.getAddressNumber()).build();
        User userDirector = userService.findByEmail(movie.getCreatedBy())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autorizado ou não encontrado"));
        var response = paymentService.process(order, userPayment, transaction.getPayment(), address, userDirector.getWalletId());

        Transaction tx = Transaction.builder()
                .user(user)
                .movie(movie)
                .amount(movie.getPrice())
                .date(OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                .status(response.getStatus())
                .transactionId(response.getTransactionId())
                .build();

        this.emailService.sendEmail(currentUser().getEmail(),
                "Nordescine - Transação iniciada" + movie.getTitle() + "  R$ " + order.getTotal(),
                "✅ Email de confirmação de transação enviado com sucesso, aguardando confirmação da operadora."
        );
        return transactionRepository.save(tx);
    }

    public List<Transaction> findTransactionByUser(User user) {
        return transactionRepository.findByUser(user).stream().map(transaction -> transaction).toList();
    }

    public SalesTransactionSuDTO getTransactionsByUserAndStatus(User user, boolean isAdmin) {
        return calculateTotal(user, isAdmin);
    }

    private TransactionByMovieDTO calculateTotalsForMovie(Movie movie) {
        List<Transaction> transactions = transactionRepository.findByMovie(movie.getId());

        BigDecimal totalAmount = transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return TransactionByMovieDTO.builder()
            .movie(toDTO(movie))
            .totalAmount(totalAmount)
            .build();
    }

    private String minutesToDuration(Integer minutes) {
        if (minutes == null) return null;
        long hours = minutes / 60;
        long mins = minutes % 60;
        return String.format("%dh %dm", hours, mins);
    }

    private EnhancedFilmDTO toDTO(Movie entity) {
        EnhancedFilmDTO dto = new EnhancedFilmDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDirector(entity.getDirector());
        dto.setYear(entity.getYear());
        dto.setCategory(entity.getCategory() != null ? entity.getCategory().getName() : null);
        dto.setGenres(entity.getGenres().stream()
            .map(genre -> new GenreDTO(genre.getId(), genre.getName()))
            .collect(Collectors.toList()));
        dto.setDuration(minutesToDuration(entity.getDurationInMinutes()));
        dto.setMovieUrl(entity.getMovieUrl());
        dto.setTrailerUrl(entity.getTrailer());
        dto.setPrice(entity.getPrice());
        dto.setSynopsis(entity.getDescription());
        dto.setPoster(entity.getPoster());
        dto.setCast(entity.getCast());
        return dto;
    }

    public List<TransactionByMovieDTO> getTransactionsByCreatedByAndMovie() {
        User user = currentUser();
        List<Movie> movies;
        var isAdmin = user.getRoles() != null && user.getRoles().contains(Role.MODERATOR);
        if (isAdmin) {
            movies = movieRepository.findAll();
        } else {
            movies = movieRepository.findByCreatedBy(user.getEmail());
        }
        if (movies.isEmpty()) {
            return null;
        }

        return movies.stream().map(this::calculateTotalsForMovie).collect(Collectors.toList());
    }

    private SalesTransactionSuDTO calculateTotal(User user, Boolean isAdmin) {
        List<Transaction> transactions;
        Long totalUser;
        Long totalMovie = movieRepository.count();
        if (isAdmin) {
            transactions = transactionRepository.findAll();
            totalUser = userService.countUsers();
        } else {
            transactions = transactionRepository.findByUser(user);
            totalUser = null;
        }

        transactions.removeIf(t -> t.getStatus() == OrderStatusEnum.FAILED || t.getStatus() == OrderStatusEnum.CANCELED);

        BigDecimal totalAmount = transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal systemTax = totalAmount.multiply(TAX_RATE);
        BigDecimal total = totalAmount.add(systemTax);

        return SalesTransactionSuDTO.builder()
                .totalAmount(totalAmount)
                .totalUser(totalUser)
                .totalMovie(totalMovie)
                .build();
    }

    public SalesTransactionSuDTO findSalesResult() {
        User user = currentUser();
        var isAdmin = user.getRoles() != null && user.getRoles().contains(Role.MODERATOR);
        return getTransactionsByUserAndStatus(user, isAdmin);
    }


    private TransactionByMovieDTO transactionToDTO(BigDecimal totalAmount, Movie movie){
        return TransactionByMovieDTO.builder().totalAmount(totalAmount).movie(toDTO(movie)).build();
    }

    private TransactionDTO transactionToDTOsoVai(Transaction transaction){
        return TransactionDTO.builder().transactionId(transaction.getId()).movie(transaction.getMovie()).build();
    }

    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }
}
