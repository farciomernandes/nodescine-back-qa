package com.cine.sk.cinesk.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "category_tbl")
@Getter
@Setter
public class CategoryEntity extends AbstractEntity {

    @Column
    private String name;

    @Column
    private String slug;
}
