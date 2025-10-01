package com.cine.sk.cinesk.domain.movie.genre;

import com.cine.sk.cinesk.domain.AbstractEntity;
import com.cine.sk.cinesk.domain.movie.Movie;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "genre")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Genre extends AbstractEntity {

    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "genres")
    private Set<Movie> movies = new HashSet<>();
}