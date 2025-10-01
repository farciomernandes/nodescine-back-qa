package com.cine.sk.cinesk.domain.transaction.payment;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ProcessPaymentResponse {
    private String transactionId;
    private OrderStatusEnum status;
    private Object transaction;
}
