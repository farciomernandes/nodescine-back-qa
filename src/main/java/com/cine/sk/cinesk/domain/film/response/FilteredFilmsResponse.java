package com.cine.sk.cinesk.domain.film.response;

import com.cine.sk.cinesk.domain.film.dto.FilteredFilmDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilteredFilmsResponse {
    private List<FilteredFilmDTO> films;
    private Map<String, Object> filters_applied;
    private int total_results;
}
