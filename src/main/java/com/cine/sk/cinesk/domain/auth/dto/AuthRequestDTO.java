package com.cine.sk.cinesk.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequestDTO {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

}