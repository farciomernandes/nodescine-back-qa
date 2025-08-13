package com.cine.sk.cinesk.controller;

import com.cine.sk.cinesk.domain.user.AuthRequestDTO;
import com.cine.sk.cinesk.domain.user.AuthResponseDTO;
import com.cine.sk.cinesk.domain.user.RegisterDTO;
import com.cine.sk.cinesk.domain.user.UserEntity;
import com.cine.sk.cinesk.domain.user.AuthService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Auth", description = "AuthController is responsible for managing authentication and user registration.")
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Login", description = "Authenticate a user and return a JWT token.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Authentication successful",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = AuthResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Invalid credentials"
        )
    })
    @PostMapping(path = "/login")
    public ResponseEntity<AuthResponseDTO> login(
            @Parameter(description = "Login credentials", required = true)
            @Valid @RequestBody AuthRequestDTO authRequestDTO) {
        return ResponseEntity.ok(authService.login(authRequestDTO).getBody());
    }

    @Operation(
        summary = "Register admin user",
        description = "Registers a new user with admin privileges"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Admin user registered successfully"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid registration data or email already exists"
        )
    })
    @PostMapping("/register/admin")
    public ResponseEntity<String> registerAdmin(
            @Parameter(description = "Admin registration details", required = true)
            @Valid @RequestBody RegisterDTO registerRequest) {
        authService.registerAdmin(registerRequest);
        return ResponseEntity.ok("User registered successfully");
    }

    @Operation(
        summary = "Register customer user",
        description = "Registers a new user with customer privileges"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Customer user registered successfully"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid registration data or email already exists"
        )
    })
    @PostMapping("/register/customer")
    public ResponseEntity<String> registerCustomer(
            @Parameter(description = "Customer registration details", required = true)
            @Valid @RequestBody RegisterDTO registerRequest) {
        authService.registerCustomer(registerRequest);
        return ResponseEntity.ok("User registered successfully");
    }

    @Operation(
        summary = "Get all users",
        description = "Returns a paginated list of all users in the system"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Users retrieved successfully"
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied - requires admin privileges"
        )
    })
    @GetMapping
    public ResponseEntity<Page<List<UserEntity>>> getAllUsers(
            @Parameter(description = "Pagination parameters")
            Pageable pageable) {
        return authService.getAll(pageable);
    }
}
