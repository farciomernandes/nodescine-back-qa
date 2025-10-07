package com.cine.sk.cinesk.domain.transaction.payment;

import lombok.Data;

@Data
public class AsaasAccountResponse {
    private String object;
    private String id;
    private String name;
    private String email;
    private String loginEmail;
    private String phone;
    private String mobilePhone;
    private String address;
    private String addressNumber;
    private String complement;
    private String province;
    private String postalCode;
    private String cpfCnpj;
    private String birthDate;
    private String personType;
    private String companyType;
    private Integer city;
    private String state;
    private String country;
    private String tradingName;
    private String site;
    private String walletId;
    private AccountNumberDto accountNumber;
    private CommercialInfoExpirationDto commercialInfoExpiration;
    private String apiKey;

    @Data
    public static class AccountNumberDto {
        private String agency;
        private String account;
        private String accountDigit;
    }

    @Data
    public static class CommercialInfoExpirationDto {
        private Boolean isExpired;
        private String scheduledDate;
    }
}
