package com.cine.sk.cinesk.controller;

import com.cine.sk.cinesk.domain.payment.CheckoutRequest;
import com.cine.sk.cinesk.domain.payment.PaymentService;
import com.cine.sk.cinesk.domain.payment.ProcessPaymentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/checkout")
@Tag(name = "Checkout", description = "API endpoints for payment processing")
public class CheckoutController {

    private final PaymentService paymentService;

    /**
     * Endpoint para processar um pagamento.
     * Recebe os dados do pedido, cliente, pagamento e endereço.
     *
     * @param checkoutRequest O objeto contendo todos os dados para o pagamento.
     * @return ResponseEntity com o resultado da transação ou um erro.
     */
    @Operation(
        summary = "Process payment",
        description = "Processes a payment for movie rental or purchase"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Payment processed successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ProcessPaymentResponse.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid payment information"),
        @ApiResponse(responseCode = "422", description = "Payment processing failed"),
        @ApiResponse(responseCode = "403", description = "Payment not authorized")
    })
    @PostMapping("/process")
    public ResponseEntity<ProcessPaymentResponse> processPayment(
            @Parameter(description = "Payment details including order, user, payment and address information", required = true)
            @RequestBody CheckoutRequest checkoutRequest) {
        ProcessPaymentResponse response = paymentService.process(
                checkoutRequest.getOrder(),
                checkoutRequest.getUser(),
                checkoutRequest.getPayment(),
                checkoutRequest.getAddress()
        );

        return ResponseEntity.ok(response);
    }
}
