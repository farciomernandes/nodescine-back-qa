package com.cine.sk.cinesk.controller;

import com.cine.sk.cinesk.domain.transaction.webhook.WebhookRequestDTO;
import com.cine.sk.cinesk.domain.transaction.webhook.WebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/asaas/webhook")
public class WebHookController {

    private final WebhookService webhookService;

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Map<String, Boolean>> handleWebhook(@RequestBody WebhookRequestDTO request) {
        String event = request.getEvent();
        String paymentId = request.getPayment().getId();

        webhookService.processWebhookPayloadAsync(event, paymentId);
        return ResponseEntity.ok(Map.of("received", true));
    }


}
