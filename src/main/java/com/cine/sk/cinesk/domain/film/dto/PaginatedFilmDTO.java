package com.cine.sk.cinesk.domain.film.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedFilmDTO {
    private Long id;
    private String title;
    private String director;
    private String poster_url;
    private Integer release_year;
    private String genre;
    private Integer duration_minutes;
    private String price;
    private Integer releaseYear;
    private String posterUrl;
    private Integer durationInMinutes;
    private boolean premium;

}