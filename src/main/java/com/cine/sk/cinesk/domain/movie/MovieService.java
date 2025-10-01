package com.cine.sk.cinesk.domain.movie;

import com.cine.sk.cinesk.domain.file.File;
import com.cine.sk.cinesk.domain.movie.category.Category;
import com.cine.sk.cinesk.domain.movie.category.CategoryRepository;
import com.cine.sk.cinesk.domain.movie.enhanced.EnhancedFilmDTO;
import com.cine.sk.cinesk.domain.movie.genre.GenreDTO;
import com.cine.sk.cinesk.domain.movie.genre.GenreService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final CategoryRepository categoryRepository;
    private final GenreService genreService;

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
        dto.setYear(entity.getYear());
        dto.setCategory(entity.getCategory() != null ? entity.getCategory().getName() : null);
        dto.setGenres(entity.getGenres().stream()
                .map(genre -> new GenreDTO(genre.getId(), genre.getName()))
                .collect(Collectors.toList()));
        dto.setDuration(minutesToDuration(entity.getDurationInMinutes()));
        dto.setMovieUrl(entity.getMovieUrl());
        dto.setTrailerUrl(entity.getTrailer());
        dto.setPrice(entity.getPrice());
        dto.setSynopsis(entity.getDescription());
        dto.setPoster(entity.getPoster());
        dto.setCast(entity.getCast());
        return dto;
    }

    private Movie toEntity(EnhancedFilmDTO dto) {
        Movie entity = new Movie();
        entity.setTitle(dto.getTitle());
        entity.setSlug(titleToSlug(dto.getTitle()));
        entity.setDirector(dto.getDirector());
        entity.setYear(dto.getYear());
        entity.setPrice(dto.getPrice());
        entity.setDescription(dto.getSynopsis());
        entity.setMovieUrl(dto.getMovieUrl());
        entity.setTrailer(dto.getTrailerUrl());
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

    @Transactional
    public EnhancedFilmDTO create(EnhancedFilmDTO dto) {
        Movie movie = toEntity(dto);

        Category category = categoryRepository.findByName(dto.getCategory()).orElse(null);
        if (category == null) {
            category = new Category();
            category.setName(dto.getCategory());
            categoryRepository.save(category);
        }
        movie.setCategory(category);

        if(dto.getCast() != null) {
            movie.setCast(dto.getCast());
        }

        movie.setGenres(dto.getGenres().stream()
                .map(genreDTO -> {
                    if (genreDTO.getName() == null || genreDTO.getName().isBlank()) {
                        throw new IllegalArgumentException("Genre name is required");
                    }
                    return genreService.findByName(genreDTO.getName())
                            .orElseGet(() -> genreService.save(new GenreDTO(null, genreDTO.getName())));
                })
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
        if (dto.getYear() != null) movie.setYear(dto.getYear());
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
                    .map(genreDTO -> {
                        if (genreDTO.getName() == null || genreDTO.getName().isBlank()) {
                            throw new IllegalArgumentException("Genre name is required");
                        }
                        return genreService.findByName(genreDTO.getName())
                                .orElseGet(() -> genreService.save(new GenreDTO(null, genreDTO.getName())));
                    })
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


    public void insertPoster(File file, Long id) {
        Movie movie = movieRepository.findById(id).orElse(null);
        if (movie == null) return;
        movie.setPoster(file.getUri());
        movieRepository.save(movie);
    }
}