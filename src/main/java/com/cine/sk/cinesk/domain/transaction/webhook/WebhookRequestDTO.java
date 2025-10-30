package com.cine.sk.cinesk.domain.transaction.webhook;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebhookRequestDTO {

    private String id;
    private String event;
    private String dateCreated;
    private Payment payment;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Payment {

        private String object;
        private String id;
        private String dateCreated;
        private String customer;
        private String checkoutSession;
        private String paymentLink;
        private BigDecimal value;
        private BigDecimal netValue;
        private BigDecimal originalValue;
        private BigDecimal interestValue;
        private String description;
        private String billingType;
        private String confirmedDate;
        private CreditCard creditCard;
        private Object pixTransaction; // pode ser null
        private String status;
        private String dueDate;
        private String originalDueDate;
        private String paymentDate;
        private String clientPaymentDate;
        private Integer installmentNumber;
        private String invoiceUrl;
        private String invoiceNumber;
        private String externalReference;
        private Boolean deleted;
        private Boolean anticipated;
        private Boolean anticipable;
        private String creditDate;
        private String estimatedCreditDate;
        private String transactionReceiptUrl;
        private String nossoNumero;
        private String bankSlipUrl;
        private String lastInvoiceViewedDate;
        private String lastBankSlipViewedDate;
        private List<Split> split;
        private Boolean postalService;
        private Object escrow; // pode ser null
        private Object refunds; // pode ser null
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreditCard {
        private String creditCardNumber;
        private String creditCardBrand;
        private String creditCardToken;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Split {
        private String id;
        private String walletId;
        private BigDecimal fixedValue;
        private BigDecimal percentualValue;
        private BigDecimal totalValue;
        private String cancellationReason;
        private String status;
        private String externalReference;
        private String description;
    }
}