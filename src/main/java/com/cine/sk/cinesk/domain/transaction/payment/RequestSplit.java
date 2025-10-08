package com.cine.sk.cinesk.domain.transaction.payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestSplit {
    @NotBlank(message = "walletId é obrigatório")
    private String walletId;

    @NotNull(message = "fixedValue é obrigatório")
    private Double fixedValue; // Ou BigDecimal, se preferir

    @NotNull(message = "percentualValue é obrigatório")
    private Double percentualValue; // Ou BigDecimal

    @NotNull(message = "totalFixedValue é obrigatório")
    private Double totalFixedValue; // Ou BigDecimal

    @NotBlank(message = "externalReference é obrigatório")
    private String externalReference;

    @NotBlank(message = "description é obrigatório")
    private String description;
}
