package com.cine.sk.cinesk.domain.movie;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchFilmDTO {
    private String id;
    private String title;
    private String director;
    private String posterUrl;
    private int matchScore;
}
