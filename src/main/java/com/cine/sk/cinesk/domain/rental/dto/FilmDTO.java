package com.cine.sk.cinesk.domain.rental.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Film", description = "Basic information about a film in a rental")
public class FilmDTO {
    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("poster_url")
    private String posterUrl;

    @JsonProperty("duration")
    private Integer duration;
}

