package com.cine.sk.cinesk.domain.transaction.payment;

import com.cine.sk.cinesk.domain.transaction.payment.client.AsaasPixResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AsaasPaymentResponse {
    private String id, status, billingType, externalReference, bankSlipUrl;
    private BigDecimal value;
    @JsonProperty("qrCode")
    private AsaasPixResponse pixQrCode;
    private CardInfo creditCard;

    @Data public static class PixInfo { private String encodedImage, payload; }
    @Data public static class CardInfo { private String creditCardNumber, creditCardBrand, creditCardToken; }
}
