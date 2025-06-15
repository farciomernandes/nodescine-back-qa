package com.cine.sk.cinesk.controller;

import com.cine.sk.cinesk.domain.payment.CheckoutRequest;
import com.cine.sk.cinesk.domain.payment.PaymentService;
import com.cine.sk.cinesk.domain.payment.ProcessPaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CheckoutController {

    private final PaymentService paymentService;

    /**
     * Endpoint para processar um pagamento.
     * Recebe os dados do pedido, cliente, pagamento e endereço.
     *
     * @param checkoutRequest O objeto contendo todos os dados para o pagamento.
     * @return ResponseEntity com o resultado da transação ou um erro.
     */
    @PostMapping("/process")
    public ResponseEntity<ProcessPaymentResponse> processPayment(@RequestBody CheckoutRequest checkoutRequest) {
        ProcessPaymentResponse response = paymentService.process(
                checkoutRequest.getOrder(),
                checkoutRequest.getUser(),
                checkoutRequest.getPayment(),
                checkoutRequest.getAddress()
        );

        return ResponseEntity.ok(response);
    }
}
