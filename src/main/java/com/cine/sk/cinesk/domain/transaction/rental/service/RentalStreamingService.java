package com.cine.sk.cinesk.domain.transaction.rental.service;

import com.cine.sk.cinesk.domain.transaction.rental.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RentalStreamingService {

    public ResponseEntity<StreamInfoDTO> getStreamingInfo(UUID rentalId) {
        validateRental(rentalId);

        StreamInfoDTO streamInfo = StreamInfoDTO.builder()
                .streamUrl("https://streaming.cine-sk.com/content/" + rentalId + "?token=xyz123")
                .expiresAt(LocalDateTime.now().plusHours(4))
                .qualityOptions(getQualityOptions())
                .drmInfo(getDrmInfo())
                .build();
        
        return ResponseEntity.ok(streamInfo);
    }

    public ResponseEntity<ProgressResponseDTO> updateProgress(UUID rentalId, ProgressUpdateDTO progressUpdate) {
        validateRental(rentalId);

        double percentageWatched = calculatePercentageWatched(
                progressUpdate.getCurrentTime(),
                progressUpdate.getTotalDuration()
        );
        
        ProgressResponseDTO response = ProgressResponseDTO.builder()
                .success(true)
                .currentTime(progressUpdate.getCurrentTime())
                .percentageWatched(percentageWatched)
                .lastUpdated(LocalDateTime.now())
                .quality(progressUpdate.getQuality())
                .device(progressUpdate.getDevice())
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Gets available subtitles for a rented movie
     */
    public ResponseEntity<List<SubtitleDTO>> getSubtitles(UUID rentalId) {
        validateRental(rentalId);

        List<SubtitleDTO> subtitles = Arrays.asList(
                createSubtitle("English", "en", "vtt"),
                createSubtitle("Spanish", "es", "vtt"),
                createSubtitle("Portuguese", "pt", "vtt"),
                createSubtitle("French", "fr", "vtt")
        );
        
        return ResponseEntity.ok(subtitles);
    }
    
    private void validateRental(UUID rentalId) {
        if (rentalId.equals(UUID.fromString("00000000-0000-0000-0000-000000000000"))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rental not found");
        }
    }
    
    private List<QualityOptionDTO> getQualityOptions() {
        return Arrays.asList(
                new QualityOptionDTO("4K", "3840x2160", 16000000L),
                new QualityOptionDTO("HD", "1920x1080", 8000000L),
                new QualityOptionDTO("SD", "1280x720", 3000000L),
                new QualityOptionDTO("Low", "854x480", 1500000L)
        );
    }
    
    private Map<String, String> getDrmInfo() {
        Map<String, String> drmInfo = new HashMap<>();
        drmInfo.put("provider", "widevine");
        drmInfo.put("license_url", "https://license.cine-sk.com/drm");
        return drmInfo;
    }
    
    private SubtitleDTO createSubtitle(String language, String code, String format) {
        return SubtitleDTO.builder()
                .language(language)
                .languageCode(code)
                .url("https://subtitles.cine-sk.com/" + code + ".vtt")
                .format(format)
                .build();
    }
    
    private double calculatePercentageWatched(Long currentTime, Long totalDuration) {
        return (double) currentTime / totalDuration * 100;
    }
}
