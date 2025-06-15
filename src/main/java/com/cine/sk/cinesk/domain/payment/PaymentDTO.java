package com.cine.sk.cinesk.domain.payment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaymentDTO {
    private PaymentMethodEnum method;
    private String creditCardHolder;
    private String creditCardNumber;
    private String creditCardExpiration;
    private String creditCardSecurityCode;
}
