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
@Schema(name = "User", description = "User profile information")
public class UserDTO {
    @Schema(example = "45")
    @JsonProperty("id")
    private Long id;

    @Schema(example = "Ana Silva")
    @JsonProperty("name")
    private String name;

    @Schema(example = "ana.silva@email.com")
    @JsonProperty("email")
    private String email;

    @Schema(example = "https://...")
    @JsonProperty("avatar")
    private String avatar;

    @Schema(example = "2024-01-15T10:30:00Z")
    @JsonProperty("created_at")
    private String createdAt;

    @Schema(example = "2024-01-22T14:15:00Z")
    @JsonProperty("updated_at")
    private String updatedAt;

    @JsonProperty("subscription")
    private SubscriptionDTO subscription;

    @JsonProperty("preferences")
    private PreferencesDTO preferences;
}
