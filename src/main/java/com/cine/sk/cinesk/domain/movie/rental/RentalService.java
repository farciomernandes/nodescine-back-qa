package com.cine.sk.cinesk.domain.movie.rental;

import org.springframework.http.ResponseEntity;

public interface RentalService {
    /**
     * Process a movie rental transaction
     * @param rentalRequest the rental request details
     * @return the rental response with transaction details
     */
    ResponseEntity<RentalResponseDTO> processRental(RentalRequestDTO rentalRequest);
}
