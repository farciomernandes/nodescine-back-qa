package com.cine.sk.cinesk.controller;

import com.cine.sk.cinesk.domain.auth.dto.AuthRequestDTO;
import com.cine.sk.cinesk.domain.auth.dto.AuthResponseDTO;
import com.cine.sk.cinesk.domain.auth.dto.ChangePasswordRequestDTO;
import com.cine.sk.cinesk.domain.auth.AuthService;
import com.cine.sk.cinesk.domain.user.User;
import com.cine.sk.cinesk.domain.user.dto.CustomerRegisterDTO;
import com.cine.sk.cinesk.domain.user.dto.RegisterDTO;
import com.cine.sk.cinesk.domain.user.dto.UserDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @PostMapping("/register/moderator")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterDTO registerRequest) {
        return authService.register(registerRequest);
    }

    @PostMapping("/register/customer")
    public ResponseEntity<AuthResponseDTO> registerCustomer(@Valid @RequestBody CustomerRegisterDTO registerRequest) {
        return authService.registerCustomer(registerRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO authRequestDTO) {
        return authService.login(authRequestDTO);
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequestDTO request) {
        authService.changePassword(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> me() {
        return authService.me();
    }
}