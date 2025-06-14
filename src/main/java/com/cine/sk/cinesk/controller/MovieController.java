package com.cine.sk.cinesk.controller;

import com.cine.sk.cinesk.dto.MoviesDTO;
import com.cine.sk.cinesk.entity.MovieEntity;
import com.cine.sk.cinesk.service.MoviesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/films")
public class MovieController {

    private final MoviesService moviesService;

    @PostMapping
    public ResponseEntity<MoviesDTO> create(@Valid @RequestBody MoviesDTO dto) {
        return moviesService.create(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MoviesDTO> update(@PathVariable UUID id, @Valid @RequestBody MoviesDTO dto) {
        return moviesService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        return moviesService.delete(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MoviesDTO> findById(@PathVariable UUID id) {
        return moviesService.findById(id);
    }

    @GetMapping
    public ResponseEntity<Page<List<MoviesDTO>>> findAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) List<String> genres,
            @RequestParam(required = false) Boolean isPremium,
            Pageable pageable) {
        return moviesService.findAll(search, genres, isPremium, pageable);
    }

    @GetMapping("/featured")
    public ResponseEntity<List<MoviesDTO>> findFeatured() {
        return moviesService.findFeatured();
    }

    @GetMapping("/new-releases")
    public ResponseEntity<List<MoviesDTO>> findNewReleases() {
        return moviesService.findNewReleases();
    }

    @GetMapping("/popular")
    public ResponseEntity<List<MoviesDTO>> findPopular() {
        return moviesService.findPopular();
    }
}
