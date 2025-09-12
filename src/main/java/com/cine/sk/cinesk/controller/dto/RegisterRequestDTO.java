package com.cine.sk.cinesk.controller.dto;

public record RegisterRequestDTO (
        String name,
        String email,
        String password,
        String confirmPassword
) { }