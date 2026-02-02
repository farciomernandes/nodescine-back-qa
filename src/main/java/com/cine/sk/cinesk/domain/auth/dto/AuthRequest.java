package com.cine.sk.cinesk.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AuthRequest(
    @Email
    @NotBlank
    String email,
    @NotBlank
    String password
) {}
