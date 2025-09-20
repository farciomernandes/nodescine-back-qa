package com.cine.sk.cinesk.controller;

import com.cine.sk.cinesk.domain.rental.dto.ActiveRentalsResponseDTO;
import com.cine.sk.cinesk.domain.rental.service.UserRentalService;
import com.cine.sk.cinesk.domain.rental.dto.UserRentalsResponseDTO;
import com.cine.sk.cinesk.domain.rental.dto.CancelRentalRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling user rental operations
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserRentalsController {

    private final UserRentalService userRentalService;

    /**
     * Get user's rental history with pagination and filtering
     *
     * @param authentication The authenticated user
     * @param status Filter by rental status (all, active, expired)
     * @param page Page number (default 1)
     * @param limit Items per page (default 10, max 100)
     * @return Paginated list of user rentals with statistics
     */
    @Operation(
            summary = "Get user rentals",
            description = "Retrieves user's rental history with pagination and optional status filtering"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Rentals retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserRentalsResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/rentals")
    public ResponseEntity<UserRentalsResponseDTO> getUserRentals(
            Authentication authentication,
            @Parameter(description = "Filter by status: all, active, expired")
            @RequestParam(defaultValue = "all") String status,
            @Parameter(description = "Page number (1-based)")
            @RequestParam(defaultValue = "1") @Min(1) Integer page,
            @Parameter(description = "Items per page")
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer limit) {

        String userEmail = authentication.getName();
        return userRentalService.getUserRentals(userEmail, status, page, limit);
    }

    /**
     * Get user's active rentals with streaming information
     *
     * @param authentication The authenticated user
     * @return List of active rentals with streaming URLs
     */
    @Operation(
            summary = "Get active rentals",
            description = "Retrieves user's currently active rentals with streaming information"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Active rentals retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ActiveRentalsResponseDTO.class))
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/rentals/active")
    public ResponseEntity<ActiveRentalsResponseDTO> getActiveRentals(Authentication authentication) {
        String userEmail = authentication.getName();
        return userRentalService.getActiveRentals(userEmail);
    }

    /**
     * Cancel a rental
     *
     * @param authentication The authenticated user
     * @param id The rental ID to cancel
     * @param request Cancellation reason and feedback
     * @return No content on successful cancellation
     */
    @Operation(
            summary = "Cancel a rental",
            description = "Cancels a user's rental with reason and optional feedback"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Rental cancelled successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid cancellation reason"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT"),
            @ApiResponse(responseCode = "403", description = "Forbidden - rental does not belong to user"),
            @ApiResponse(responseCode = "404", description = "Rental not found")
    })
    @DeleteMapping("/rentals/{id}")
    public ResponseEntity<Void> cancelRental(
            Authentication authentication,
            @Parameter(description = "Rental ID", required = true)
            @PathVariable String id,
            @Valid @RequestBody CancelRentalRequestDTO request) {

        String userEmail = authentication.getName();
        return userRentalService.cancelRental(userEmail, id, request);
    }
}
