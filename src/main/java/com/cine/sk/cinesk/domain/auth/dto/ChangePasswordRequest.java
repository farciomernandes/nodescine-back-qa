package com.cine.sk.cinesk.domain.auth.dto;

public record ChangePasswordRequest(
        String oldPassword,
        String newPassword,
        String confirmNewPassword
) { }