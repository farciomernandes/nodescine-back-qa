package com.cine.sk.cinesk.domain.movie;

import com.cine.sk.cinesk.domain.AbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "film_tbl")
@Getter
@Setter
public class MovieEntity extends AbstractEntity {

    private String title;

    private String slug;

    private String director;

    private Integer releaseYear;

    private String price;

    private Integer durationInMinutes;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String posterUrl;

    private boolean premium;

    private boolean featured;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @ManyToMany
    @JoinTable(
        name = "film_genres",
        joinColumns = @JoinColumn(name = "film_id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<GenreEntity> genres = new HashSet<>();
}
