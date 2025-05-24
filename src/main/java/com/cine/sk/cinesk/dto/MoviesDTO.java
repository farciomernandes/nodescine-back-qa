package com.cine.sk.cinesk.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
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

    private String producer;

    private String director;

    private String aboutDirector;

    private List<String> actors;

    private boolean isFree;

    private UUID categoryUuid;
}
