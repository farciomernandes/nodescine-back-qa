package com.cine.sk.cinesk.controller;

import com.cine.sk.cinesk.dto.CategoryDTO;
import com.cine.sk.cinesk.dto.MoviesDTO;
import com.cine.sk.cinesk.entity.MovieEntity;
import com.cine.sk.cinesk.service.MoviesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movie")
public class MovieController {

    private final MoviesService moviesService;

    @PostMapping
    public ResponseEntity<MovieEntity> create(@Valid @RequestBody MoviesDTO dto) {
        return moviesService.create(dto);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<MoviesDTO> update(@PathVariable UUID uuid, @Valid @RequestBody MoviesDTO dto) {
        return moviesService.update(uuid, dto);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid) {
        return moviesService.delete(uuid);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<MoviesDTO> findById(@PathVariable UUID uuid) {
        return moviesService.findById(uuid);
    }

    @GetMapping
    public ResponseEntity<List<MovieEntity>> findAll() {
        return moviesService.findAll();
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<List<MoviesDTO>> findBySlug(@PathVariable String slug) {
        return moviesService.findBySlug(slug);
    }
}