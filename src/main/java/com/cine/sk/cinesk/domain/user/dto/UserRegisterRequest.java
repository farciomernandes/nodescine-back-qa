package com.cine.sk.cinesk.domain.user.dto;

import com.cine.sk.cinesk.domain.auth.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record UserRegisterRequest(
    @NotBlank
    @Email
    @Schema(description = "User's email address", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    String email,

    @NotBlank
    @Schema(description = "User's password", example = "P@ssw0rd", requiredMode = Schema.RequiredMode.REQUIRED)
    String password,

    @NotBlank
    @Schema(description = "User's full name", example = "John Doe", requiredMode = Schema.RequiredMode.REQUIRED)
    String name,

    String avatar,

    @NotBlank
    @Schema(description = "Set of roles assigned to the user", requiredMode = Schema.RequiredMode.REQUIRED)
    Set<Role> roles,

    @NotBlank
    String cpf,

    @NotBlank
    String phone,

    @NotBlank
    String postalCode,

    @NotBlank
    String address,

    @NotBlank
    String addressNumber,

    @NotBlank
    String complement,

    @NotBlank
    String province
) {}
