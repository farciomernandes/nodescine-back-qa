package com.cine.sk.cinesk.domain.transaction.payment.client;

import com.cine.sk.cinesk.domain.transaction.payment.AsaasPaymentRequest;
import com.cine.sk.cinesk.domain.transaction.payment.AsaasPaymentResponse;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.cine.sk.cinesk.domain.transaction.payment.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "asaas-api", url = "${ASAAS_API_URL}")
@Headers("Content-Type: application/json")
public interface AsaasApiClient {

    @GetMapping("/customers")
    AsaasCustomerListResponse findCustomerByEmail(@RequestHeader("access_token") String accessToken, @RequestParam("email") String email);

    @PostMapping("/customers")
    AsaasCustomerResponse createCustomer(@RequestHeader("access_token") String accessToken, @RequestBody AsaasCustomerResponse customerData);

    @PostMapping("/accounts")
    AsaasAccountResponse createAccount(@RequestHeader("access_token") String accessToken, @RequestBody AsaasAccountRequest paymentRequest);

    @PostMapping("/payments")
    AsaasPaymentResponse createPayment(@RequestHeader("access_token") String accessToken, @RequestBody AsaasPaymentRequest paymentRequest);
}
