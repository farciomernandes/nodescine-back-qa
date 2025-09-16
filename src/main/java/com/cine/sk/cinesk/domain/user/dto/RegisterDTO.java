package com.cine.sk.cinesk.domain.user.dto;

import com.cine.sk.cinesk.domain.auth.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Schema(description = "Request object for user registration containing email, password, name, and roles.")
public class RegisterDTO {

    @NotBlank
    @Email
    @Schema(description = "User's email address", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank
    @Schema(description = "User's password", example = "P@ssw0rd", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @NotBlank
    @Schema(description = "User's full name", example = "John Doe", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank
    @Schema(description = "Set of roles assigned to the user", requiredMode = Schema.RequiredMode.REQUIRED)
    private Set<Role> roles;
}
