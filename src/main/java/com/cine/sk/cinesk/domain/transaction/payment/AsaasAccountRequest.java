package com.cine.sk.cinesk.domain.transaction.payment;

import lombok.Data;
import java.util.List;

@Data
public class AsaasAccountRequest {
    private String name;
    private String email;
    private String cpfCnpj;
    private String mobilePhone;
    private Integer incomeValue;
    private String address;
    private String addressNumber;
    private String province;
    private String postalCode;
    private String loginEmail;
    private String birthDate;
    private String companyType;
    private String phone;
    private String site;
    private String complement;
    private List<AsaasWebhook> webhooks;
}