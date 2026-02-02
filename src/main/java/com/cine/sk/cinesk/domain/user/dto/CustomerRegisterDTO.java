package com.cine.sk.cinesk.domain.user.dto;

import com.cine.sk.cinesk.domain.auth.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Set;

@Getter
@Setter
@Schema(description = "Request object for user registration containing email, password, name, and roles.")
public class CustomerRegisterDTO {

    @NotBlank
    @NotNull
    @Email
    @Schema(description = "User's email address", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank
    @NotNull
    @Schema(description = "User's password", example = "P@ssw0rd", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @NotBlank
    @NotNull
    @Schema(description = "User's full name", example = "John Doe", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    private String avatar;

    @NotBlank
    @NotNull
    private String cpf;

    @NotBlank
    @NotNull
    private String phone;

    @NotBlank
    @NotNull
    private String postalCode;

    @NotBlank
    @NotNull
    private String address;

    @NotBlank
    @NotNull
    private String addressNumber;

    @NotBlank
    @NotNull
    private String complement;

    @NotBlank
    @NotNull
    private String province;

    @NotEmpty(message = "O set de roles não pode ser null ou vazio")
    @Schema(description = "Set of roles assigned to the user", requiredMode = Schema.RequiredMode.REQUIRED)
    private Set<Role> roles;

    @NotNull(message = "Data de nascimento é obrigatória")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Formato deve ser yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String birthDate;

    private Integer incomeValue;
}
