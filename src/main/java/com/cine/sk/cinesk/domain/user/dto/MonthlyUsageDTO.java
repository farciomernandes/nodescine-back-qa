package com.cine.sk.cinesk.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "MonthlyUsage", description = "Monthly usage stats keyed by month name")
public class MonthlyUsageDTO {
    private Map<String, MonthEntry> entries = new HashMap<>();

    @JsonAnyGetter
    public Map<String, MonthEntry> getEntries() {
        return entries == null ? Collections.emptyMap() : entries;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthEntry {
        @Schema(example = "8")
        private Integer rentals;
        @Schema(example = "720")
        private Integer watch_time;
    }
}

