package com.cine.sk.cinesk.service;

import com.cine.sk.cinesk.dto.CategoryDTO;
import com.cine.sk.cinesk.dto.MoviesDTO;
import com.cine.sk.cinesk.entity.CategoryEntity;
import com.cine.sk.cinesk.entity.MovieEntity;
import com.cine.sk.cinesk.repository.CategoryRepository;
import com.cine.sk.cinesk.repository.MovieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
}
