package com.cine.sk.cinesk.controller;

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
    public ResponseEntity<Map<String, Boolean>> handleWebhook(@RequestBody Map<String, Object> body) {
        String event = (String) body.get("event");
        Map<String, Object> payment = (Map<String, Object>) body.get("payment");
        webhookService.processWebhookPayloadAsync(event, payment.get("id").toString());
        return ResponseEntity.ok(Map.of("received", true));
    }

}
