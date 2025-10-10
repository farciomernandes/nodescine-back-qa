package com.cine.sk.cinesk.domain.transaction;


import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesTransactionSuDTO {
    private Long totalMovie;
    private BigDecimal totalAmount;
    private Long totalUser;
}