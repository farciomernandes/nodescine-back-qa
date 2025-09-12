package com.cine.sk.cinesk.controller.dto;

import com.cine.sk.cinesk.domain.user.UserEntity;

public record AuthResponseDTO (
        String token,
        UserEntity user
) { }