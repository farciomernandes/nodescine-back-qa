package com.cine.sk.cinesk.domain.transaction.payment;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BoletoTransactionDTO {
    private BaseTransactionDTO paymentInfo;
    private String bankSlipUrl;
}

