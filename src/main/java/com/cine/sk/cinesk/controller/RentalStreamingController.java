package com.cine.sk.cinesk.controller;

import com.cine.sk.cinesk.domain.rental.dto.ProgressResponseDTO;
import com.cine.sk.cinesk.domain.rental.dto.ProgressUpdateDTO;
import com.cine.sk.cinesk.domain.rental.service.RentalStreamingService;
import com.cine.sk.cinesk.domain.rental.dto.StreamInfoDTO;
import com.cine.sk.cinesk.domain.rental.dto.SubtitleDTO;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller for handling movie streaming features for rentals
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rentals")
@Tag(name = "Rental Streaming", description = "API endpoints for movie streaming functionality")
public class RentalStreamingController {

    private final RentalStreamingService rentalStreamingService;

    /**
     * Returns streaming URL and options for a rented movie
     *
     * @param id The rental ID
     * @return Streaming information including URL, expiration, and quality options
     */
    @Operation(
        summary = "Get streaming URL for a rental",
        description = "Returns streaming URL, quality options, and DRM information for a rented movie"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Streaming information retrieved successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = StreamInfoDTO.class))
        ),
        @ApiResponse(responseCode = "403", description = "Access denied - user does not own this rental"),
        @ApiResponse(responseCode = "404", description = "Rental not found")
    })
    @GetMapping("/{id}/stream")
    public ResponseEntity<StreamInfoDTO> getStreamingUrl(
            @Parameter(description = "Rental identifier", required = true)
            @PathVariable UUID id) {
        return rentalStreamingService.getStreamingInfo(id);
    }

    /**
     * Updates the viewing progress of a rented movie
     *
     * @param id The rental ID
     * @param progressUpdate The progress update data
     * @return Updated progress information
     */
    @Operation(
        summary = "Update viewing progress",
        description = "Saves the current viewing progress of a rented movie"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Progress updated successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ProgressResponseDTO.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "403", description = "Access denied - user does not own this rental"),
        @ApiResponse(responseCode = "404", description = "Rental not found")
    })
    @PostMapping("/{id}/progress")
    public ResponseEntity<ProgressResponseDTO> updateProgress(
            @Parameter(description = "Rental identifier", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Progress update details", required = true)
            @Valid @RequestBody ProgressUpdateDTO progressUpdate) {
        return rentalStreamingService.updateProgress(id, progressUpdate);
    }

    /**
     * Returns available subtitle options for a rented movie
     *
     * @param id The rental ID
     * @return List of available subtitles
     */
    @Operation(
        summary = "Get available subtitles",
        description = "Returns a list of all available subtitle options for a rented movie"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Subtitles retrieved successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = SubtitleDTO.class))
        ),
        @ApiResponse(responseCode = "403", description = "Access denied - user does not own this rental"),
        @ApiResponse(responseCode = "404", description = "Rental not found")
    })
    @GetMapping("/{id}/subtitles")
    public ResponseEntity<List<SubtitleDTO>> getSubtitles(
            @Parameter(description = "Rental identifier", required = true)
            @PathVariable UUID id) {
        return rentalStreamingService.getSubtitles(id);
    }
}
