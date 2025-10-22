package com.cine.sk.cinesk.domain.transaction.payment;

import lombok.Data;

import java.util.List;

@Data
public class AsaasWebhook {
    private String authToken;
    private List<Event> events;
    private String name;
    private String url;
    private String email;
    private boolean enabled;
    private boolean interrupted;
    private int apiVersion;
    private String sendType = "SEQUENTIALLY";

    public enum Event {
        PAYMENT_REFUNDED,
        PAYMENT_RECEIVED,
        PAYMENT_OVERDUE,
        PAYMENT_DELETED,
        PAYMENT_CREATED
    }
}
