package com.cine.sk.cinesk.domain.auth.dto;

public record ChangePasswordRequestDTO (
        String oldPassword,
        String newPassword,
        String confirmNewPassword
) { }