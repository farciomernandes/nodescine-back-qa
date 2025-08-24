package com.cine.sk.cinesk.domain.movie.dto;

import com.cine.sk.cinesk.domain.movie.category.CategoryDTO;
import com.cine.sk.cinesk.domain.movie.genre.GenreDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieDTO {
    private UUID uuid;

    private String title;

    private String slug;

    private String director;

    private Integer releaseYear;

    private Integer durationInMinutes;

    private String description;

    private String price;

    private String posterUrl;

    private boolean premium;

    private boolean featured;

    private String trailerUrl;

    private String videoUrl;

    private List<String> cast;

    private Double rating;

    private Integer viewCount;

    private CategoryDTO category;

    private List<GenreDTO> genres;

    public boolean isPremium() {
        return premium;
    }

    public boolean isFeatured() {
        return featured;
    }
}
