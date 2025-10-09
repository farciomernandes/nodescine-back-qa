package com.cine.sk.cinesk.domain.transaction;


import com.cine.sk.cinesk.domain.transaction.payment.OrderStatusEnum;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesTransactionDTO {
    private BigDecimal amount;
    private OrderStatusEnum status;
    private BigDecimal total;
    private BigDecimal systemTax;
}