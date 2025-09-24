package com.cine.sk.cinesk.domain.transaction;

import com.cine.sk.cinesk.domain.movie.Movie;
import com.cine.sk.cinesk.domain.movie.MovieRepository;
import com.cine.sk.cinesk.domain.user.User;
import com.cine.sk.cinesk.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final MovieRepository movieRepository;
    private final UserService userService;

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

    public Transaction create(Long movieId) {
        User user = currentUser();

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Filme não encontrado"));

        Transaction tx = Transaction.builder()
                .user(user)
                .movie(movie)
                .amount(movie.getPrice())
                .date(OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                .status(TransactionStatus.ACTIVE)
                .build();
        return transactionRepository.save(tx);
    }

    public List<Transaction> findTransactionByUser(User user) {
        return transactionRepository.findByUser(user);
    }
}
