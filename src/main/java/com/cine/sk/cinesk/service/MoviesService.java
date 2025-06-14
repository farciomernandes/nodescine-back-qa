package com.cine.sk.cinesk.service;

import com.cine.sk.cinesk.dto.MoviesDTO;
import com.cine.sk.cinesk.entity.CategoryEntity;
import com.cine.sk.cinesk.entity.GenreEntity;
import com.cine.sk.cinesk.entity.MovieEntity;
import com.cine.sk.cinesk.repository.CategoryRepository;
import com.cine.sk.cinesk.repository.GenreRepository;
import com.cine.sk.cinesk.repository.MovieRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MoviesService {

    private final MovieRepository movieRepository;
    private final CategoryRepository categoryRepository;
    private final GenreRepository genreRepository;
    private final ObjectMapper objectMapper;

    public ResponseEntity<MoviesDTO> create(MoviesDTO dto) {
        try {
            MovieEntity movie = objectMapper.convertValue(dto, MovieEntity.class);

            // Set category
            if (dto.getCategory() != null && dto.getCategory().getUuid() != null) {
                CategoryEntity category = categoryRepository.findById(dto.getCategory().getUuid())
                    .orElseThrow(() -> new NoSuchElementException("Category not found"));
                movie.setCategory(category);
            }

            // Set genres
            if (dto.getGenres() != null && !dto.getGenres().isEmpty()) {
                dto.getGenres().forEach(genreDTO -> {
                    if (genreDTO.getUuid() != null) {
                        GenreEntity genre = genreRepository.findById(genreDTO.getUuid())
                            .orElseThrow(() -> new NoSuchElementException("Genre not found"));
                        movie.getGenres().add(genre);
                    }
                });
            }

            MovieEntity saved = movieRepository.save(movie);
            MoviesDTO savedDto = objectMapper.convertValue(saved, MoviesDTO.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDto);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public ResponseEntity<MoviesDTO> update(UUID uuid, MoviesDTO dto) {
        try {
            MovieEntity movie = findActiveMovieById(uuid);

            // Update basic properties
            movie.setTitle(dto.getTitle());
            movie.setDirector(dto.getDirector());
            movie.setReleaseYear(dto.getReleaseYear());
            movie.setDurationInMinutes(dto.getDurationInMinutes());
            movie.setDescription(dto.getDescription());
            movie.setPosterUrl(dto.getPosterUrl());
            movie.setPremium(dto.isPremium());
            movie.setFeatured(dto.isFeatured());

            // Update category
            if (dto.getCategory() != null && dto.getCategory().getUuid() != null) {
                CategoryEntity category = categoryRepository.findById(dto.getCategory().getUuid())
                    .orElseThrow(() -> new NoSuchElementException("Category not found"));
                movie.setCategory(category);
            }

            // Update genres
            if (dto.getGenres() != null) {
                movie.getGenres().clear();
                dto.getGenres().forEach(genreDTO -> {
                    if (genreDTO.getUuid() != null) {
                        GenreEntity genre = genreRepository.findById(genreDTO.getUuid())
                            .orElseThrow(() -> new NoSuchElementException("Genre not found"));
                        movie.getGenres().add(genre);
                    }
                });
            }

            MovieEntity updated = movieRepository.save(movie);
            return ResponseEntity.ok(objectMapper.convertValue(updated, MoviesDTO.class));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public ResponseEntity<Void> delete(UUID uuid) {
        try {
            movieRepository.deleteById(LocalDateTime.now(), uuid);
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

    public ResponseEntity<Page<List<MoviesDTO>>> findAll(String search, List<String> genres, Boolean isPremium, Pageable pageable) {
        Page<List<MovieEntity>> movies = movieRepository.findAllActive(pageable);

        // Apply filters
        Page<List<MoviesDTO>> dtoPage = movies.map(movieList -> 
            movieList.stream()
                // Filter by search term (title or description)
                .filter(movie -> search == null || 
                    movie.getTitle().toLowerCase().contains(search.toLowerCase()) || 
                    (movie.getDescription() != null && movie.getDescription().toLowerCase().contains(search.toLowerCase())))
                // Filter by genres
                .filter(movie -> genres == null || genres.isEmpty() || 
                    movie.getGenres().stream()
                        .anyMatch(genre -> genres.contains(genre.getName())))
                // Filter by premium status
                .filter(movie -> isPremium == null || movie.isPremium() == isPremium)
                // Convert to DTO
                .map(movie -> objectMapper.convertValue(movie, MoviesDTO.class))
                .collect(Collectors.toList())
        );

        return ResponseEntity.ok(dtoPage);
    }

    public ResponseEntity<List<MoviesDTO>> findFeatured() {
        try {
            // This is a placeholder implementation. In a real application, you would query
            // the database for movies where isFeatured is true.
            List<MovieEntity> featuredMovies = new ArrayList<>(); // Replace with actual query
            List<MoviesDTO> dtos = featuredMovies.stream()
                .map(movie -> objectMapper.convertValue(movie, MoviesDTO.class))
                .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<List<MoviesDTO>> findNewReleases() {
        try {
            // This is a placeholder implementation. In a real application, you would query
            // the database for recently added movies, sorted by createdAt.
            List<MovieEntity> newReleases = new ArrayList<>(); // Replace with actual query
            List<MoviesDTO> dtos = newReleases.stream()
                .map(movie -> objectMapper.convertValue(movie, MoviesDTO.class))
                .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<List<MoviesDTO>> findPopular() {
        try {
            // This is a placeholder implementation. In a real application, you would query
            // the database for popular movies based on some criteria.
            List<MovieEntity> popularMovies = new ArrayList<>(); // Replace with actual query
            List<MoviesDTO> dtos = popularMovies.stream()
                .map(movie -> objectMapper.convertValue(movie, MoviesDTO.class))
                .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private MovieEntity findActiveMovieById(UUID uuid) {
        return movieRepository.findActiveById(uuid)
                .orElseThrow(() -> new NoSuchElementException("Movie not found"));
    }
}
