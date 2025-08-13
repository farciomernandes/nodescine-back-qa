package com.cine.sk.cinesk.domain.movie.rental;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgressResponseDTO {
    private boolean success;
    private Long currentTime;
    private Double percentageWatched;
    private LocalDateTime lastUpdated;
    private String quality;
    private String device;
}
