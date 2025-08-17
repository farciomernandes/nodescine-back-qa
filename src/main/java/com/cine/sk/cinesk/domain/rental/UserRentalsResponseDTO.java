package com.cine.sk.cinesk.domain.rental;

import com.cine.sk.cinesk.domain.rental.dto.PaginationDTO;
import com.cine.sk.cinesk.domain.rental.dto.RentalDTO;
import com.cine.sk.cinesk.domain.rental.dto.RentalsStatsDTO;

public class UserRentalsResponseDTO {
    public java.util.List<RentalDTO> rentals;
    public PaginationDTO pagination;
    public RentalsStatsDTO stats;
}
