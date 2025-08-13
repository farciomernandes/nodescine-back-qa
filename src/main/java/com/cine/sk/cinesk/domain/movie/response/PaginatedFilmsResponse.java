package com.cine.sk.cinesk.domain.movie.response;

import com.cine.sk.cinesk.domain.movie.PaginatedFilmDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedFilmsResponse {
    private List<PaginatedFilmDTO> films;
    private PaginationMetadata pagination;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaginationMetadata {
        private int current_page;
        private int total_pages;
        private long total_items;
        private int per_page;
    }
}
