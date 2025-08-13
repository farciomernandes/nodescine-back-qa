package com.cine.sk.cinesk.domain.movie.rental;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubtitleDTO {
    private String language;
    private String languageCode;
    private String url;
    private String format;
}
