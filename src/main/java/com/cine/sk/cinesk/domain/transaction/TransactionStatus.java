package com.cine.sk.cinesk.domain.transaction;

public enum TransactionStatus {
    ACTIVE("active"),
    EXPIRED("expired"),
    CANCELLED("cancelled"),
    REFUNDED("refunded");

    private final String value;

    TransactionStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
