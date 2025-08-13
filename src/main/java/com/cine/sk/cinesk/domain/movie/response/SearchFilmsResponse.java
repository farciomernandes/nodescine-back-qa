package com.cine.sk.cinesk.domain.movie.response;

import com.cine.sk.cinesk.domain.movie.SearchFilmDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchFilmsResponse {
    private List<SearchFilmDTO> films;
    private int total_results;
}
