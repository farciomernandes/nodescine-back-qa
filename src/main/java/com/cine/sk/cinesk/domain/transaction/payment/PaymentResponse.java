package com.cine.sk.cinesk.domain.transaction.payment;

import com.cine.sk.cinesk.domain.transaction.payment.client.AsaasPixResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private ProcessPaymentResponse paymentResponse;
    private AsaasPixResponse pix;

}
