package com.cine.sk.cinesk.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequestDTO {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

}