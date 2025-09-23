package com.cine.sk.cinesk.domain.transaction.rental.service;

import com.cine.sk.cinesk.domain.transaction.rental.dto.*;
import com.cine.sk.cinesk.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserRentalServiceImpl implements UserRentalService {

    private final UserRepository userRepository;

    @Override
    public ResponseEntity<UserRentalsResponseDTO> getUserRentals(String userEmail, String status, int page, int limit) {
        // Validate user exists
        userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Mock rental data - in real implementation would query from database
        List<RentalDTO> allRentals = createMockRentals();

        // Filter by status if specified
        List<RentalDTO> filteredRentals = allRentals;
        if (!"all".equals(status)) {
            filteredRentals = allRentals.stream()
                    .filter(rental -> status.equals(rental.getStatus()))
                    .collect(Collectors.toList());
        }

        // Apply pagination
        int totalItems = filteredRentals.size();
        int totalPages = (int) Math.ceil((double) totalItems / limit);
        int startIndex = (page - 1) * limit;
        int endIndex = Math.min(startIndex + limit, totalItems);

        List<RentalDTO> paginatedRentals = filteredRentals.subList(startIndex, endIndex);

        PaginationDTO pagination = PaginationDTO.builder()
                .currentPage(page)
                .totalPages(totalPages)
                .totalItems(totalItems)
                .build();

        RentalsStatusDTO stats = RentalsStatusDTO.builder()
                .totalRentals(25)
                .activeRentals(3)
                .totalSpent("R$ 287,50")
                .build();

        UserRentalsResponseDTO response = new UserRentalsResponseDTO();
        response.rentals = paginatedRentals;
        response.pagination = pagination;
        response.stats = stats;

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ActiveRentalsResponseDTO> getActiveRentals(String userEmail) {
        userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<RentalDTO> activeRentals = createMockRentals().stream()
                .filter(rental -> "active".equals(rental.getStatus()))
                .map(rental -> {
                    rental.setTimeRemaining("5 dias, 14 horas");
                    rental.setStreamUrl("https://secure-stream.nordescine.com/" + rental.getId() + "/playlist.m3u8");
                    return rental;
                })
                .collect(Collectors.toList());

        ActiveRentalsResponseDTO response = new ActiveRentalsResponseDTO();
        response.active_rentals = activeRentals;
        response.total_active = activeRentals.size();

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> cancelRental(String userEmail, String rentalId, CancelRentalRequestDTO request) {
        userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<String> validReasons = Arrays.asList("changed_mind", "technical_issues", "not_satisfied", "other");
        if (!validReasons.contains(request.getReason())) {
            throw new IllegalArgumentException("Invalid cancellation reason");
        }

        return ResponseEntity.noContent().build();
    }

    private List<RentalDTO> createMockRentals() {
        FilmDTO film = FilmDTO.builder()
                .id("1")
                .title("O Último Samurai")
                .posterUrl("https://...")
                .duration(154)
                .build();

        WatchProgressDTO progress = WatchProgressDTO.builder()
                .currentTime(1840)
                .percentage(19.9)
                .lastWatched(LocalDateTime.now().minusHours(1).atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT))
                .build();

        RentalDTO rental = RentalDTO.builder()
                .id("rental_456")
                .film(film)
                .rentedAt(LocalDateTime.now().minusDays(1).atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT))
                .expiresAt(LocalDateTime.now().plusDays(6).atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT))
                .price("R$ 12,90")
                .status("active")
                .watchProgress(progress)
                .build();

        return Arrays.asList(rental);
    }
}
