package com.cine.sk.cinesk.domain.movie.genre;

import com.cine.sk.cinesk.domain.AbstractEntity;
import com.cine.sk.cinesk.domain.movie.MovieEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "genre_tbl")
@Getter
@Setter
public class GenreEntity extends AbstractEntity {

    private String name;

    @ManyToMany(mappedBy = "genres")
    private Set<MovieEntity> films = new HashSet<>();
}