package com.cine.sk.cinesk.domain.user.dto;

import com.cine.sk.cinesk.domain.movie.enhanced.EnhancedFilmDTO;
import com.cine.sk.cinesk.domain.transaction.payment.OrderStatusEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class TransactionDTO {

    private Long transactionId;

    private LocalDateTime createdAt;

    private OrderStatusEnum status;

    private EnhancedFilmDTO movie;

    private boolean expired;
}
