package com.cine.sk.cinesk.controller;

import com.cine.sk.cinesk.domain.movie.genre.GenreDTO;
import com.cine.sk.cinesk.domain.movie.genre.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
@Tag(name = "Genres", description = "API endpoints for genre management")
public class GenreController {

    private final GenreService genreService;

    @Operation(
        summary = "Delete a genre",
        description = "Removes a genre from the system"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Genre deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Genre not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeGenre(
            @Parameter(description = "Genre identifier", required = true)
            @PathVariable Long id) {
        genreService.deleteById(id);
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