package com.cine.sk.cinesk.domain.transaction;

import com.cine.sk.cinesk.domain.transaction.payment.OrderStatusEnum;
import com.cine.sk.cinesk.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);

    @Query("SELECT t FROM Transaction t WHERE t.movie.createdBy = :createdBy AND t.status = :status")
    List<Transaction> findByMovieCreatedByAndStatus(String createdBy, OrderStatusEnum status);

    @Query("SELECT t FROM Transaction t WHERE t.movie.id = :movieId")
    List<Transaction> findByMovie(Long movieId);

    @Query("SELECT t FROM Transaction t WHERE t.status = :status")
    List<Transaction> findByStatus(OrderStatusEnum status);

    Optional<Transaction> findByTransactionId(String transactionId);

    @Query("SELECT t FROM Transaction t WHERE t.movie.id = :movieId AND t.status = :status")
    List<Transaction> findByMovieIdAndStatus(Long movieId, OrderStatusEnum status);

    List<Transaction> findAllByUser_Id(Long userId);
}