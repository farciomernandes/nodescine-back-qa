package com.cine.sk.cinesk.domain.transaction.rental.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalRequestDTO {
    
    @NotNull(message = "Film ID is required")
    private UUID filmId;
    
    @NotNull(message = "User ID is required")
    private UUID userId;
    
    @NotNull(message = "Payment method is required")
    private String paymentMethod;
    
    @NotNull(message = "Rental duration is required")
    @Min(value = 1, message = "Rental duration must be at least 1 hour")
    private Integer durationHours;
}
