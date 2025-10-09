package com.cine.sk.cinesk.domain.movie;

import com.cine.sk.cinesk.domain.file.File;
import com.cine.sk.cinesk.domain.movie.category.Category;
import com.cine.sk.cinesk.domain.movie.category.CategoryRepository;
import com.cine.sk.cinesk.domain.movie.enhanced.EnhancedFilmDTO;
import com.cine.sk.cinesk.domain.movie.genre.GenreDTO;
import com.cine.sk.cinesk.domain.movie.genre.GenreService;
import com.cine.sk.cinesk.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final CategoryRepository categoryRepository;
    private final GenreService genreService;

    public Page<EnhancedFilmDTO> findAll(String searchTerm, Pageable pageable) {
        if(searchTerm == null || searchTerm.isBlank()){
            return movieRepository.findAllByActiveTrue(pageable).map(this::toDTO);
        }
        Page<Movie> moviePage = movieRepository.findAllByFilters(searchTerm, pageable);
        return toResponse(moviePage);
    }

    public Page<EnhancedFilmDTO> findAll( String title, String description, String director, String genre,
                                          String category, String cast, Pageable pageable ) {
        boolean isAllBlank = Stream.of(title, description, director, genre, category, cast)
                .allMatch(s -> s == null || s.isBlank());

        Page<Movie> moviePage;

        if (isAllBlank) {
            moviePage = movieRepository.findAllByActiveTrue(pageable);
        } else {
            moviePage = movieRepository.findAllByFilters(title, description, director, genre, category, cast, pageable);
        }

        return moviePage.map(this::toDTO);
    }


    public EnhancedFilmDTO findById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));
        return toDTO(movie);
    }

    public List<EnhancedFilmDTO> findByUserEmail(String email) {
        return movieRepository.findAllByCreatedBy(email).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
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

    private Page<EnhancedFilmDTO> toResponse(Page<Movie> moviePage) {
        List<EnhancedFilmDTO> responses = moviePage.getContent().stream()
                .map(this::toDTO)
                .toList();
        return new PageImpl<>(responses, moviePage.getPageable(), moviePage.getTotalElements());
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
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Session not found");
        }
        String email = authentication.getName();
        movie.setCreatedBy(email);

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
        movie.setActive(true);
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

        movie.setActive(true);
        movie = movieRepository.save(movie);
        return toDTO(movie);
    }

    public void delete(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Filme não encontrado com o ID: " + id));
        movie.setActive(false);
        movieRepository.save(movie);
    }


    public EnhancedFilmDTO insertPoster(File file, Long id) {
        Movie movie = movieRepository.findById(id).orElse(null);
        if (movie == null) {
            throw new RuntimeException("Movie not found with id: " + id + " ");
        }
        movie.setPoster(file.getUri());
        movie = movieRepository.save(movie);
        return toDTO(movie);
    }
}