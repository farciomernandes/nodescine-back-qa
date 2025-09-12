package com.cine.sk.cinesk.controller;

import com.cine.sk.cinesk.controller.dto.AuthRequestDTO;
import com.cine.sk.cinesk.controller.dto.AuthResponseDTO;
import com.cine.sk.cinesk.controller.dto.ChangePasswordRequestDTO;
import com.cine.sk.cinesk.controller.dto.RegisterRequestDTO;
import com.cine.sk.cinesk.domain.user.AuthService;
import com.cine.sk.cinesk.domain.user.UserEntity;
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
    public ResponseEntity<AuthResponseDTO> register(@RequestBody RegisterRequestDTO request) {
        AuthResponseDTO response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO request) {
        AuthResponseDTO response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRequestDTO request, @AuthenticationPrincipal UserEntity user) {
        authService.changePassword(user, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserEntity> getAuthenticatedUser(@AuthenticationPrincipal UserEntity user) {
        return ResponseEntity.ok(user);
    }
}