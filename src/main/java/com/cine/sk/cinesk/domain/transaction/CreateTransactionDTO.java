package com.cine.sk.cinesk.domain.transaction;

import com.cine.sk.cinesk.domain.transaction.payment.PaymentDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionDTO {
    private Long movieId;
    private PaymentDTO payment;
}
