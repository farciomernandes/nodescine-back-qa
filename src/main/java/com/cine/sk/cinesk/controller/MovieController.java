package com.cine.sk.cinesk.controller;

import com.cine.sk.cinesk.domain.movie.MoviesDTO;
import com.cine.sk.cinesk.domain.movie.MoviesService;
import com.cine.sk.cinesk.domain.movie.MovieDetailDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    private final MoviesService moviesService;

    @Operation(
        summary = "Create a new movie",
        description = "Adds a new movie to the catalog"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Movie created successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = MoviesDTO.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<MoviesDTO> create(
            @Parameter(description = "Movie details", required = true)
            @Valid @RequestBody MoviesDTO dto) {
        return moviesService.create(dto);
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
            schema = @Schema(implementation = MoviesDTO.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Movie not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MoviesDTO> update(
            @Parameter(description = "Movie identifier", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Updated movie details", required = true)
            @Valid @RequestBody MoviesDTO dto) {
        return moviesService.update(id, dto);
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
        return moviesService.delete(id);
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
        return moviesService.findById(id);
    }

//    @GetMapping
//    public ResponseEntity<Page<List<MoviesDTO>>> findAll(
//            @RequestParam(required = false) String search,
//            @RequestParam(required = false) List<String> genres,
//            @RequestParam(required = false) Boolean isPremium,
//            Pageable pageable) {
//        return moviesService.findAll(search, genres, isPremium, pageable);
//    }

    @Operation(
        summary = "Get featured films",
        description = "Returns a curated list of featured films"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Featured films retrieved successfully"
        )
    })
    @GetMapping("/featured")
    public ResponseEntity<?> getFeaturedFilms() {
        return moviesService.getFeaturedFilms();
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
            schema = @Schema(implementation = MoviesDTO.class))
        )
    })
    @GetMapping("/new-releases")
    public ResponseEntity<List<MoviesDTO>> findNewReleases() {
        return moviesService.findNewReleases();
    }

    @Operation(
        summary = "Get popular movies",
        description = "Returns a list of popular movies based on view count or rating"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Popular movies retrieved successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = MoviesDTO.class))
        )
    })
    @GetMapping("/popular")
    public ResponseEntity<List<MoviesDTO>> findPopular() {
        return moviesService.findPopular();
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
        return moviesService.getAllCategories();
    }

    @Operation(
        summary = "Get paginated film list",
        description = "Returns a paginated list of films with sorting options"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Films retrieved successfully"
        )
    })
    @GetMapping
    public ResponseEntity<?> getPaginatedFilms(
            @Parameter(description = "Page number (zero-based)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page")
            @RequestParam(defaultValue = "10") int limit,
            @Parameter(description = "Field to sort by")
            @RequestParam(defaultValue = "title") String sort,
            @Parameter(description = "Sort direction (asc/desc)")
            @RequestParam(defaultValue = "asc") String order) {
        return moviesService.getPaginatedFilms(page, limit, sort, order);
    }

    @Operation(
        summary = "Search films",
        description = "Search films by title or director with relevance scoring"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Search results retrieved successfully"
        )
    })
    @GetMapping("/search")
    public ResponseEntity<?> searchFilms(
            @Parameter(description = "Search query", required = true)
            @RequestParam String q,
            @Parameter(description = "Maximum number of results")
            @RequestParam(defaultValue = "10") int limit) {
        return moviesService.searchFilms(q, limit);
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
        return moviesService.filterFilms(genre, year_min, year_max, category);
    }
}
