package com.cine.sk.cinesk.controller;

import com.cine.sk.cinesk.dto.AuthRequestDTO;
import com.cine.sk.cinesk.dto.AuthResponseDTO;
import com.cine.sk.cinesk.dto.RegisterDTO;
import com.cine.sk.cinesk.service.AuthService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Auth", description = "AuthController is responsible for managing authentication and user registration.")
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Login", description = "Authenticate a user and return a JWT token.")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(
                      implementation = AuthResponseDTO.class)))
    @PostMapping(path = "/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO authRequestDTO) {
        return ResponseEntity.ok(authService.login(authRequestDTO).getBody());
    }

    @PostMapping("/register/admin")
    public ResponseEntity<String> registerAdmin(@Valid @RequestBody RegisterDTO registerRequest) {
        authService.registerAdmin(registerRequest);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/register/customer")
    public ResponseEntity<String> registerCustomer(@Valid @RequestBody RegisterDTO registerRequest) {
        authService.registerCustomer(registerRequest);
        return ResponseEntity.ok("User registered successfully");
    }
}
