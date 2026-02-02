package com.cine.sk.cinesk.controller;

import com.cine.sk.cinesk.domain.auth.dto.AuthRequest;
import com.cine.sk.cinesk.domain.auth.dto.AuthResponse;
import com.cine.sk.cinesk.domain.auth.dto.ChangePasswordRequest;
import com.cine.sk.cinesk.domain.auth.AuthService;
import com.cine.sk.cinesk.domain.user.dto.CustomerRegisterDTO;
import com.cine.sk.cinesk.domain.user.dto.UserRegisterRequest;
import com.cine.sk.cinesk.domain.user.dto.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register/moderator")
    public ResponseEntity<String> register(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
        return authService.register(userRegisterRequest);
    }

    @PostMapping("/register/customer")
    public ResponseEntity<String> registerCustomer(@Valid @RequestBody CustomerRegisterDTO registerRequest) {
        return authService.registerCustomer(registerRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        return authService.login(authRequest);
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        authService.changePassword(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me() {
        return authService.me();
    }
}