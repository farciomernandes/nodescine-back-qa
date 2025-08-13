package com.cine.sk.cinesk.controller;

import com.cine.sk.cinesk.domain.movie.GenreDTO;
import com.cine.sk.cinesk.domain.movie.GenreService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/genres")
@Tag(name = "Genres", description = "API endpoints for genre management")
public class GenreController {

    private final GenreService genreService;

    @Operation(
        summary = "Create a new genre",
        description = "Adds a new genre to the system"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Genre created successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = GenreDTO.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<GenreDTO> createGenre(
            @Parameter(description = "Genre details", required = true)
            @Valid @RequestBody GenreDTO dto) {
        return genreService.create(dto);
    }

    @Operation(
        summary = "Delete a genre",
        description = "Removes a genre from the system"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Genre deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Genre not found")
    })
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> removeGenre(
            @Parameter(description = "Genre identifier", required = true)
            @PathVariable UUID uuid) {
        genreService.deleteById(uuid);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Get all genres",
        description = "Returns a list of all available genres"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Genres retrieved successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = GenreDTO.class))
        )
    })
    @GetMapping
    public ResponseEntity<List<GenreDTO>> getAllGenres() {
        return genreService.getAll();
    }
}