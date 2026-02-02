package com.cine.sk.cinesk.domain.email;

public record EmailRequest(
        String emailTo,
        String subject,
        String text
) {
}