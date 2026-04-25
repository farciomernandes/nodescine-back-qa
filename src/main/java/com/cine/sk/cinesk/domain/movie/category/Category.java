package com.cine.sk.cinesk.domain.movie.category;

import com.cine.sk.cinesk.domain.AbstractEntity;
import com.cine.sk.cinesk.domain.movie.Movie;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(unique = true)
    private String name;

    @Column
    private String slug;

    @Column
    private String imageUrl;

    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private List<Movie> movies = new ArrayList<>();
}