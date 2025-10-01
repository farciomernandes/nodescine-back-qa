package com.cine.sk.cinesk.controller;

import com.cine.sk.cinesk.domain.movie.enhanced.EnhancedFilmDTO;
import com.cine.sk.cinesk.domain.movie.enhanced.EnhancedFilmService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @GetMapping
    public ResponseEntity<List<EnhancedFilmDTO>> getAllFilms() {
        return ResponseEntity.ok(enhancedFilmService.findAll());
    }

    @PostMapping
    public ResponseEntity<EnhancedFilmDTO> create(@Valid @RequestBody EnhancedFilmDTO dto) {
        return ResponseEntity.ok(enhancedFilmService.create(dto));
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
