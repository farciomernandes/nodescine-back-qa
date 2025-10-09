package com.cine.sk.cinesk.domain.transaction;


import com.cine.sk.cinesk.domain.transaction.payment.OrderStatusEnum;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesTransactionDTO {
    private Long movieId;
    private String movieName;
    private BigDecimal amount;
    private OrderStatusEnum status;
    private BigDecimal total;
    private BigDecimal systemTax;
    private List<Transaction> transactions;
}