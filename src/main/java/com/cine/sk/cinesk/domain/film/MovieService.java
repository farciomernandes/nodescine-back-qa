package com.cine.sk.cinesk.domain.film;

import com.cine.sk.cinesk.domain.film.category.CategoryDTO;
import com.cine.sk.cinesk.domain.film.category.Category;
import com.cine.sk.cinesk.domain.film.category.CategoryRepository;
import com.cine.sk.cinesk.domain.film.category.CategoryService;
import com.cine.sk.cinesk.domain.film.dto.FilteredFilmDTO;
import com.cine.sk.cinesk.domain.film.dto.MovieDTO;
import com.cine.sk.cinesk.domain.film.dto.PaginatedFilmDTO;
import com.cine.sk.cinesk.domain.film.enhanced.EnhancedFilmDTO;
import com.cine.sk.cinesk.domain.film.genre.GenreDTO;
import com.cine.sk.cinesk.domain.film.genre.Genre;
import com.cine.sk.cinesk.domain.film.genre.GenreRepository;
import com.cine.sk.cinesk.domain.film.response.FilteredFilmsResponse;
import com.cine.sk.cinesk.domain.film.response.PaginatedFilmsResponse;
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

    public List<EnhancedFilmDTO> findAll() {
        return movieRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public EnhancedFilmDTO findById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));
        return toDTO(movie);
    }

    private EnhancedFilmDTO toDTO(Movie entity) {
        EnhancedFilmDTO dto = new EnhancedFilmDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDirector(entity.getDirector());
        dto.setYear(entity.getReleaseYear());
        dto.setCategory(entity.getCategory() != null ? entity.getCategory().getName() : null);
        dto.setGenres(entity.getGenres().stream()
                .map(genre -> new GenreDTO(genre.getId(), genre.getName()))
                .collect(Collectors.toList()));
        dto.setDuration(minutesToDuration(entity.getDurationInMinutes()));
        dto.setPosterUrl(entity.getPosterUrl());
        dto.setFilmUrl(entity.getVideoUrl());
        dto.setTrailerUrl(entity.getTrailerUrl());
        dto.setRentalPrice(entity.getPrice());
        dto.setSynopsis(entity.getDescription());
        dto.setIsPremium(entity.isPremium());
        return dto;
    }

    private Movie toEntity(EnhancedFilmDTO dto) {
        Movie entity = new Movie();
        entity.setTitle(dto.getTitle());
        entity.setSlug(titleToSlug(dto.getTitle()));
        entity.setDirector(dto.getDirector());
        entity.setReleaseYear(dto.getYear());
        entity.setPrice(dto.getRentalPrice());
        entity.setDescription(dto.getSynopsis());
        entity.setPosterUrl(dto.getPosterUrl());
        entity.setVideoUrl(dto.getFilmUrl());
        entity.setTrailerUrl(dto.getTrailerUrl());
        entity.setPremium(dto.getIsPremium() != null ? dto.getIsPremium() : false);
        entity.setDurationInMinutes(durationToMinutes(dto.getDuration()));
        return entity;
    }

    private String titleToSlug(String title) {
        if (title == null) return null;
        return title.toLowerCase().replaceAll("[^a-z0-9]+", "-");
    }

    private String minutesToDuration(Integer minutes) {
        if (minutes == null) return null;
        long hours = minutes / 60;
        long mins = minutes % 60;
        return String.format("%dh %dm", hours, mins);
    }

    private Integer durationToMinutes(String duration) {
        if (duration == null) return null;
        try {
            String[] parts = duration.trim().split("\\s+");
            long hours = 0, minutes = 0;
            for (String part : parts) {
                if (part.endsWith("h")) {
                    hours = Long.parseLong(part.replace("h", ""));
                } else if (part.endsWith("m")) {
                    minutes = Long.parseLong(part.replace("m", ""));
                }
            }
            return (int) (hours * 60 + minutes);
        } catch (Exception e) {
            throw new IllegalArgumentException("Duração em formato inválido: " + duration);
        }
    }

    public EnhancedFilmDTO create(EnhancedFilmDTO dto) {
        Movie movie = toEntity(dto);

        Category category = categoryRepository.findByName(dto.getCategory()).orElse(null);
        if (category == null) {
            category = new Category();
            category.setName(dto.getCategory());
            categoryRepository.save(category);
        }
        movie.setCategory(category);

        movie.setGenres(dto.getGenres().stream()
                .map(genreDTO -> genreRepository.findById(genreDTO.getId())
                        .orElseThrow(() -> new RuntimeException("Genero não encontrado com ID: " + genreDTO.getId())))
                .collect(Collectors.toSet()));

        movie = movieRepository.save(movie);
        return toDTO(movie);
    }

    public EnhancedFilmDTO update(Long id, EnhancedFilmDTO dto) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Filme não encontrado com ID: " + id));

        if (dto.getTitle() != null) {
            movie.setTitle(dto.getTitle());
            movie.setSlug(titleToSlug(dto.getTitle()));
        }
        if (dto.getDirector() != null) movie.setDirector(dto.getDirector());
        if (dto.getYear() != null) movie.setReleaseYear(dto.getYear());
        if (dto.getCategory() != null) {
            Category category = categoryRepository.findByName(dto.getCategory()).orElse(null);
            if (category == null) {
                category = new Category();
                category.setName(dto.getCategory());
                categoryRepository.save(category);
            }
            movie.setCategory(category);
        }
        if (dto.getGenres() != null && !dto.getGenres().isEmpty()) {
            movie.setGenres(dto.getGenres().stream()
                    .map(genreDTO -> genreRepository.findById(genreDTO.getId())
                            .orElseThrow(() -> new RuntimeException("Genero não encontrado com ID: " + genreDTO.getId())))
                    .collect(Collectors.toSet()));
        }

        movie = movieRepository.save(movie);
        return toDTO(movie);
    }

    public void delete(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Filme não encontrado com o ID: " + id));
        movieRepository.delete(movie);
    }

//    public ResponseEntity<MovieDTO> create(MovieDTO dto) {
//        try {
//            Movie movie = objectMapper.convertValue(dto, Movie.class);
//
//            if (dto.getCategory() != null && dto.getCategory().getId() != null) {
//                Category category = categoryRepository.findById(dto.getCategory().getId())
//                    .orElseThrow(() -> new NoSuchElementException("Categoria não encontrada"));
//                movie.setCategory(category);
//            }
//
//            if (dto.getGenres() != null && !dto.getGenres().isEmpty()) {
//                dto.getGenres().forEach(genreDTO -> {
//                    if (genreDTO.getName() != null) {
//                        Genre genre = genreRepository.findByName(genreDTO.getName())
//                            .orElseThrow(() -> new NoSuchElementException("Genero não encontrado"));
//                        movie.getGenres().add(genre);
//                    }
//                });
//            }
//
//            Movie saved = movieRepository.save(movie);
//            MovieDTO savedDto = mapToMovieDTO(saved);
//            return ResponseEntity.status(HttpStatus.CREATED).body(savedDto);
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
//        }
//    }

    public ResponseEntity<MovieDTO> update(Long id, MovieDTO dto) {
        try {
            Movie movie = findActiveMovieById(id);

            // Update basic properties
            movie.setTitle(dto.getTitle());
            movie.setDirector(dto.getDirector());
            movie.setReleaseYear(dto.getReleaseYear());
            movie.setDurationInMinutes(dto.getDurationInMinutes());
            movie.setDescription(dto.getDescription());
            movie.setPosterUrl(dto.getPosterUrl());
            movie.setPremium(dto.isPremium());
            movie.setFeatured(dto.isFeatured());

            if (dto.getCategory() != null && dto.getCategory().getId() != null) {
                Category category = categoryRepository.findById(dto.getCategory().getId())
                    .orElseThrow(() -> new NoSuchElementException("Categoria não encontrada"));
                movie.setCategory(category);
            }

            if (dto.getGenres() != null) {
                movie.getGenres().clear();
                dto.getGenres().forEach(genreDTO -> {
                    if (genreDTO.getName() != null) {
                        Genre genre = genreRepository.findByName(genreDTO.getName())
                            .orElseThrow(() -> new NoSuchElementException("Genero não encontrado"));
                        movie.getGenres().add(genre);
                    }
                });
            }

            Movie updated = movieRepository.save(movie);
            return ResponseEntity.ok(mapToMovieDTO(updated));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

//    public ResponseEntity<Void> delete(Long id) {
//        try {
//            movieRepository.deleteById(LocalDateTime.now(), id);
//            return ResponseEntity.noContent().build();
//        } catch (NoSuchElementException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//    }

//    public ResponseEntity<MovieDetailDTO> findById(UUID uuid) {
//        try {
//            Movie movie = findActiveMovieById(uuid);
//            MovieDetailDTO detailDTO = mapToMovieDetailDTO(movie);
//            return ResponseEntity.ok(detailDTO);
//        } catch (NoSuchElementException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//    }
    
//    private MovieDetailDTO mapToMovieDetailDTO(Movie movie) {
//        String genreName = movie.getGenres().stream()
//                .findFirst()
//                .map(GenreEntity::getName)
//                .orElse("");
//
//        double priceValue = 0.0;
//        if (movie.getPrice() != null && !movie.getPrice().isEmpty()) {
//            try {
//                priceValue = Double.parseDouble(movie.getPrice());
//            } catch (NumberFormatException e) {
//                // Handle parsing error if needed
//            }
//        }
//
//        return new MovieDetailDTO(
//            movie.getId(),
//            movie.getTitle(),
//            movie.getDirector(),
//            movie.getDescription(),
//            movie.getPosterUrl(),
//            movie.getTrailerUrl(),
//            movie.getVideoUrl(),
//            movie.getReleaseYear(),
//            genreName,
//            movie.getDurationInMinutes(),
//            priceValue,
//            movie.isPremium(),
//            movie.getCast(),
//            movie.getRating(),
//            movie.getViewCount()
//        );
//    }

    public ResponseEntity<PaginatedFilmsResponse> findAll(String search, List<String> genres, Boolean isPremium, Pageable pageable) {
        try {
            String searchParam = (search != null && !search.trim().isEmpty()) ? search.trim() : null;
            String genresParam = (genres != null && !genres.isEmpty()) ? String.join(",", genres) : null;
            List<String> genreList = (genres != null && !genres.isEmpty()) ? genres : List.of();

            Page<Movie> movies = movieRepository.findAllActiveWithFilters(
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
                        .map(Genre::getName)
                        .orElse("");

                    PaginatedFilmDTO dto = new PaginatedFilmDTO();
                    dto.setId(movie.getId());
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
            List<Movie> newReleases = movieRepository.findNewReleases();
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
        List<Movie> results = movieRepository.filter(genre, yearMin, yearMax, category);

        List<FilteredFilmDTO> films = results.stream().map(movie -> {
            String genreName = movie.getGenres().stream().findFirst().map(Genre::getName).orElse("");

            FilteredFilmDTO dto = new FilteredFilmDTO();
            dto.setId(movie.getId());
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
    private MovieDTO mapToMovieDTO(Movie movie) {
        List<GenreDTO> genreDTOs = movie.getGenres().stream()
            .map(genre -> {
                GenreDTO genreDTO = new GenreDTO();
                genreDTO.setId(genre.getId());
                genreDTO.setName(genre.getName());
                return genreDTO;
            })
            .collect(Collectors.toList());

        CategoryDTO categoryDTO = null;
        if (movie.getCategory() != null) {
            categoryDTO = new CategoryDTO();
            categoryDTO.setId(movie.getCategory().getId());
            categoryDTO.setName(movie.getCategory().getName());
            categoryDTO.setSlug(movie.getCategory().getSlug());
            categoryDTO.setImageUrl(movie.getCategory().getImageUrl());
        }

        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(movie.getId());
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

    private Movie findActiveMovieById(Long id) {
        return movieRepository.findActiveById(id)
                .orElseThrow(() -> new NoSuchElementException("Movie not found"));
    }
}
