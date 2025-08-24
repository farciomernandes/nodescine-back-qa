package com.cine.sk.cinesk.domain.movie;

import com.cine.sk.cinesk.domain.movie.category.CategoryDTO;
import com.cine.sk.cinesk.domain.movie.category.CategoryEntity;
import com.cine.sk.cinesk.domain.movie.category.CategoryRepository;
import com.cine.sk.cinesk.domain.movie.category.CategoryService;
import com.cine.sk.cinesk.domain.movie.dto.FilteredFilmDTO;
import com.cine.sk.cinesk.domain.movie.dto.MovieDTO;
import com.cine.sk.cinesk.domain.movie.dto.MovieDetailDTO;
import com.cine.sk.cinesk.domain.movie.dto.PaginatedFilmDTO;
import com.cine.sk.cinesk.domain.movie.genre.GenreDTO;
import com.cine.sk.cinesk.domain.movie.genre.GenreEntity;
import com.cine.sk.cinesk.domain.movie.genre.GenreRepository;
import com.cine.sk.cinesk.domain.movie.response.FilteredFilmsResponse;
import com.cine.sk.cinesk.domain.movie.response.PaginatedFilmsResponse;
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
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final CategoryRepository categoryRepository;
    private final GenreRepository genreRepository;
    private final ObjectMapper objectMapper;
    private final CategoryService categoryService;

    public ResponseEntity<MovieDTO> create(MovieDTO dto) {
        try {
            MovieEntity movie = objectMapper.convertValue(dto, MovieEntity.class);

            if (dto.getCategory() != null && dto.getCategory().getUuid() != null) {
                CategoryEntity category = categoryRepository.findById(dto.getCategory().getUuid())
                    .orElseThrow(() -> new NoSuchElementException("Category not found"));
                movie.setCategory(category);
            }

            if (dto.getGenres() != null && !dto.getGenres().isEmpty()) {
                dto.getGenres().forEach(genreDTO -> {
                    if (genreDTO.getName() != null) {
                        GenreEntity genre = genreRepository.findByName(genreDTO.getName())
                            .orElseThrow(() -> new NoSuchElementException("Genre not found"));
                        movie.getGenres().add(genre);
                    }
                });
            }

            MovieEntity saved = movieRepository.save(movie);
            MovieDTO savedDto = mapToMovieDTO(saved);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDto);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public ResponseEntity<MovieDTO> update(UUID uuid, MovieDTO dto) {
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

            if (dto.getCategory() != null && dto.getCategory().getUuid() != null) {
                CategoryEntity category = categoryRepository.findById(dto.getCategory().getUuid())
                    .orElseThrow(() -> new NoSuchElementException("Category not found"));
                movie.setCategory(category);
            }

            if (dto.getGenres() != null) {
                movie.getGenres().clear();
                dto.getGenres().forEach(genreDTO -> {
                    if (genreDTO.getName() != null) {
                        GenreEntity genre = genreRepository.findByName(genreDTO.getName())
                            .orElseThrow(() -> new NoSuchElementException("Genre not found"));
                        movie.getGenres().add(genre);
                    }
                });
            }

            MovieEntity updated = movieRepository.save(movie);
            return ResponseEntity.ok(mapToMovieDTO(updated));
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

    public ResponseEntity<MovieDetailDTO> findById(UUID uuid) {
        try {
            MovieEntity movie = findActiveMovieById(uuid);
            MovieDetailDTO detailDTO = mapToMovieDetailDTO(movie);
            return ResponseEntity.ok(detailDTO);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    private MovieDetailDTO mapToMovieDetailDTO(MovieEntity movie) {
        String genreName = movie.getGenres().stream()
                .findFirst()
                .map(GenreEntity::getName)
                .orElse("");
                
        // Calculate price as double (assuming price is stored as String in the entity)
        double priceValue = 0.0;
        if (movie.getPrice() != null && !movie.getPrice().isEmpty()) {
            try {
                priceValue = Double.parseDouble(movie.getPrice());
            } catch (NumberFormatException e) {
                // Handle parsing error if needed
            }
        }
        
        return new MovieDetailDTO(
            movie.getUuid().toString(),
            movie.getTitle(),
            movie.getDirector(),
            movie.getDescription(), // Synopsis is stored in description field
            movie.getPosterUrl(),
            movie.getTrailerUrl(),
            movie.getVideoUrl(),
            movie.getReleaseYear(),
            genreName,
            movie.getDurationInMinutes(),
            priceValue,
            movie.isPremium(),
            movie.getCast(),
            movie.getRating(),
            movie.getViewCount()
        );
    }

    public ResponseEntity<PaginatedFilmsResponse> findAll(String search, List<String> genres, Boolean isPremium, Pageable pageable) {
        try {
            String searchParam = (search != null && !search.trim().isEmpty()) ? search.trim() : null;
            String genresParam = (genres != null && !genres.isEmpty()) ? String.join(",", genres) : null;
            List<String> genreList = (genres != null && !genres.isEmpty()) ? genres : List.of();

            Page<MovieEntity> movies = movieRepository.findAllActiveWithFilters(
                searchParam, 
                isPremium, 
                genresParam,
                genreList,
                pageable
            );

            List<PaginatedFilmDTO> films = movies.getContent().stream()
                .map(movie -> {
                    String genreName = movie.getGenres().stream()
                        .findFirst()
                        .map(GenreEntity::getName)
                        .orElse("");

                    PaginatedFilmDTO dto = new PaginatedFilmDTO();
                    dto.setUuid(movie.getUuid());
                    dto.setTitle(movie.getTitle());
                    dto.setDirector(movie.getDirector());
                    dto.setPosterUrl(movie.getPosterUrl());
                    dto.setReleaseYear(movie.getReleaseYear());
                    dto.setGenre(genreName);
                    dto.setDurationInMinutes(movie.getDurationInMinutes());
                    dto.setPrice(movie.getPrice());
                    dto.setPremium(movie.isPremium());
                    return dto;
                })
                .collect(Collectors.toList());

            PaginatedFilmsResponse.PaginationMetadata pagination = 
                new PaginatedFilmsResponse.PaginationMetadata(
                    movies.getNumber() + 1,
                    movies.getTotalPages(),
                    movies.getTotalElements(),
                    movies.getSize()
                );

            PaginatedFilmsResponse response = new PaginatedFilmsResponse(films, pagination);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Erro ao buscar filmes: " + e.getMessage());
        }
    }

    public ResponseEntity<List<MovieDTO>> findNewReleases() {
        try {
            List<MovieEntity> newReleases = movieRepository.findNewReleases();
            List<MovieDTO> dtos = newReleases.stream()
                .map(this::mapToMovieDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<?> getAllCategories() {
        return ResponseEntity.ok(java.util.Collections.singletonMap("categories", categoryService.getAllCategoryDetails()));
    }

    public ResponseEntity<FilteredFilmsResponse> filter(String genre, Integer yearMin, Integer yearMax, String category) {
        List<MovieEntity> results = movieRepository.filter(genre, yearMin, yearMax, category);

        List<FilteredFilmDTO> films = results.stream().map(movie -> {
            String genreName = movie.getGenres().stream().findFirst().map(GenreEntity::getName).orElse("");

            FilteredFilmDTO dto = new FilteredFilmDTO();
            dto.setUuid(movie.getUuid());
            dto.setTitle(movie.getTitle());
            dto.setDirector(movie.getDirector());
            dto.setPosterUrl(movie.getPosterUrl());
            dto.setReleaseYear(movie.getReleaseYear());
            dto.setGenre(genreName);
            dto.setDurationInMinutes(movie.getDurationInMinutes());
            dto.setPrice(movie.getPrice());
            dto.setPremium(movie.isPremium());

            return dto;
        }).toList();

        java.util.Map<String, Object> filtersApplied = new java.util.LinkedHashMap<>();
        if (genre != null) filtersApplied.put("genre", genre);
        if (yearMin != null) filtersApplied.put("year_min", yearMin);
        if (yearMax != null) filtersApplied.put("year_max", yearMax);
        if (category != null) filtersApplied.put("category", category);

        FilteredFilmsResponse response =
            new FilteredFilmsResponse(films, filtersApplied, results.size());

        return ResponseEntity.ok(response);
    }
    private MovieDTO mapToMovieDTO(MovieEntity movie) {
        List<GenreDTO> genreDTOs = movie.getGenres().stream()
            .map(genre -> {
                GenreDTO genreDTO = new GenreDTO();
                genreDTO.setUuid(genre.getUuid());
                genreDTO.setName(genre.getName());
                return genreDTO;
            })
            .collect(Collectors.toList());

        CategoryDTO categoryDTO = null;
        if (movie.getCategory() != null) {
            categoryDTO = new CategoryDTO();
            categoryDTO.setUuid(movie.getCategory().getUuid());
            categoryDTO.setName(movie.getCategory().getName());
            categoryDTO.setSlug(movie.getCategory().getSlug());
            categoryDTO.setImageUrl(movie.getCategory().getImageUrl());
        }

        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setUuid(movie.getUuid());
        movieDTO.setTitle(movie.getTitle());
        movieDTO.setSlug(movie.getSlug());
        movieDTO.setDirector(movie.getDirector());
        movieDTO.setReleaseYear(movie.getReleaseYear());
        movieDTO.setPrice(movie.getPrice());
        movieDTO.setDurationInMinutes(movie.getDurationInMinutes());
        movieDTO.setDescription(movie.getDescription());
        movieDTO.setPosterUrl(movie.getPosterUrl());
        movieDTO.setTrailerUrl(movie.getTrailerUrl());
        movieDTO.setVideoUrl(movie.getVideoUrl());
        movieDTO.setPremium(movie.isPremium());
        movieDTO.setFeatured(movie.isFeatured());
        movieDTO.setCast(movie.getCast());
        movieDTO.setRating(movie.getRating());
        movieDTO.setViewCount(movie.getViewCount());
        movieDTO.setCategory(categoryDTO);
        movieDTO.setGenres(genreDTOs);

        return movieDTO;
    }

    private MovieEntity findActiveMovieById(UUID uuid) {
        return movieRepository.findActiveById(uuid)
                .orElseThrow(() -> new NoSuchElementException("Movie not found"));
    }
}
