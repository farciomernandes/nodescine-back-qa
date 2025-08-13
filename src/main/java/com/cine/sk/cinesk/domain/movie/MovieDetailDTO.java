package com.cine.sk.cinesk.domain.movie;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDetailDTO {
    private String id;
    private String title;
    private String director;
    private String synopsis;
    private String posterUrl;
    private String trailerUrl;
    private String videoUrl;
    private int releaseYear;
    private String genre;
    private int durationInMinutes;
    private double price;
    private boolean isPremium;
    private List<String> cast;
    private Double rating;
    private Integer viewCount;
}
