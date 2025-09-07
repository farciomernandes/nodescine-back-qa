package com.cine.sk.cinesk.domain.film.category;

import com.cine.sk.cinesk.domain.AbstractEntity;
import com.cine.sk.cinesk.domain.film.Movie;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
public class Category extends AbstractEntity {

    @Column
    private String name;

    @Column
    private String slug;

    @Column
    private String imageUrl;

    @OneToMany(mappedBy = "category")
    private List<Movie> movies = new ArrayList<>();
}
