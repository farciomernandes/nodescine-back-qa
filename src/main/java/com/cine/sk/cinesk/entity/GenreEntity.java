package com.cine.sk.cinesk.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "genre_tbl")
@Getter
@Setter
public class GenreEntity extends AbstractEntity {

    private String name;

    @ManyToMany(mappedBy = "genres")
    private Set<MovieEntity> films = new HashSet<>();
}