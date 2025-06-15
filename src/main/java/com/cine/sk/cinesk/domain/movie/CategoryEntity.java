package com.cine.sk.cinesk.domain.movie;

import com.cine.sk.cinesk.domain.AbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category_tbl")
@Getter
@Setter
public class CategoryEntity extends AbstractEntity {

    @Column
    private String name;

    @Column
    private String slug;

    @Column
    private String imageUrl;

    @OneToMany(mappedBy = "category")
    private List<MovieEntity> films = new ArrayList<>();
}
