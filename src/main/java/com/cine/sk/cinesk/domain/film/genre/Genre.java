package com.cine.sk.cinesk.domain.film.genre;

import com.cine.sk.cinesk.domain.AbstractEntity;
import com.cine.sk.cinesk.domain.film.Movie;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "genre")
@Getter
@Setter
public class Genre extends AbstractEntity {

    private String name;

    @ManyToMany(mappedBy = "genres")
    private Set<Movie> films = new HashSet<>();
}