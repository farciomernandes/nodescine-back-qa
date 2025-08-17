package com.cine.sk.cinesk.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Preferences", description = "User playback and notification preferences")
public class PreferencesDTO {
    @Schema(example = "pt-BR")
    @NotBlank
    @JsonProperty("language")
    private String language;

    @Schema(example = "1080p")
    @NotBlank
    @JsonProperty("quality")
    private String quality;

    @Schema(example = "true")
    @NotNull
    @JsonProperty("notifications")
    private Boolean notifications;
}

