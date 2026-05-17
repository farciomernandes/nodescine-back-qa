package com.cine.sk.cinesk.controller;

import com.cine.sk.cinesk.domain.movie.MovieService;
import com.cine.sk.cinesk.domain.movie.EnhancedMovieResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import com.cine.sk.cinesk.domain.movie.MovieFormat;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/enhanced-films")
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/{id}")
    public ResponseEntity<EnhancedMovieResponse> findById(
            @Parameter(description = "Movie identifier", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(movieService.findById(id));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<EnhancedMovieResponse> findBySlug(
        @Parameter(description = "Movie Slug", required = true)
        @PathVariable String slug) {
        return ResponseEntity.ok(movieService.findBySlug(slug));
    }

    @GetMapping("/me")
    public ResponseEntity<List<EnhancedMovieResponse>> findMyMovies() {

        return ResponseEntity.ok(movieService.findMyMovies());
    }


    @GetMapping
    public ResponseEntity<Page<EnhancedMovieResponse>> getAllFilms(Pageable pageable, @RequestParam(required = false) String searchTerm) {
        return ResponseEntity.ok(movieService.findAll(searchTerm, pageable));
    }

    @GetMapping("/formats")
    public ResponseEntity<List<MovieFormat>> getMovieFormats() {
        return ResponseEntity.ok(Arrays.asList(MovieFormat.values()));
    }

    @GetMapping("/filter")
    public Page<EnhancedMovieResponse> getAllMovies(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String director,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String cast,
            @PageableDefault(size = 10, sort = "title") Pageable pageable
    ) {
        return movieService.findAll(title, description, director, genre, category, cast, pageable);
    }

    @Operation(summary = "Create movie",
            description = "Creates a movie. Send 'dto' as a JSON part and optional files 'poster' and 'fileBanner'.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EnhancedMovieResponse> create(
            @Parameter(description = "DTO JSON part", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EnhancedMovieResponse.class)))
            @RequestPart(value = "dto", required = true) EnhancedMovieResponse dto,

            @Parameter(description = "Poster image file", content = @Content(mediaType = "image/*"))
            @RequestPart(value = "poster", required = false) MultipartFile poster,

            @Parameter(description = "Banner image file", content = @Content(mediaType = "image/*"))
            @RequestPart(value = "fileBanner", required = false) MultipartFile banner) {

        var created = movieService.create(dto);

        // Start with created DTO and overwrite with responses from file uploads (if any)
        EnhancedMovieResponse result = created;
        if (poster != null && !poster.isEmpty()) {
            result = movieService.insertPoster(created.getId(), poster);
        }

        if (banner != null && !banner.isEmpty()) {
            result = movieService.insertBanner(created.getId(), banner);
        }

        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Update movie",
            description = "Updates a movie. Send 'dto' as a JSON part and optional files 'poster' and 'banner'.")
    @PutMapping("/{id}")
    public ResponseEntity<EnhancedMovieResponse> update(@PathVariable Long id, @Valid @RequestPart("dto") EnhancedMovieResponse dto,
                                                        @RequestPart(value = "poster", required = false) MultipartFile poster,
                                                        @RequestPart(value = "banner", required = false) MultipartFile banner) {
        var movie = movieService.update(id, dto);
        EnhancedMovieResponse result = movie;
        if (poster != null && !poster.isEmpty()) {
            result = movieService.insertPoster(movie.getId(), poster);
        }

        if (banner != null && !banner.isEmpty()) {
            result = movieService.insertBanner(movie.getId(), banner);
        }

        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/poster", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> insertPoster(
            @RequestParam("id") Long id,
            @RequestParam("file") MultipartFile file) {
       movieService.insertPoster(id, file);
       return ResponseEntity.ok("Poster inserted successfully");
    }

    @PostMapping(value = "/banner", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> insertPBanner(
        @RequestParam("id") Long id,
        @RequestParam("file") MultipartFile file) {
        movieService.insertBanner(id, file);
        return ResponseEntity.ok("Banner inserted successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        movieService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/report")
    public ResponseEntity<String> reportMovie(@PathVariable Long id, @RequestBody com.cine.sk.cinesk.domain.movie.report.ReportRequest request) {
        movieService.reportMovie(id, request);
        return ResponseEntity.ok("Report received");
    }
}
