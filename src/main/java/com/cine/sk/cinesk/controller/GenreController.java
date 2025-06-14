package com.cine.sk.cinesk.controller;

import com.cine.sk.cinesk.dto.GenreDTO;
import com.cine.sk.cinesk.service.GenreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/genres")
public class GenreController {

    private final GenreService genreService;

    @PostMapping
    public ResponseEntity<GenreDTO> createGenre(@Valid @RequestBody GenreDTO dto) {
        return genreService.create(dto);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> removeGenre(@PathVariable UUID uuid) {
        genreService.deleteById(uuid);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<GenreDTO>> getAllGenres() {
        return genreService.getAll();
    }
}