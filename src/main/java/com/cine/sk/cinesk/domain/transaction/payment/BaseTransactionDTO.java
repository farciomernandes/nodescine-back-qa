package com.cine.sk.cinesk.domain.transaction.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseTransactionDTO {
    private String id;
    private BigDecimal value;
    private String status;
    private String billingType;
    private String externalReference;
}
