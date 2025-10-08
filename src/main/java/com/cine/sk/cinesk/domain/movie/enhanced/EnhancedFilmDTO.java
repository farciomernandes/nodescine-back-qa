package com.cine.sk.cinesk.domain.movie.enhanced;

import com.cine.sk.cinesk.domain.movie.genre.GenreDTO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EnhancedFilmDTO {
    private Long id;
    private String title;
    private String director;
    private Integer year;
    private String category;
    private BigDecimal price;
    private List<GenreDTO> genres;
    private String duration;
    private String poster;
    private String movieUrl;
    private String trailerUrl;
    private String synopsis;
    List<String> cast = new ArrayList<>();
}
