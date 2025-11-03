package com.cine.sk.cinesk.domain.user.dto;

import com.cine.sk.cinesk.domain.movie.Movie;
import com.cine.sk.cinesk.domain.transaction.payment.OrderStatusEnum;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class TransactionDTO {

    private Long transactionId;

    private LocalDateTime createdAt;

    private OrderStatusEnum status;

    private Movie movie;
}
