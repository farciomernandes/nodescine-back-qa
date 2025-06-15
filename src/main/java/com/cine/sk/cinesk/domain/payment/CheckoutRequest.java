package com.cine.sk.cinesk.domain.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CheckoutRequest {
        private OrderDTO order;
        private UserPaymentDTO user;
        private PaymentDTO payment;
        private AddressDTO address;

}
