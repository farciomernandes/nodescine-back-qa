package com.cine.sk.cinesk.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MoviesDTO {
    private UUID uuid;

    private String title;

    private String slug;

    private String director;

    private Integer releaseYear;

    private Integer durationInMinutes;

    private String description;

    private String posterUrl;

    private boolean premium;

    private boolean featured;

    public boolean isPremium() {
        return premium;
    }

    public boolean isFeatured() {
        return featured;
    }

    private CategoryDTO category;

    private Set<GenreDTO> genres = new HashSet<>();
}
