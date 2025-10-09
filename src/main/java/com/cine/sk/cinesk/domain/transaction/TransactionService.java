package com.cine.sk.cinesk.domain.transaction;

import com.cine.sk.cinesk.domain.auth.enums.Role;
import com.cine.sk.cinesk.domain.email.EmailService;
import com.cine.sk.cinesk.domain.movie.Movie;
import com.cine.sk.cinesk.domain.movie.MovieRepository;
import com.cine.sk.cinesk.domain.transaction.payment.*;
import com.cine.sk.cinesk.domain.user.User;
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
import java.util.Arrays;
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

    public List<Transaction> getMy() {
        User user = currentUser();
        return findTransactionByUser(user);
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
        var response = paymentService.process(order,userPayment, transaction.getPayment(), address, userDirector.getWalletId());

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
        return transactionRepository.findByUser(user);
    }

    public List<SalesTransactionDTO> getTransactionsByUserAndStatus(User user, boolean isAdmin) {
        List<SalesTransactionDTO> transactions = Arrays.stream(OrderStatusEnum.values())
                .map(status -> calculateTotalsForStatus(user, status))
                .collect(Collectors.toList());

        if (isAdmin) {
            transactions.forEach(dto -> {
                dto.setAmount(dto.getSystemTax());
                dto.setTotal(dto.getSystemTax());
            });
        }

        return transactions;
    }

    private SalesTransactionDTO calculateTotalsForStatus(User user, OrderStatusEnum status) {
        List<Transaction> transactions = transactionRepository.findByUserAndStatus(user, status);

        BigDecimal totalAmount = transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal systemTax = totalAmount.multiply(TAX_RATE);
        BigDecimal total = totalAmount.add(systemTax);

        return SalesTransactionDTO.builder()
                .amount(totalAmount)
                .status(status)
                .total(total)
                .systemTax(systemTax)
                .build();
    }

    public List<SalesTransactionDTO> findSalesResult() {
        User user = currentUser();
        var isAdmin = user.getRoles() != null && user.getRoles().contains(Role.MODERATOR);
        return getTransactionsByUserAndStatus(user, isAdmin);
    }

}
