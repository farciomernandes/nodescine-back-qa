package com.cine.sk.cinesk.domain.transaction.payment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class OrderDTO {
    private String id;
    private BigDecimal total;
}
