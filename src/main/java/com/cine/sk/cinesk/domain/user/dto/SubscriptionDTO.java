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
@Schema(name = "Subscription", description = "User subscription data")
public class SubscriptionDTO {
    @Schema(example = "premium")
    @JsonProperty("type")
    private String type;

    @Schema(example = "2024-01-15T10:30:00Z")
    @JsonProperty("since")
    private String since;

    @Schema(example = "2025-01-15T10:30:00Z")
    @JsonProperty("expires_at")
    private String expiresAt;
}

