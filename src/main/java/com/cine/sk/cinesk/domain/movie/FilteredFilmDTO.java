package com.cine.sk.cinesk.domain.movie;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilteredFilmDTO {
    private String id;
    private String title;
    private String director;
    private String posterUrl;
    private int releaseYear;
    private String genre;
    private int durationInMinutes;
    private String price;
    private boolean isPremium;
}
