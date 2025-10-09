package com.cine.sk.cinesk.domain.transaction;

import com.cine.sk.cinesk.domain.transaction.payment.OrderStatusEnum;
import com.cine.sk.cinesk.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);

    @Query("SELECT t FROM Transaction t WHERE t.movie.createdBy = :createdBy AND t.status = :status")
    List<Transaction> findByMovieCreatedByAndStatus(String createdBy, OrderStatusEnum status);


    @Query("SELECT t FROM Transaction t WHERE t.movie.id = :movieId AND t.status = :status")
    List<Transaction> findByMovieIdAndStatus(Long movieId, OrderStatusEnum status);
}