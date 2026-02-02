package com.cine.sk.cinesk.domain.auth.dto;

import com.cine.sk.cinesk.domain.auth.Role;
import lombok.Builder;

import java.util.Set;

@Builder
public record AuthResponse(
    String token,
    String name,
    String email,
    Set<Role> roles
) {}
