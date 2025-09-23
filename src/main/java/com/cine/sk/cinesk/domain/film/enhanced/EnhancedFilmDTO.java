package com.cine.sk.cinesk.domain.movie.enhanced;

import com.cine.sk.cinesk.domain.movie.genre.GenreDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EnhancedFilmDTO {
    private Long id;
    private String title;
    private String director;
    private Integer year;
    private String state;
    private String category;
    private List<GenreDTO> genres;
    private String originalLanguage;
    private List<String> subtitles;
    private String format;
    private String color;
    private String duration;
    private String ageRating;
    private String posterUrl;
    private String wallpaperUrl;
    private String filmUrl;
    private String trailerUrl;
    private String rentalPrice;
    private String synopsis;
    private Boolean isPremium;
}
