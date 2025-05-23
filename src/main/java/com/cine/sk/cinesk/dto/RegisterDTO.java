package com.cine.sk.cinesk.dto;

import com.cine.sk.cinesk.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Schema(description = "Request object for user registration containing email, password, name, and roles.")
public class RegisterDTO {

    @Schema(description = "User's email address", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Schema(description = "User's password", example = "P@ssw0rd", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @Schema(description = "User's full name", example = "John Doe", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
}
