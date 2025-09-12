package com.cine.sk.cinesk.domain.user;

import com.cine.sk.cinesk.domain.user.dto.*;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<UserProfileResponseDTO> getUserProfile(String userEmail);
    ResponseEntity<UpdateProfileResponseDTO> updateUserProfile(Long id, UpdateProfileRequestDTO request);
    ResponseEntity<UserStatsResponseDTO> getUserStats(String userEmail);
    void deleteUser(Long id);
    UserEntity findById(Long id);
}

