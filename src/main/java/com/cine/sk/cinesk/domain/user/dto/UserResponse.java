package com.cine.sk.cinesk.domain.user.dto;

import com.cine.sk.cinesk.domain.auth.Role;
import com.cine.sk.cinesk.domain.user.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Builder
@Schema(name = "User", description = "User profile information")
public record UserResponse(
    @Schema(example = "45")
    Long id,

    @Schema(example = "Ana Silva")
    String name,

    @Schema(example = "ana.silva@email.com")
    String email,

    @Schema(example = "ACTIVE")
    UserStatus status,

    @Schema(example = "2024-01-15T10:30:00Z")
    LocalDateTime createdAt,

    @Schema(example = "2024-01-22T14:15:00Z")
    LocalDateTime updatedAt,

    String cpf,

    String phone,

    String postalCode,

    String address,

    String addressNumber,

    String complement,

    String province,

    Set<Role> roles,

    List<TransactionDTO> transactions,

    BigDecimal totalAmount,

    int totalMovie,

    String walletId
) {
    public UserResponse {
        if (transactions == null) {
            transactions = new ArrayList<>();
        }
    }
}
