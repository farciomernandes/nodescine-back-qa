package com.cine.sk.cinesk.controller;

import com.cine.sk.cinesk.domain.user.UpdateProfileResponseDTO;
import com.cine.sk.cinesk.domain.user.UserProfileResponseDTO;
import com.cine.sk.cinesk.domain.user.UserService;
import com.cine.sk.cinesk.domain.user.UserStatsResponseDTO;
import com.cine.sk.cinesk.domain.user.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling user profile operations
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "User Profile", description = "API endpoints for user profile management")
@SecurityRequirement(name = "Bearer Authentication")
public class UserProfileController {

    private final UserService userService;

    /**
     * Get user profile information
     *
     * @param authentication The authenticated user
     * @return User profile with subscription and preferences
     */
    @Operation(
            summary = "Get user profile",
            description = "Retrieves complete user profile including subscription and preferences"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Profile retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserProfileResponseDTO.class))
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponseDTO> getUserProfile(Authentication authentication) {
        String userEmail = authentication.getName();
        return userService.getUserProfile(userEmail);
    }

    /**
     * Update user profile
     *
     * @param authentication The authenticated user
     * @param request The profile update request
     * @return Updated user profile
     */
    @Operation(
            summary = "Update user profile",
            description = "Updates user name and preferences"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Profile updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UpdateProfileResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/profile")
    public ResponseEntity<UpdateProfileResponseDTO> updateUserProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateProfileRequestDTO request) {
        String userEmail = authentication.getName();
        return userService.updateUserProfile(userEmail, request);
    }

    /**
     * Get user statistics
     *
     * @param authentication The authenticated user
     * @return User statistics including watch time and rental history
     */
    @Operation(
            summary = "Get user statistics",
            description = "Retrieves user statistics including total rentals, watch time, and monthly usage"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Statistics retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserStatsResponseDTO.class))
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/stats")
    public ResponseEntity<UserStatsResponseDTO> getUserStats(Authentication authentication) {
        String userEmail = authentication.getName();
        return userService.getUserStats(userEmail);
    }
}
