package com.cine.sk.cinesk.domain.movie;

import com.cine.sk.cinesk.domain.movie.response.FilteredFilmsResponse;
import com.cine.sk.cinesk.domain.movie.response.PaginatedFilmsResponse;
import com.cine.sk.cinesk.domain.movie.response.SearchFilmsResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final CategoryService categoryService; // Add CategoryService

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
                    if (genreDTO.getName() != null) {
                        GenreEntity genre = genreRepository.findByName(genreDTO.getName())
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
                    if (genreDTO.getName() != null) {
                        GenreEntity genre = genreRepository.findByName(genreDTO.getName())
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

    public ResponseEntity<?> getFeaturedFilms() {
        List<MovieEntity> featuredMovies = movieRepository.findByFeaturedTrue();
        List<FeaturedFilmDTO> films = featuredMovies.stream().map(movie -> {
            String genreName = movie.getGenres().stream().findFirst().map(GenreEntity::getName).orElse("");
            return new FeaturedFilmDTO(
                movie.getUuid().toString(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getPosterUrl(),
                movie.getReleaseYear(),
                genreName,
                movie.getDurationInMinutes(),
                movie.getPrice(),
                movie.isPremium()
            );
        }).toList();
        return ResponseEntity.ok(java.util.Collections.singletonMap("films", films));
    }

    public ResponseEntity<?> getAllCategories() {
        return ResponseEntity.ok(java.util.Collections.singletonMap("categories", categoryService.getAllCategoryDetails()));
    }

    public ResponseEntity<com.cine.sk.cinesk.domain.movie.response.PaginatedFilmsResponse> getPaginatedFilms(int page, int limit, String sort, String order) {
        Sort.Direction direction = order.equalsIgnoreCase("desc") ? org.springframework.data.domain.Sort.Direction.DESC : org.springframework.data.domain.Sort.Direction.ASC;
        Pageable pageable = org.springframework.data.domain.PageRequest.of(page, limit, org.springframework.data.domain.Sort.by(direction, sort));

        Page<List<MovieEntity>> filmPage = movieRepository.findAllActive(pageable);

        // Extrair todos os filmes da página atual que contém listas
        List<MovieEntity> movies = filmPage.getContent().stream()
            .flatMap(List::stream)
            .toList();

        List<PaginatedFilmDTO> films = movies.stream().map(movie -> {
            String genreName = movie.getGenres().stream().findFirst().map(GenreEntity::getName).orElse("");
            return new PaginatedFilmDTO(
                movie.getUuid().toString(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getPosterUrl(),
                movie.getReleaseYear(),
                genreName,
                movie.getDurationInMinutes(),
                movie.getPrice(),
                movie.isPremium()
            );
        }).toList();

        PaginatedFilmsResponse.PaginationMetadata pagination =
            new PaginatedFilmsResponse.PaginationMetadata(
                filmPage.getNumber(),
                filmPage.getTotalPages(),
                filmPage.getTotalElements(),
                filmPage.getSize()
            );

        PaginatedFilmsResponse response =
            new PaginatedFilmsResponse(films, pagination);

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<SearchFilmsResponse> searchFilms(String q, int limit) {
        Pageable pageable = org.springframework.data.domain.PageRequest.of(0, limit);
        List<MovieEntity> results = movieRepository.searchByTitleOrDirector(q, pageable);

        // Calculate relevance score for each movie and create DTOs
        final String qLower = q.toLowerCase();
        List<SearchFilmDTO> films = results.stream().map(movie -> {
            // Simple relevance scoring algorithm
            int matchScore = 0;
            if (movie.getTitle().toLowerCase().contains(qLower)) matchScore += 2;
            if (movie.getDirector().toLowerCase().contains(qLower)) matchScore += 1;

            return new SearchFilmDTO(
                movie.getUuid().toString(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getPosterUrl(),
                matchScore
            );
        })
        // Sort by match score in descending order
        .sorted((film1, film2) -> Integer.compare(film2.getMatchScore(), film1.getMatchScore()))
        .toList();

        SearchFilmsResponse response =
            new SearchFilmsResponse(films, results.size());

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<FilteredFilmsResponse> filterFilms(String genre, Integer yearMin, Integer yearMax, String category) {
        List<MovieEntity> results = movieRepository.filterFilms(genre, yearMin, yearMax, category);

        List<FilteredFilmDTO> films = results.stream().map(movie -> {
            String genreName = movie.getGenres().stream().findFirst().map(GenreEntity::getName).orElse("");
            return new FilteredFilmDTO(
                movie.getUuid().toString(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getPosterUrl(),
                movie.getReleaseYear(),
                genreName,
                movie.getDurationInMinutes(),
                movie.getPrice(),
                movie.isPremium()
            );
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
    private MovieEntity findActiveMovieById(UUID uuid) {
        return movieRepository.findActiveById(uuid)
                .orElseThrow(() -> new NoSuchElementException("Movie not found"));
    }
}
