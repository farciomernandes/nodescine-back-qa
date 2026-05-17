package com.cine.sk.cinesk.domain.movie;

import com.cine.sk.cinesk.domain.AbstractEntity;
import com.cine.sk.cinesk.domain.movie.category.Category;
import com.cine.sk.cinesk.domain.movie.genre.Genre;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "movie")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Movie extends AbstractEntity {

    private String title;

    private String slug;

    private String director;

    private Integer year;

    private BigDecimal price;

    private Integer durationInMinutes;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String poster;

    @Column(columnDefinition = "TEXT")
    private String banner;
    
    private String trailer;
    
    private String movieUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovieType movieType;

    @ElementCollection
    @CollectionTable(name = "movie_cast", joinColumns = @JoinColumn(name = "movie_id"))
    @Column(name = "actor_name")
    private List<String> cast = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    @ColumnDefault("'UNKNOWN'")
    private MovieFormat format = MovieFormat.UNKNOWN;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    @ColumnDefault("'PUBLISHED'")
    private ModerationStatus moderationStatus = ModerationStatus.PUBLISHED;

    @ManyToMany
    @JoinTable(
        name = "movie_genres",
        joinColumns = @JoinColumn(name = "movie_id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    private String createdBy;

    private Boolean active;

    private String producerDeadline;

    private Boolean isAdultConfirmed;
}