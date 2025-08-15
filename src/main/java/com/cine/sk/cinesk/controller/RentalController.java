package com.cine.sk.cinesk.controller;

import com.cine.sk.cinesk.domain.movie.rental.RentalRequestDTO;
import com.cine.sk.cinesk.domain.movie.rental.RentalResponseDTO;
import com.cine.sk.cinesk.domain.movie.rental.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling movie rental operations
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rentals")
@Tag(name = "Movie Rentals", description = "API endpoints for movie rental operations")
public class RentalController {

    private final RentalService rentalService;

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
}
