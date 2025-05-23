package com.cine.sk.cinesk.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "movie_tbl")
@Getter
@Setter
public class MovieEntity extends AbstractEntity {

    private String title;

    private String slug;

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    private String trailerUrl;

    private String videoUrl;

    private String thumbnailUrl;

    private Long price;

    private String categoryUuid;

    private String producer;

    private String director;

    @Column
    private String aboutDirector;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "JSON")
    private String actors;

    @Column(columnDefinition = "TINYINT(1) DEFAULT 1")
    private boolean isFree = true;
}
