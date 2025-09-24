package com.cine.sk.cinesk.domain.user.dto;

import com.cine.sk.cinesk.domain.user.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "User", description = "User profile information")
public class UserDTO {

    @Schema(example = "45")
    private Long id;

    @Schema(example = "Ana Silva")
    private String name;

    @Schema(example = "ana.silva@email.com")
    private String email;

    @Schema(example = "ACTIVE")
    private UserStatus status;

    @Schema(example = "2024-01-15T10:30:00Z")
    private LocalDateTime createdAt;

    @Schema(example = "2024-01-22T14:15:00Z")
    private LocalDateTime updatedAt;

    private List<TransactionDTO> transactions = new ArrayList<>();

}
