package com.cine.sk.cinesk.controller;


import com.cine.sk.cinesk.dto.CategoryDTO;
import com.cine.sk.cinesk.dto.MovieCategoriesDTO;
import com.cine.sk.cinesk.dto.MoviesDTO;
import com.cine.sk.cinesk.service.MoviesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movies")
public class MovieController {

    private final MoviesService moviesService;

    @PostMapping
    public ResponseEntity<MoviesDTO> create(@Valid @RequestBody MoviesDTO dto) {
        MoviesDTO created = moviesService.create(dto);
        return ResponseEntity.status(201).body(created);
    }

    @PostMapping("/category")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO dto) {
        CategoryDTO created = moviesService.createCategory(dto);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<MoviesDTO> update(@PathVariable UUID uuid, @Valid @RequestBody MoviesDTO dto) {
        MoviesDTO updated = moviesService.update(uuid, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        moviesService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MoviesDTO> findById(@PathVariable UUID id) {
        MoviesDTO dto = moviesService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<MoviesDTO>> findAll() {
        List<MoviesDTO> dtos = moviesService.findAll();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<List<MoviesDTO>> findBySlug(@PathVariable String slug) {
        List<MoviesDTO> dtos = moviesService.findBySlug(slug);
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/{id}/categories")
    public ResponseEntity<Void> addCategory(@PathVariable UUID id, @Valid @RequestBody MovieCategoriesDTO dto) {
        if (!id.equals(dto.getMovieUuid())) {
            throw new IllegalArgumentException("Movie ID in path and DTO must match");
        }
        moviesService.addCategoryToMovie(dto);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/{id}/categories/{categoryId}")
    public ResponseEntity<Void> removeCategory(@PathVariable UUID id, @PathVariable UUID categoryId) {
        moviesService.removeCategoryFromMovie(id, categoryId);
        return ResponseEntity.noContent().build();
    }
}
