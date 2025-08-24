package com.cine.sk.cinesk.domain.rental.service;

import com.cine.sk.cinesk.domain.rental.dto.*;
import org.springframework.http.ResponseEntity;

public interface UserRentalService {
    ResponseEntity<UserRentalsResponseDTO> getUserRentals(String userEmail, String status, int page, int limit);
    ResponseEntity<ActiveRentalsResponseDTO> getActiveRentals(String userEmail);
    ResponseEntity<Void> cancelRental(String userEmail, String rentalId, CancelRentalRequestDTO request);
}

