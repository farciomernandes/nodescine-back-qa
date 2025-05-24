package com.cine.sk.cinesk.service;


import com.cine.sk.cinesk.dto.CategoryDTO;
import com.cine.sk.cinesk.dto.MoviesDTO;
import com.cine.sk.cinesk.entity.CategoryEntity;
import com.cine.sk.cinesk.entity.MovieEntity;
import com.cine.sk.cinesk.repository.CategoryRepository;
import com.cine.sk.cinesk.repository.MovieRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MoviesService {

    private static final String EMPTY_JSON_ARRAY = "[]";
    private final MovieRepository movieRepository;
    private final CategoryService categoryService;
    private final ObjectMapper objectMapper;

    public ResponseEntity<MovieEntity> create(MoviesDTO dto) {
        try {
            categoryService.validateCategoryExists(dto.getCategoryUuid());
            var actors = serializeActors(dto.getActors());
            dto.setActors(null);
            MovieEntity movie = objectMapper.convertValue(dto, MovieEntity.class);
            movie.setVideoUrl(new String(Base64.encode(dto.getVideoUrl().getBytes())));
            movie.setThumbnailUrl(new String(Base64.encode(dto.getThumbnailUrl().getBytes())));
            movie.setTrailerUrl(new String(Base64.encode(dto.getTrailerUrl().getBytes())));
            movie.setActors(actors);
            MovieEntity saved = movieRepository.save(movie);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    private String serializeActors(List<String> actors) throws JsonProcessingException {
        if (actors == null || actors.isEmpty()) {
            return EMPTY_JSON_ARRAY;
        }
        return objectMapper.writeValueAsString(actors); // Convert List<String> to JSON string
    }

    private List<String> deserializeActors(String actorsJson) throws JsonProcessingException {
        if (actorsJson == null || actorsJson.equals(EMPTY_JSON_ARRAY)) {
            return List.of();
        }
        return objectMapper.readValue(actorsJson, new TypeReference<List<String>>() {});
    }

    public ResponseEntity<MoviesDTO> update(UUID uuid, MoviesDTO dto) {
        try {
            MovieEntity movie = findActiveMovieById(uuid);
            MovieEntity updated = objectMapper.updateValue(movie, dto);
            updated.setVideoUrl(new String(Base64.encode(dto.getVideoUrl().getBytes())));
            updated.setThumbnailUrl(new String(Base64.encode(dto.getThumbnailUrl().getBytes())));
            updated.setTrailerUrl(new String(Base64.encode(dto.getTrailerUrl().getBytes())));
            updated.setActors(serializeActors(dto.getActors()));
            return ResponseEntity.ok(objectMapper.convertValue(movieRepository.save(updated), MoviesDTO.class));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    public ResponseEntity<Void> delete(UUID uuid) {
        try {
            movieRepository.deleteById(LocalDateTime.now() ,uuid);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    public ResponseEntity<MoviesDTO> findById(UUID uuid) {
        try {
            MovieEntity movie = findActiveMovieById(uuid);
            MoviesDTO dto = objectMapper.convertValue(movie, MoviesDTO.class);
            return ResponseEntity.ok(dto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    public ResponseEntity<List<MovieEntity>> findAll() {
        List<MovieEntity> movies = movieRepository.findAllActive();
        return ResponseEntity.ok(movies);
    }

    public ResponseEntity<List<MoviesDTO>> findBySlug(String slug) {
        try {
            List<MoviesDTO> movies = movieRepository.findBySlug(slug)
                    .filter(movie -> movie.getDeletedAt() == null)
                    .map(movie -> {
                        MoviesDTO dto = objectMapper.convertValue(movie, MoviesDTO.class);
                        dto.setVideoUrl(Base64.decode(movie.getVideoUrl()).toString());
                        return dto;
                    })
                    .map(List::of)
                    .orElseThrow(() -> new NoSuchElementException("Movie not found"));
            return ResponseEntity.ok(movies);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    private MovieEntity findActiveMovieById(UUID uuid) {
        return movieRepository.findActiveById(uuid)
                .orElseThrow(() -> new NoSuchElementException("Movie not found"));
    }
}
