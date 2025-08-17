package com.cine.sk.cinesk.domain.movie.rental;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalResponseDTO {
    
    private UUID rentalId;
    private UUID filmId;
    private UUID userId;
    private LocalDateTime rentedAt;
    private LocalDateTime expiresAt;
    private String paymentId;
    private String status;
    private String streamUrl;
    private String message;
}
