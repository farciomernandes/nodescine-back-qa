package com.cine.sk.cinesk.controller;

import com.cine.sk.cinesk.domain.movie.enhanced.EnhancedFilmDTO;
import com.cine.sk.cinesk.domain.movie.enhanced.EnhancedFilmService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/enhanced-films")
public class EnhancedFilmController {

    private final EnhancedFilmService enhancedFilmService;

    @GetMapping("/{id}")
    public ResponseEntity<EnhancedFilmDTO> findById(
            @Parameter(description = "Movie identifier", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(enhancedFilmService.findById(id));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<EnhancedFilmDTO> findBySlug(
        @Parameter(description = "Movie Slug", required = true)
        @PathVariable String slug) {
        return ResponseEntity.ok(enhancedFilmService.findBySlug(slug));
    }

    @GetMapping("/me")
    public ResponseEntity<List<EnhancedFilmDTO>> findMyMovies() {
        return ResponseEntity.ok(enhancedFilmService.findMyMovies());
    }


    @GetMapping
    public ResponseEntity<Page<EnhancedFilmDTO>> getAllFilms(Pageable pageable, @RequestParam(required = false) String searchTerm) {
        return ResponseEntity.ok(enhancedFilmService.findAll(searchTerm, pageable));
    }

    @GetMapping("/filter")
    public Page<EnhancedFilmDTO> getAllMovies(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String director,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String cast,
            @PageableDefault(size = 10, sort = "title") Pageable pageable
    ) {
        return enhancedFilmService.findAll(title, description, director, genre, category, cast, pageable);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EnhancedFilmDTO> create(
            @RequestPart(value = "dto", required = true) EnhancedFilmDTO dto,
            @RequestPart(value = "poster", required = false) MultipartFile poster,
            @RequestPart(value = "fileBanner", required = false) MultipartFile banner) {

        var created = enhancedFilmService.create(dto);

        EnhancedFilmDTO result = null;
        if (poster != null && !poster.isEmpty()) {
            result = enhancedFilmService.insertPoster(created.getId(), poster);
        }

        if (banner != null && !banner.isEmpty()) {
            result = enhancedFilmService.insertBanner(created.getId(), banner);
        }

        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnhancedFilmDTO> update(@PathVariable Long id, @Valid @RequestPart("dto") EnhancedFilmDTO dto,
                                                  @RequestPart(value = "poster", required = false) MultipartFile poster,
                                                  @RequestPart(value = "banner", required = false) MultipartFile banner) {
        var movie = enhancedFilmService.update(id, dto);
        EnhancedFilmDTO result = null;
        if (poster != null && !poster.isEmpty()) {
            result = enhancedFilmService.insertPoster(movie.getId(), poster);
        }

        if (banner != null && !banner.isEmpty()) {
            result = enhancedFilmService.insertBanner(movie.getId(), banner);
        }

        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/poster", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> insertPoster(
            @RequestParam("id") Long id,
            @RequestParam("file") MultipartFile file) {
       enhancedFilmService.insertPoster(id, file);
       return ResponseEntity.ok("Poster inserted successfully");
    }

    @PostMapping(value = "/banner", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> insertPBanner(
        @RequestParam("id") Long id,
        @RequestParam("file") MultipartFile file) {
        enhancedFilmService.insertBanner(id, file);
        return ResponseEntity.ok("Banner inserted successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        enhancedFilmService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
