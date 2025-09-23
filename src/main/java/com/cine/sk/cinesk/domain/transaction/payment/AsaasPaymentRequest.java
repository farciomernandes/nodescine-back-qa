package com.cine.sk.cinesk.domain.transaction.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AsaasPaymentRequest {
    private String customer, billingType, description, externalReference;
    private BigDecimal value;
    private String dueDate;
    private CreditCard creditCard;
    private CreditCardHolderInfo creditCardHolderInfo;

    @Data @Builder public static class CreditCard {
        private String holderName, number, expiryMonth, expiryYear, ccv;
    }

    @Data @Builder public static class CreditCardHolderInfo {
        private String name, email, cpfCnpj, postalCode, addressNumber, addressComplement, phone, mobilePhone;
    }
}
