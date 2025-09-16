package com.cine.sk.cinesk.controller;

import com.cine.sk.cinesk.domain.auth.dto.AuthRequestDTO;
import com.cine.sk.cinesk.domain.auth.dto.AuthResponseDTO;
import com.cine.sk.cinesk.domain.auth.dto.ChangePasswordRequestDTO;
import com.cine.sk.cinesk.domain.auth.AuthService;
import com.cine.sk.cinesk.domain.user.User;
import com.cine.sk.cinesk.domain.user.dto.RegisterDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterDTO registerRequest) {
        return authService.register(registerRequest);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO authRequestDTO) {
        return authService.login(authRequestDTO);
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRequestDTO request, @AuthenticationPrincipal User user) {
        authService.changePassword(user, request);
        return ResponseEntity.ok().build();
    }
}