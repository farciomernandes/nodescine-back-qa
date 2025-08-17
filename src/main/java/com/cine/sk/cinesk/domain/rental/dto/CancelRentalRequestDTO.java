package com.cine.sk.cinesk.domain.rental.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CancelRentalRequest", description = "Request to cancel a rental")
public class CancelRentalRequestDTO {
    @Schema(example = "changed_mind")
    @NotBlank
    @JsonProperty("reason")
    private String reason;

    @Schema(example = "Não consegui assistir no prazo")
    @JsonProperty("feedback")
    private String feedback;
}
