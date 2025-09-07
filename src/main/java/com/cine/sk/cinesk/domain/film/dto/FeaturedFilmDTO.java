package com.cine.sk.cinesk.domain.film.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeaturedFilmDTO {
    private String id;
    private String title;
    private String director;
    private String posterUrl;
    private Integer year;
    private String genre;
    private Integer duration;
    private String price;
    private boolean isPremium;
}

