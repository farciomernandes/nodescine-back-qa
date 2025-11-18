package com.cine.sk.cinesk.domain.transaction;

import com.cine.sk.cinesk.domain.movie.Movie;
import com.cine.sk.cinesk.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByTransactionId(String transactionId);

    @Query("SELECT t FROM Transaction t JOIN FETCH t.movie m LEFT JOIN FETCH m.category LEFT JOIN FETCH m.genres WHERE t.user = :user")
    List<Transaction> findByUserWithDetails(@Param("user") User user);

    List<Transaction> findByUser(User user);

    @Query("SELECT t FROM Transaction t WHERE t.movie.id = :movieId")
    List<Transaction> findByMovie(@Param("movieId") Long movieId);

    List<Transaction> findAllByUser_Id(Long userId);
}
