package com.cine.sk.cinesk.domain.transaction;

import com.cine.sk.cinesk.domain.movie.Movie;
import com.cine.sk.cinesk.domain.transaction.payment.OrderStatusEnum;
import com.cine.sk.cinesk.domain.transaction.payment.client.AsaasPixResponse;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class TransactionResponse {

    private long id;

    private String transactionId;

    private Long movieId;

    private BigDecimal amount;

    private String date;

    private OrderStatusEnum status;

    private AsaasPixResponse pix;
}
