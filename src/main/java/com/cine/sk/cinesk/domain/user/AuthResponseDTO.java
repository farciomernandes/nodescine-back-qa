package com.cine.sk.cinesk.domain.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Response containing the authentication token.")
public class AuthResponseDTO {

    @Schema(description = "JWT authentication token.", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

}
