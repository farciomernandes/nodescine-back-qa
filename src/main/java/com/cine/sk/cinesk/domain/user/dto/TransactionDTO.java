package com.cine.sk.cinesk.domain.user.dto;

import com.cine.sk.cinesk.domain.movie.EnhancedMovieResponse;
import com.cine.sk.cinesk.domain.transaction.payment.OrderStatusEnum;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TransactionDTO(
    Long transactionId,
    LocalDateTime createdAt,
    OrderStatusEnum status,
    EnhancedMovieResponse movie,
    boolean expired,
    String encodedImagePix,
    String payloadPix
) {}
