package com.cine.sk.cinesk.domain.user;

import com.cine.sk.cinesk.domain.user.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public ResponseEntity<UserProfileResponseDTO> getUserProfile(String userEmail) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDTO userDTO = mapToUserDTO(user);
        UserProfileResponseDTO response = new UserProfileResponseDTO();
        response.user = userDTO;

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<UpdateProfileResponseDTO> updateUserProfile(String userEmail, UpdateProfileRequestDTO request) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update user fields
        user.setName(request.getName());
        // Note: In a real implementation, preferences would be stored in database

        userRepository.save(user);

        UserDTO userDTO = mapToUserDTO(user);
        userDTO.setUpdatedAt(LocalDateTime.now().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT));
        userDTO.setPreferences(request.getPreferences());

        UpdateProfileResponseDTO response = new UpdateProfileResponseDTO();
        response.user = userDTO;
        response.message = "Perfil atualizado com sucesso";

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<UserStatsResponseDTO> getUserStats(String userEmail) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Mock stats data - in real implementation would query from database
        MonthlyUsageDTO monthlyUsage = new MonthlyUsageDTO();
        Map<String, MonthlyUsageDTO.MonthEntry> entries = new HashMap<>();
        entries.put("january", MonthlyUsageDTO.MonthEntry.builder()
                .rentals(8)
                .watch_time(720)
                .build());
        monthlyUsage.setEntries(entries);

        StatsDTO stats = StatsDTO.builder()
                .totalRentals(25)
                .activeRentals(3)
                .totalWatchTime(2340)
                .favoriteGenre("Drama")
                .moviesCompleted(18)
                .lastActivity(LocalDateTime.now().minusHours(2).atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT))
                .monthlyUsage(monthlyUsage)
                .build();

        UserStatsResponseDTO response = new UserStatsResponseDTO();
        response.stats = stats;

        return ResponseEntity.ok(response);
    }

    private UserDTO mapToUserDTO(UserEntity user) {
        SubscriptionDTO subscription = SubscriptionDTO.builder()
                .type("premium")
                .since(user.getCreatedAt().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT))
                .expiresAt(user.getCreatedAt().plusYears(1).atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT))
                .build();

        PreferencesDTO preferences = PreferencesDTO.builder()
                .language("pt-BR")
                .quality("1080p")
                .notifications(true)
                .build();

        return UserDTO.builder()
                .uuid(user.getUuid().toString())
                .name(user.getName())
                .email(user.getEmail())
                .avatar("https://...")
                .createdAt(user.getCreatedAt().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT))
                .subscription(subscription)
                .preferences(preferences)
                .build();
    }
}
