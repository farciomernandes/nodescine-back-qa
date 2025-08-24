package com.cine.sk.cinesk.domain.rental.service;

import com.cine.sk.cinesk.domain.rental.dto.RentalRequestDTO;
import com.cine.sk.cinesk.domain.rental.dto.RentalResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RentalServiceImpl implements RentalService {
    
    @Override
    public ResponseEntity<RentalResponseDTO> processRental(RentalRequestDTO rentalRequest) {
        // In a real implementation:
        // 1. Verify that the movie exists
        // 2. Verify that the user exists and has permission to rent
        // 3. Check if the user already has an active rental for this movie
        // 4. Process the payment
        // 5. Create and save the rental record
        // 6. Generate streaming URL
        
        try {
            // For demo purposes, we're generating data that would normally come from the database
            UUID rentalId = UUID.randomUUID();
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expiresAt = now.plusHours(rentalRequest.getDurationHours());
            String paymentId = "pmt_" + UUID.randomUUID().toString().substring(0, 8);
            
            // Generate a stream URL (in real implementation this would be secure and time-limited)
            String streamUrl = "https://stream.cine-sk.com/movies/" + 
                               rentalRequest.getFilmId() + "?token=xyz123";
            
            RentalResponseDTO response = RentalResponseDTO.builder()
                    .rentalId(rentalId)
                    .filmId(rentalRequest.getFilmId())
                    .userId(rentalRequest.getUserId())
                    .rentedAt(now)
                    .expiresAt(expiresAt)
                    .paymentId(paymentId)
                    .status("active")
                    .streamUrl(streamUrl)
                    .message("Rental processed successfully. You can now start streaming the movie.")
                    .build();
            
            // In a real implementation, save the rental entity to the database
            
            log.info("Processed rental {} for user {} for movie {}", 
                    rentalId, rentalRequest.getUserId(), rentalRequest.getFilmId());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error processing rental: {}", e.getMessage(), e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, 
                    "Failed to process rental: " + e.getMessage(), e);
        }
    }
}
