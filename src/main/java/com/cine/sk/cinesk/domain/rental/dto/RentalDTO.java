package com.cine.sk.cinesk.domain.rental.dto;

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
@Schema(name = "Rental", description = "Rental information with film and progress details")
public class RentalDTO {
    @JsonProperty("id")
    private String id;

    @JsonProperty("film")
    private FilmDTO film;

    @JsonProperty("rented_at")
    private String rentedAt;

    @JsonProperty("expires_at")
    private String expiresAt;

    @JsonProperty("price")
    private String price;

    @JsonProperty("status")
    private String status;

    @JsonProperty("watch_progress")
    private WatchProgressDTO watchProgress;

    @JsonProperty("time_remaining")
    private String timeRemaining;

    @JsonProperty("stream_url")
    private String streamUrl;
}
