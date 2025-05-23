package com.cine.sk.cinesk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class MoviesDTO {
    private UUID uuid;

    private String title;

    private String slug;

    private String description;

    private String trailerUrl;

    private String videoUrl;

    private String thumbnailUrl;

    private Long price;

    private String categoryUuid;

    private String producer;

    private String director;

    private String aboutDirector;

    private String actors;

    private boolean isFree;
}
