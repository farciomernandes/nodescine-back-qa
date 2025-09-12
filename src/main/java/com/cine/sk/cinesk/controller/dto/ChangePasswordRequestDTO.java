package com.cine.sk.cinesk.controller.dto;

public record ChangePasswordRequestDTO (
        String oldPassword,
        String newPassword,
        String confirmNewPassword
) { }