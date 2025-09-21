package com.cine.sk.cinesk.domain.auth.dto;

import com.cine.sk.cinesk.domain.auth.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class AuthResponseDTO {

    private String token;

    private String name;

    private String email;

    private Set<Role> roles;

}