package com.cine.sk.cinesk.controller;

import com.cine.sk.cinesk.dto.CategoryDTO;
import com.cine.sk.cinesk.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/category")
@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO dto) {
        return categoryService.create(dto);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> removeCategory(@PathVariable String name) {
        categoryService.deleteByName(name);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return categoryService.getAll();
    }
}
