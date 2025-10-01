package com.cine.sk.cinesk.domain.transaction;

import com.cine.sk.cinesk.domain.movie.Movie;
import com.cine.sk.cinesk.domain.transaction.payment.OrderStatusEnum;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TransactionResponse {

    private long id;

    private String transactionId;

    private Long movieId;

    private Long amount;

    private String date;

    private OrderStatusEnum status;
}
