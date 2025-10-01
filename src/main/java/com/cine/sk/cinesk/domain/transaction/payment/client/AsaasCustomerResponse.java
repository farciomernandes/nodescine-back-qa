package com.cine.sk.cinesk.domain.transaction.payment.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AsaasCustomerResponse {
    private String id, name, email, mobilePhone, cpfCnpj;
}
