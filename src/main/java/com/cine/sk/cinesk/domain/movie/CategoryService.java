package com.cine.sk.cinesk.domain.movie;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final ObjectMapper objectMapper;

    public ResponseEntity<CategoryDTO> create(CategoryDTO dto) {
        try {
            boolean result = validateCategoryExistsByName(dto.getName());
            if (!result) {
                CategoryEntity category = objectMapper.convertValue(dto, CategoryEntity.class);
                CategoryEntity saved = categoryRepository.save(category);
                return ResponseEntity.status(HttpStatus.CREATED).body(objectMapper.convertValue(saved, CategoryDTO.class));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public void deleteByName(String name) {
        categoryRepository.deleteByName(LocalDateTime.now(), name);
    }

    private boolean validateCategoryExistsByName(String categoryName) {
        Optional<CategoryEntity> category = categoryRepository.findByName(categoryName);
        return category.isPresent();
    }

    public void validateCategoryExists(UUID categoryUuid) {
        categoryRepository.findById(categoryUuid)
                .orElseThrow(() -> new NoSuchElementException("Category not found"));
    }

    public ResponseEntity<List<CategoryDTO>> getAll() {
        return ResponseEntity.ok(objectMapper.convertValue(categoryRepository.findAll(), List.class));
    }

    public List<Object> getAllCategoryDetails() {
        List<CategoryEntity> categories = categoryRepository.findAll();
        List<Object> result = new ArrayList<>();
        for (CategoryEntity category : categories) {
            int filmCount = category.getMovies() != null ? category.getMovies().size() : 0;
            result.add(new LinkedHashMap<String, Object>() {{
                put("id", category.getUuid().toString());
                put("name", category.getName());
                put("image_url", category.getImageUrl());
                put("film_count", filmCount);
            }});
        }
        return result;
    }
}
