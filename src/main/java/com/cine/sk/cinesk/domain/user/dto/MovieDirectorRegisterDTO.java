package com.cine.sk.cinesk.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Request object for simplified movie director registration containing email, password and name.")
public class MovieDirectorRegisterDTO {

    @NotBlank
    @Email
    @Schema(description = "User's email address", example = "director@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank
    @Schema(description = "User's password", example = "P@ssw0rd", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @NotBlank
    @Schema(description = "User's full name", example = "Jane Director", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    private String avatar;
}

