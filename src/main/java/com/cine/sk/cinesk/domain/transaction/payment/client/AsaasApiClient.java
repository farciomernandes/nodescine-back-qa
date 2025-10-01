package com.cine.sk.cinesk.domain.transaction.payment.client;

import com.cine.sk.cinesk.domain.transaction.payment.AsaasPaymentRequest;
import com.cine.sk.cinesk.domain.transaction.payment.AsaasPaymentResponse;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

package com.cine.sk.cinesk.domain.transaction.payment.client;

import com.cine.sk.cinesk.domain.transaction.payment.*;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "asaas-api", url = "${ASAAS_API_URL}")
@Headers("Content-Type: application/json")
public interface AsaasApiClient {

    @GetMapping("/customers")
    AsaasCustomerListResponse findCustomerByEmail(@RequestParam("email") String email);

    @PostMapping("/customers")
    AsaasCustomerResponse createCustomer(@RequestBody AsaasCustomerResponse customerData);

    @PostMapping("/payments")
    AsaasPaymentResponse createPayment(@RequestBody AsaasPaymentRequest paymentRequest);
}
