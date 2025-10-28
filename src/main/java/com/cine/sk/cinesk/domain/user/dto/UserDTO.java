package com.cine.sk.cinesk.domain.user.dto;

import com.cine.sk.cinesk.domain.auth.enums.Role;
import com.cine.sk.cinesk.domain.user.enums.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    private String cpf;

    private String phone;

    private String postalCode;

    private String address;

    private String addressNumber;

    private String complement;

    private String province;

    private Set<Role> roles;

    private List<TransactionDTO> transactions = new ArrayList<>();

    private BigDecimal totalAmount;

    private int totalMovie;

    private String walletId;

}
