package com.cine.sk.cinesk.domain.rental.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "RentalsStats", description = "Statistics for user rentals")
public class RentalsStatusDTO {
    @JsonProperty("total_rentals")
    private Integer totalRentals;

    @JsonProperty("active_rentals")
    private Integer activeRentals;

    @JsonProperty("total_spent")
    private String totalSpent;
}
