package com.cine.sk.cinesk.domain.user.dto;

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
@Schema(name = "UserStats", description = "Aggregated user statistics")
public class StatsDTO {
    @JsonProperty("total_rentals")
    private Integer totalRentals;

    @JsonProperty("active_rentals")
    private Integer activeRentals;

    @JsonProperty("total_watch_time")
    private Integer totalWatchTime;

    @JsonProperty("favorite_genre")
    private String favoriteGenre;

    @JsonProperty("movies_completed")
    private Integer moviesCompleted;

    @JsonProperty("last_activity")
    private String lastActivity;

    @JsonProperty("monthly_usage")
    private MonthlyUsageDTO monthlyUsage;
}

