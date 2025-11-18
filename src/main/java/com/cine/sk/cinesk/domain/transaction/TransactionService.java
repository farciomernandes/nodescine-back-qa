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

import static com.cine.sk.cinesk.domain.util.ConverterUtil.movieToEnhancedFilmDTO;

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

    public TransactionDTO getById(Long id) {
        User user = currentUser();

        Transaction tx = transactionRepository.findById(id)
                .filter(t -> t.getUser() != null && t.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transação não encontrada"));
        return toDTO(tx);
    }

    public Transaction findByTransactionId(String transactionId) {
        return transactionRepository.findByTransactionId(transactionId).orElse(null);
    }

    public List<TransactionDTO> getMy() {
        User user = currentUser();
        return findTransactionByUser(user).parallelStream().map(this::toDTO).toList();
    }

    public List<TransactionByMovieDTO> getDirectorId(Long id) {
        User user = userService.findById(id).orElseThrow( () -> new RuntimeException("User not found"));

        List<Movie> movies = movieRepository.findByCreatedBy(user.getEmail());
        return movies.stream().map(this::calculateTotalsForMovie).collect(Collectors.toList());
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
                .type(transaction.getPayment().getMethod())
                .status(OrderStatusEnum.PENDING)
                .build();
        var tx = transactionRepository.save(transactionToSave);

        this.emailService.sendEmail(currentUser().getEmail(),
                "Nordescine - Transação iniciada" + movie.getTitle() + "  R$ " + movie.getPrice(),
                "✅ Email de confirmação de transação enviado com sucesso, aguardando confirmação da operadora."
        );
        return TransactionResponse.builder()
                .id(tx.getId())
                .transactionId(tx.getTransactionId())
                .amount(tx.getAmount())
                .status(tx.getStatus())
                .date(tx.getDate())
                .movieId(tx.getMovie().getId())
                .build();

    }

    public TransactionResponse create(CreateTransactionDTO transaction) {
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
                .status(response.getPaymentResponse().getStatus())
                .transactionId(response.getPaymentResponse().getTransactionId())
                .type(transaction.getPayment().getMethod())
                .build();


        this.emailService.sendEmail(currentUser().getEmail(),
                "Nordescine - Transação iniciada" + movie.getTitle() + "  R$ " + order.getTotal(),
                "✅ Email de confirmação de transação enviado com sucesso, aguardando confirmação da operadora."
        );
        Transaction saved = transactionRepository.save(tx);
        return TransactionResponse.builder()
            .id(saved.getId())
            .transactionId(saved.getTransactionId())
            .amount(saved.getAmount())
            .status(saved.getStatus())
            .date(saved.getDate())
            .movieId(saved.getMovie().getId())
            .pix(response.getPix())
            .build();
    }

    public List<Transaction> findTransactionByUser(User user) {
        return transactionRepository.findByUserWithDetails(user);
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
            .movie(movieToEnhancedFilmDTO(movie))
            .totalAmount(totalAmount)
            .build();
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

    public SalesTransactionSuDTO findSalesResult(Long id) {
        User user = userService.findById(id).orElseThrow( () -> new RuntimeException("User not found"));
        return getTransactionsByUserAndStatus(user, false);
    }

    private TransactionDTO toDTO(Transaction transaction) {
        EnhancedFilmDTO movieDTO = movieToEnhancedFilmDTO(transaction.getMovie());
        boolean expired = false;
        String producerDeadline = movieDTO.getProducerDeadline();

        if (producerDeadline != null && !producerDeadline.isBlank()) {
            try {
                long hours = Long.parseLong(producerDeadline.replace("h", "").trim());
                OffsetDateTime transactionDate = OffsetDateTime.parse(transaction.getDate());
                OffsetDateTime expirationDate = transactionDate.plusHours(hours);

                if (OffsetDateTime.now().isAfter(expirationDate)) {
                    expired = true;
                    movieDTO.setMovieUrl(null);
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid producerDeadline format: " + producerDeadline);
            }
        }

        return TransactionDTO.builder()
                .transactionId(transaction.getId())
                .createdAt(transaction.getCreatedAt())
                .status(transaction.getStatus())
                .movie(movieDTO)
                .expired(expired)
                .build();
    }

    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public List<TransactionDTO> getByUserId(Long id) {
        return transactionRepository.findAllByUser_Id(id).stream().map(this::toDTO).toList();
    }
}
