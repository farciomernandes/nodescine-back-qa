package com.cine.sk.cinesk.domain.email.dto;

public record EmailRequestDTO(
        String emailTo,
        String subject,
        String text
) {
}