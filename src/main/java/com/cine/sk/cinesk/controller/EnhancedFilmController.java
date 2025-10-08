package com.cine.sk.cinesk.controller;

import com.cine.sk.cinesk.domain.movie.enhanced.EnhancedFilmDTO;
import com.cine.sk.cinesk.domain.movie.enhanced.EnhancedFilmService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @GetMapping("/me")
    public ResponseEntity<List<EnhancedFilmDTO>> findMyMovies() {
        return ResponseEntity.ok(enhancedFilmService.findMyMovies());
    }


    @GetMapping
    public ResponseEntity<Page<EnhancedFilmDTO>> getAllFilms(Pageable pageable, @RequestParam(required = false) String searchTerm) {
        return ResponseEntity.ok(enhancedFilmService.findAll(searchTerm, pageable));
    }

    @Transactional
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EnhancedFilmDTO> create(
            @Valid @RequestPart("dto") EnhancedFilmDTO dto,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        var created = enhancedFilmService.create(dto);
        if (file != null && !file.isEmpty()) {
            var movieWithPoster = enhancedFilmService.insertPoster(created.getId(), file);
            return ResponseEntity.ok(movieWithPoster);
        }

        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnhancedFilmDTO> update(@PathVariable Long id, @Valid @RequestBody EnhancedFilmDTO dto) {
        return ResponseEntity.ok(enhancedFilmService.update(id, dto));
    }

    @PostMapping(value = "/poster", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> insertPoster(
            @RequestParam("id") Long id,
            @RequestParam("file") MultipartFile file) {
       enhancedFilmService.insertPoster(id, file);
       return ResponseEntity.ok("Poster inserted successfully");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        enhancedFilmService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
