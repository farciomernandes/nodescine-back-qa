package com.cine.sk.cinesk.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "movie_categories_tbl")
@Getter
@Setter
public class MovieCategoryEntity extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "movieId", referencedColumnName = "uuid")
    private MovieEntity movie;

    @ManyToOne
    @JoinColumn(name = "categoryId", referencedColumnName = "uuid")
    private CategoryEntity category;
}
