package com.cine.sk.cinesk.domain.user.dto;

import com.cine.sk.cinesk.domain.movie.Movie;
import com.cine.sk.cinesk.domain.transaction.payment.OrderStatusEnum;
import lombok.*;

@Getter
@Setter
@Builder
public class TransactionDTO {

    private Long transactionId;

    private OrderStatusEnum status;

    private Movie movie;
}
