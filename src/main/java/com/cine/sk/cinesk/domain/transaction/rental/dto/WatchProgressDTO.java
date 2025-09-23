package com.cine.sk.cinesk.domain.transaction.rental.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "WatchProgress", description = "Watch progress details for a rental")
public class WatchProgressDTO {
    @JsonProperty("current_time")
    private Integer currentTime;

    @JsonProperty("percentage")
    private Double percentage;

    @JsonProperty("last_watched")
    private String lastWatched;
}

