package com.cine.sk.cinesk.domain.movie;

import com.cine.sk.cinesk.domain.file.File;
import com.cine.sk.cinesk.domain.file.AwsService;
import com.cine.sk.cinesk.domain.movie.category.Category;
import com.cine.sk.cinesk.domain.movie.category.CategoryRepository;
import com.cine.sk.cinesk.domain.movie.genre.GenreDTO;
import com.cine.sk.cinesk.domain.movie.genre.GenreService;
import com.cine.sk.cinesk.domain.user.User;
import com.cine.sk.cinesk.domain.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final AwsService awsService;
    private final UserService userService;
    private final MovieRepository movieRepository;
    private final CategoryRepository categoryRepository;
    private final GenreService genreService;

    public Page<EnhancedMovieResponse> findAll(String searchTerm, Pageable pageable) {
        if(searchTerm == null || searchTerm.isBlank()){
            return movieRepository.findAllByActiveTrue(pageable).map(this::toDTO);
        }
        Page<Movie> moviePage = movieRepository.findAllByFilters(searchTerm, pageable);
        return toResponse(moviePage);
    }

    public Page<EnhancedMovieResponse> findAll(String title, String description, String director, String genre,
                                               String category, String cast, Pageable pageable ) {
        boolean isAllBlank = Stream.of(title, description, director, genre, category, cast)
                .allMatch(s -> s == null || s.isBlank());

        Page<Movie> moviePage;

        if (isAllBlank) {
            moviePage = movieRepository.findAllByActiveTrue(pageable);
        } else {
            moviePage = movieRepository.findAllByFilters(title, description, director, genre, category, cast, pageable);
        }

        return moviePage.map(this::toDTOMainPage);
    }

    public EnhancedMovieResponse insertPoster(Long id, MultipartFile file) {
        var uploaded = awsService.upload(file, "poster", id.toString(), file.getName());
        return insertPoster(uploaded, id);
    }

    public EnhancedMovieResponse findById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));
        return toDTO(movie);
    }

    public EnhancedMovieResponse findBySlug(String slug) {
        return movieRepository.findBySlug(slug).map(this::toDTO).orElse(null);
    }

    private List<EnhancedMovieResponse> findByUserEmail(String email) {
        return movieRepository.findByCreatedBy(email).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private EnhancedMovieResponse toDTO(Movie entity) {
        EnhancedMovieResponse dto = new EnhancedMovieResponse();
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
        dto.setSlug(entity.getSlug());
        dto.setIsAdultConfirmed(entity.getIsAdultConfirmed());
        dto.setProducerDeadline(entity.getProducerDeadline());
        dto.setMovieType(entity.getMovieType());
        dto.setBanner(entity.getBanner());
        return dto;
    }

    private EnhancedMovieResponse toDTOMainPage(Movie entity) {
        EnhancedMovieResponse dto = new EnhancedMovieResponse();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDirector(entity.getDirector());
        dto.setYear(entity.getYear());
        dto.setCategory(entity.getCategory() != null ? entity.getCategory().getName() : null);
        dto.setGenres(entity.getGenres().stream()
            .map(genre -> new GenreDTO(genre.getId(), genre.getName()))
            .collect(Collectors.toList()));
        dto.setDuration(minutesToDuration(entity.getDurationInMinutes()));
        dto.setMovieUrl(null);
        dto.setTrailerUrl(entity.getTrailer());
        dto.setPrice(entity.getPrice());
        dto.setSynopsis(entity.getDescription());
        dto.setPoster(entity.getPoster());
        dto.setCast(entity.getCast());
        dto.setSlug(entity.getSlug());
        dto.setIsAdultConfirmed(entity.getIsAdultConfirmed());
        dto.setProducerDeadline(entity.getProducerDeadline());
        dto.setBanner(entity.getBanner());
        dto.setMovieType(entity.getMovieType());
        return dto;
    }

    private Page<EnhancedMovieResponse> toResponse(Page<Movie> moviePage) {
        List<EnhancedMovieResponse> responses = moviePage.getContent().stream()
                .map(this::toDTO)
                .toList();
        return new PageImpl<>(responses, moviePage.getPageable(), moviePage.getTotalElements());
    }

    private Movie toEntity(EnhancedMovieResponse dto) {
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
        entity.setProducerDeadline(dto.getProducerDeadline());
        entity.setIsAdultConfirmed(dto.getIsAdultConfirmed());
        entity.setMovieType(dto.getMovieType());
        entity.setBanner(dto.getBanner());
        return entity;
    }

    private String titleToSlug(String title) {
        if (title == null) return null;
        return title.toLowerCase().replaceAll("[^a-z0-9]+", "_");
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

    private User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autorizado ou não encontrado");
        }
        String email = auth.getName();
        return userService.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autorizado ou não encontrado"));
    }

    public List<EnhancedMovieResponse> findMyMovies() {
        User user = currentUser();
        return findByUserEmail(user.getEmail());
    }

    @Transactional
    public EnhancedMovieResponse create(EnhancedMovieResponse dto) {
        if (dto.getGenres() == null || dto.getGenres().isEmpty()) {
            throw new IllegalArgumentException("Precisa ter pelo menos um genero");
        }

        String slug = titleToSlug(dto.getTitle());
        if (movieRepository.existsBySlug(slug)) {
            throw new RuntimeException("Movie with slug " + slug + " already exists");
        }

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
                    if (genreDTO.name() == null || genreDTO.name().isBlank()) {
                        throw new IllegalArgumentException("Genre name is required");
                    }
                    return genreService.findByName(genreDTO.name())
                            .orElseGet(() -> genreService.save(new GenreDTO(null, genreDTO.name())));
                })
                .collect(Collectors.toSet()));
        movie.setActive(true);
        movie = movieRepository.save(movie);
        return toDTO(movie);
    }

    public EnhancedMovieResponse insertBanner(Long id, MultipartFile file) {
        var uploaded = awsService.upload(file, "banner", id.toString(), file.getName());
        return insertBanner(uploaded, id);
    }

    public EnhancedMovieResponse update(Long id, EnhancedMovieResponse dto) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Filme não encontrado com ID: " + id));

        if (dto.getTitle() != null) {
            String newSlug = titleToSlug(dto.getTitle());
            if (!Objects.equals(movie.getSlug(), newSlug)) {
                Optional<Movie> movieSlug = movieRepository.findBySlug(newSlug);
                    if (movieSlug.isPresent() && !movieSlug.get().getId().equals(id)) {
                        throw new RuntimeException("Slug já existe: " + newSlug);
                    }

                movie.setSlug(newSlug);
            }
            movie.setTitle(dto.getTitle());
        }

        if(dto.getPrice() != null) movie.setPrice(dto.getPrice());
        if(dto.getDuration() != null) movie.setDurationInMinutes(durationToMinutes(dto.getDuration()));
        if(dto.getMovieUrl() != null) movie.setMovieUrl(dto.getMovieUrl());
        if(dto.getTrailerUrl() != null) movie.setTrailer(dto.getTrailerUrl());
        if(dto.getSynopsis() != null) movie.setDescription(dto.getSynopsis());
        if(dto.getCast() != null) movie.setCast(dto.getCast());

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
                        if (genreDTO.name() == null || genreDTO.name().isBlank()) {
                            throw new IllegalArgumentException("Genre name is required");
                        }
                        return genreService.findByName(genreDTO.name())
                                .orElseGet(() -> genreService.save(new GenreDTO(null, genreDTO.name())));
                    })
                    .collect(Collectors.toSet()));
        }

        movie.setProducerDeadline(dto.getProducerDeadline());
        movie.setIsAdultConfirmed(dto.getIsAdultConfirmed());
        movie.setMovieType(dto.getMovieType());
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


    public EnhancedMovieResponse insertPoster(File file, Long id) {
        Movie movie = movieRepository.findById(id).orElse(null);
        if (movie == null) {
            throw new RuntimeException("Movie not found with id: " + id + " ");
        }
        movie.setPoster(file.getUri());
        movie = movieRepository.save(movie);
        return toDTO(movie);
    }

    public EnhancedMovieResponse insertBanner(File uploaded, Long id) {
        Movie movie = movieRepository.findById(id).orElse(null);
        if (movie == null) {
            throw new RuntimeException("Movie not found with id: " + id + " ");
        }
        movie.setBanner(uploaded.getUri());
        movie = movieRepository.save(movie);
        return toDTO(movie);
    }

}
