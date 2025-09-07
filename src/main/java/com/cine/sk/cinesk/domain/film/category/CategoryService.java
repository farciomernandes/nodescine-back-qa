package com.cine.sk.cinesk.domain.film.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Object> getAllCategoryDetails() {
        List<Category> categories = categoryRepository.findAll();
        List<Object> result = new ArrayList<>();
        for (Category category : categories) {
            int filmCount = category.getMovies() != null ? category.getMovies().size() : 0;
            result.add(new LinkedHashMap<String, Object>() {{
                put("id", category.getId().toString());
                put("name", category.getName());
                put("image_url", category.getImageUrl());
                put("film_count", filmCount);
            }});
        }
        return result;
    }
}
