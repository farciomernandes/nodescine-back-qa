package com.cine.sk.cinesk.domain.film.response;

import com.cine.sk.cinesk.domain.film.dto.PaginatedFilmDTO;
import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedFilmsResponse {
    private List<PaginatedFilmDTO> films;
    private PaginationMetadata pagination;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaginationMetadata {
        private int current_page;
        private int total_pages;
        private long total_items;
        private int per_page;
    }
}
