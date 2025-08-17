package com.cine.sk.cinesk.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UpdateProfileRequest", description = "Request to update user profile")
public class UpdateProfileRequestDTO {
    @Schema(example = "Ana Silva Santos")
    @NotBlank
    @JsonProperty("name")
    private String name;

    @Valid
    @JsonProperty("preferences")
    private PreferencesDTO preferences;
}
