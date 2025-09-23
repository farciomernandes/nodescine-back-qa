package com.cine.sk.cinesk.domain.transaction.rental.service;

import com.cine.sk.cinesk.domain.transaction.rental.dto.RentalRequestDTO;
import com.cine.sk.cinesk.domain.transaction.rental.dto.RentalResponseDTO;
import org.springframework.http.ResponseEntity;

public interface RentalService {
    /**
     * Process a movie rental transaction
     * @param rentalRequest the rental request details
     * @return the rental response with transaction details
     */
    ResponseEntity<RentalResponseDTO> processRental(RentalRequestDTO rentalRequest);
}
