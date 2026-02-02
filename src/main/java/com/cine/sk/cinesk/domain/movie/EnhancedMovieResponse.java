package com.cine.sk.cinesk.domain.movie;

import com.cine.sk.cinesk.domain.movie.genre.GenreDTO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EnhancedMovieResponse {
    private Long id;
    private String title;
    private String director;
    private Integer year;
    private String category;
    private BigDecimal price;
    private List<GenreDTO> genres;
    private String duration;
    private String poster;
    private String banner;
    private String background;
    private String movieUrl;
    private String trailerUrl;
    private String synopsis;
    private String slug;
    List<String> cast = new ArrayList<>();
    private Boolean isAdultConfirmed;
    private String producerDeadline;
    private MovieType movieType;
}
