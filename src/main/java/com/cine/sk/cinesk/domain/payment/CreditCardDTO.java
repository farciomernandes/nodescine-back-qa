package com.cine.sk.cinesk.domain.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreditCardDTO {
    private String creditCardNumber;
    private String creditCardBrand;
    private String creditCardToken;
}