package com.cine.sk.cinesk.controller;

import com.cine.sk.cinesk.domain.movie.dto.MovieDTO;
import com.cine.sk.cinesk.domain.movie.MovieService;
import com.cine.sk.cinesk.domain.movie.dto.MovieDetailDTO;
import com.cine.sk.cinesk.domain.movie.response.PaginatedFilmsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/films")
@Tag(name = "Movies", description = "API endpoints for movie catalog management")
public class MovieController {

    private final MovieService movieService;

    @Operation(
        summary = "Create a new movie",
        description = "Adds a new movie to the catalog"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Movie created successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = MovieDTO.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<MovieDTO> create(
            @Parameter(description = "Movie details", required = true)
            @Valid @RequestBody MovieDTO dto) {
        return movieService.create(dto);
    }

    @Operation(
        summary = "Update a movie",
        description = "Updates an existing movie in the catalog"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Movie updated successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = MovieDTO.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Movie not found")
    })
    @PutMapping("/{uuid}")
    public ResponseEntity<MovieDTO> update(
            @Parameter(description = "Movie identifier", required = true)
            @PathVariable UUID uuid,
            @Parameter(description = "Updated movie details", required = true)
            @Valid @RequestBody MovieDTO dto) {
        return movieService.update(uuid, dto);
    }

    @Operation(
        summary = "Delete a movie",
        description = "Deletes a movie from the catalog"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Movie deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Movie not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Movie identifier", required = true)
            @PathVariable UUID id) {
        return movieService.delete(id);
    }

    @Operation(
        summary = "Get movie details",
        description = "Returns comprehensive details for a specific movie"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Movie details retrieved successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = MovieDetailDTO.class))
        ),
        @ApiResponse(responseCode = "404", description = "Movie not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MovieDetailDTO> findById(
            @Parameter(description = "Movie identifier", required = true)
            @PathVariable UUID id) {
        return movieService.findById(id);
    }

    @GetMapping
    public ResponseEntity<PaginatedFilmsResponse> findAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) List<String> genres,
            @RequestParam(required = false) Boolean isPremium,
            Pageable pageable) {
        return movieService.findAll(search, genres, isPremium, pageable);
    }

    @Operation(
        summary = "Get new releases",
        description = "Returns recently added movies in the catalog"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "New releases retrieved successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = MovieDTO.class))
        )
    })
    @GetMapping("/new-releases")
    public ResponseEntity<List<MovieDTO>> findNewReleases() {
        return movieService.findNewReleases();
    }

    @Operation(
        summary = "Get all categories",
        description = "Returns all available movie categories"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Categories retrieved successfully"
        )
    })
    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories() {
        return movieService.getAllCategories();
    }

    @Operation(
        summary = "Filter films",
        description = "Filter films by various criteria such as genre, year range, and category"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Filtered results retrieved successfully"
        )
    })
    @GetMapping("/filter")
    public ResponseEntity<?> filterFilms(
            @Parameter(description = "Filter by genre")
            @RequestParam(required = false) String genre,
            @Parameter(description = "Minimum year")
            @RequestParam(required = false) Integer year_min,
            @Parameter(description = "Maximum year")
            @RequestParam(required = false) Integer year_max,
            @Parameter(description = "Filter by category")
            @RequestParam(required = false) String category) {
        return movieService.filter(genre, year_min, year_max, category);
    }
}
