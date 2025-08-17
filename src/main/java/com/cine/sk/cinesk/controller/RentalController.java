package com.cine.sk.cinesk.controller;

import com.cine.sk.cinesk.domain.movie.rental.RentalRequestDTO;
import com.cine.sk.cinesk.domain.movie.rental.RentalResponseDTO;
import com.cine.sk.cinesk.domain.movie.rental.RentalService;
import com.cine.sk.cinesk.domain.rental.UserRentalService;
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
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling movie rental operations
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rentals")
@Tag(name = "Movie Rentals", description = "API endpoints for movie rental operations")
public class RentalController {

    private final RentalService rentalService;
    private final UserRentalService userRentalService;

    /**
     * Process a new movie rental transaction
     *
     * @param rentalRequest The rental request details
     * @return Rental transaction details and streaming information
     */
    @Operation(
            summary = "Process a movie rental",
            description = "Creates a new rental transaction for a movie, processes payment, and returns streaming access"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Rental processed successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = RentalResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - user not authenticated"),
            @ApiResponse(responseCode = "403", description = "Forbidden - user not authorized to rent this movie"),
            @ApiResponse(responseCode = "404", description = "Movie not found"),
            @ApiResponse(responseCode = "409", description = "Conflict - user already has an active rental for this movie"),
            @ApiResponse(responseCode = "500", description = "Error processing payment or creating rental")
    })
    @PostMapping
    public ResponseEntity<RentalResponseDTO> processRental(
            @Parameter(description = "Rental request details", required = true)
            @Valid @RequestBody RentalRequestDTO rentalRequest) {
        return rentalService.processRental(rentalRequest);
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
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelRental(
            Authentication authentication,
            @Parameter(description = "Rental ID", required = true)
            @PathVariable String id,
            @Valid @RequestBody CancelRentalRequestDTO request) {

        String userEmail = authentication.getName();
        return userRentalService.cancelRental(userEmail, id, request);
    }
}
