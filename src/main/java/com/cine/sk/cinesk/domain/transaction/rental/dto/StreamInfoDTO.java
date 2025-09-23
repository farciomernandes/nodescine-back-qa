package com.cine.sk.cinesk.domain.transaction.rental.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StreamInfoDTO {
    private String streamUrl;
    private LocalDateTime expiresAt;
    private List<QualityOptionDTO> qualityOptions;
    private Map<String, String> drmInfo;
}
