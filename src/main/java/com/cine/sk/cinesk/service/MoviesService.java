package com.cine.sk.cinesk.service;


import com.cine.sk.cinesk.dto.CategoryDTO;
import com.cine.sk.cinesk.dto.MovieCategoriesDTO;
import com.cine.sk.cinesk.dto.MoviesDTO;
import com.cine.sk.cinesk.entity.CategoryEntity;
import com.cine.sk.cinesk.entity.MovieCategoryEntity;
import com.cine.sk.cinesk.entity.MovieEntity;
import com.cine.sk.cinesk.repository.CategoryRepository;
import com.cine.sk.cinesk.repository.MovieCategoryRepository;
import com.cine.sk.cinesk.repository.MovieRepository;
import com.cine.sk.cinesk.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MoviesService {

    private final MovieRepository moviesRepository;
    private final UserRepository usersRepository;
    private final MovieCategoryRepository movieCategoriesRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CategoryRepository categoryRepository;

    public MoviesDTO create(MoviesDTO dto) {
        MovieEntity movie = new MovieEntity();
        mapToEntity(dto, movie);
        MovieEntity saved = moviesRepository.save(movie);
        return mapToDTO(saved);
    }

    public CategoryDTO createCategory(CategoryDTO dto) {
        CategoryEntity category = new CategoryEntity();
        mapToEntity(dto, category);
        CategoryEntity saved =  categoryRepository.save(category);
        return mapToDTO(saved);
    }


    public MoviesDTO update(UUID uuid, MoviesDTO dto) {
        MovieEntity movie = moviesRepository.findActiveById(uuid)
                .orElseThrow(() -> new NoSuchElementException("Movie not found"));
        mapToEntity(dto, movie);
        MovieEntity updated = moviesRepository.save(movie);
        return mapToDTO(updated);
    }

    public void delete(UUID uuid) {
        MovieEntity movie = moviesRepository.findActiveById(uuid)
                .orElseThrow(() -> new NoSuchElementException("Movie not found"));
        movie.setDeletedAt(LocalDateTime.now());
        moviesRepository.save(movie);
        // Soft delete associated movie categories
        List<MovieCategoryEntity> movieCategories = movieCategoriesRepository.findByMovieId(uuid);
        movieCategories.forEach(mc -> {
            mc.setDeletedAt(java.time.LocalDateTime.now());
            movieCategoriesRepository.save(mc);
        });
    }

    public MoviesDTO findById(UUID uuid) {
        return moviesRepository.findActiveById(uuid)
                .map(this::mapToDTO)
                .orElseThrow(() -> new NoSuchElementException("Movie not found"));
    }

    public List<MoviesDTO> findAll() {
        return moviesRepository.findAllActive()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<MoviesDTO> findBySlug(String slug) {
        return moviesRepository.findBySlug(slug)
                .filter(movie -> movie.getDeletedAt() == null)
                .map(this::mapToDTO)
                .map(List::of)
                .orElseThrow(() -> new NoSuchElementException("Movie not found"));
    }

    public void addCategoryToMovie(MovieCategoriesDTO dto) {
        MovieEntity movie = moviesRepository.findActiveById(dto.getMovieUuid())
                .orElseThrow(() -> new NoSuchElementException("Movie not found"));
        categoryRepository.findActiveById(dto.getCategoryUuid())
                .orElseThrow(() -> new NoSuchElementException("Category not found"));
        MovieCategoryEntity movieCategory = new MovieCategoryEntity();
        movieCategory.setMovie(movie);
        movieCategory.setCategory(categoryRepository.getReferenceById(dto.getCategoryUuid()));
        movieCategoriesRepository.save(movieCategory);
    }

    public void removeCategoryFromMovie(UUID movieUuid, UUID categoryUuid) {
        MovieCategoryEntity movieCategory = movieCategoriesRepository.findAllActive()
                .stream()
                .filter(mc -> mc.getMovie().getUuid().equals(movieUuid) && mc.getCategory().getUuid().equals(categoryUuid))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Movie-Category association not found"));
        movieCategory.setDeletedAt(java.time.LocalDateTime.now());
        movieCategoriesRepository.save(movieCategory);
    }

    private MoviesDTO mapToDTO(MovieEntity movie) {
        return new MoviesDTO(
                movie.getUuid(),
                movie.getTitle(),
                movie.getSlug(),
                movie.getDescription(),
                movie.getTrailerUrl(),
                movie.getVideoUrl(),
                movie.getThumbnailUrl(),
                movie.getPrice(),
                movie.getCategoryUuid(),
                movie.getProducer(),
                movie.getDirector(),
                movie.getAboutDirector(),
                movie.getActors(),
                movie.isFree()
        );
    }

    private CategoryDTO mapToDTO(CategoryEntity category) {
        return new CategoryDTO(
                category.getUuid(),
                category.getName(),
                category.getSlug()
        );
    }

    private void mapToEntity(MoviesDTO dto, MovieEntity movie) {
        movie.setTitle(dto.getTitle());
        movie.setSlug(dto.getSlug());
        movie.setDescription(dto.getDescription());
        movie.setTrailerUrl(dto.getTrailerUrl());
        movie.setVideoUrl(dto.getVideoUrl());
        movie.setThumbnailUrl(dto.getThumbnailUrl());
        movie.setPrice(dto.getPrice());
        movie.setProducer(dto.getProducer());
        movie.setDirector(dto.getDirector());
        movie.setAboutDirector(dto.getAboutDirector());

        try {
            if (dto.getActors() != null && !dto.getActors().isEmpty()) {
                Object json = objectMapper.readValue(dto.getActors(), Object.class);
                String validJsonString = objectMapper.writeValueAsString(json);
                movie.setActors(validJsonString);
            } else {
                movie.setActors("[]");
            }
        } catch (JsonProcessingException e) {
            try {
                String jsonArray = "[" + (dto.getActors() != null ? "\"" + dto.getActors().replace("\"", "\\\"") + "\"" : "") + "]";
                movie.setActors(jsonArray);
            } catch (Exception ex) {
                movie.setActors("[]");
            }
        }

        movie.setFree(dto.isFree());
    }

    private void mapToEntity(CategoryDTO dto, CategoryEntity category) {
        category.setName(dto.getName());
        category.setSlug(dto.getSlug());
    }
}
