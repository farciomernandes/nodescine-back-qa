package com.cine.sk.cinesk.domain.user.dto;

import com.cine.sk.cinesk.domain.auth.Role;
import com.cine.sk.cinesk.domain.user.UserStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "UpdateUserAdminRequest", description = "Partial update for a user. Admin/moderator only.")
public class UpdateUserDTO {

    @Schema(description = "User name")
    @JsonProperty("name")
    private String name;

    @Schema(description = "User email")
    @JsonProperty("email")
    private String email;

    @Schema(description = "Avatar URL")
    @JsonProperty("avatar")
    private String avatar;

    @Schema(description = "User roles")
    @JsonProperty("roles")
    private Set<Role> roles;

    @Schema(description = "User status")
    @JsonProperty("status")
    private UserStatus status;
}
