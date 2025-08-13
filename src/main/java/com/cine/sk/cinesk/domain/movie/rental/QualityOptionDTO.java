package com.cine.sk.cinesk.domain.movie.rental;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QualityOptionDTO {
    private String quality;
    private String resolution;
    private Long bitrate;
}
