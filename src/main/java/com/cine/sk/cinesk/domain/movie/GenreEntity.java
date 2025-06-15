package com.cine.sk.cinesk.domain.movie;

import com.cine.sk.cinesk.domain.AbstractEntity;
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