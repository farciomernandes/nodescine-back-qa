package com.cine.sk.cinesk.domain.movie.rental;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgressUpdateDTO {
    @NotNull(message = "Current time is required")
    @Min(value = 0, message = "Current time must be positive")
    private Long currentTime;
    
    @NotNull(message = "Total duration is required")
    @Min(value = 0, message = "Total duration must be positive")
    private Long totalDuration;
    
    @NotNull(message = "Quality is required")
    private String quality;
    
    @NotNull(message = "Device information is required")
    private String device;
}
