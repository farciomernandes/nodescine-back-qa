package com.cine.sk.cinesk.domain.user;

import com.cine.sk.cinesk.domain.auth.enums.Role;
import com.cine.sk.cinesk.domain.auth.usertoken.UserTokenRepository;
import com.cine.sk.cinesk.domain.user.dto.UpdateUserDTO;
import com.cine.sk.cinesk.domain.user.dto.UserDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;

    public ResponseEntity<UserDTO> getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return ResponseEntity.ok(mapToDto(user));
    }

    public ResponseEntity<UserDTO> updateById(Long id, UpdateUserDTO request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!isSelfOrStaff(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is deactivated");
        }

        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
        }
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            if (!request.getEmail().equalsIgnoreCase(user.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
            }
            user.setEmail(request.getEmail());
        }
        if (request.getAvatar() != null && !request.getAvatar().isBlank()) {
            user.setAvatar(request.getAvatar());
        }
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            user.setRoles(request.getRoles());
        }
        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
        }

        User saved = userRepository.save(user);
        return ResponseEntity.ok(mapToDto(saved));
    }

    @Transactional
    public ResponseEntity<Void> deleteById(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        User user = userOpt.get();

        if (!isSelfOrStaff(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        if (user.getStatus() != UserStatus.INACTIVE) {
            user.setStatus(UserStatus.INACTIVE);
            userTokenRepository.deactivateAllUserTokens(user.getEmail());
            userRepository.save(user);
        }
        
        return ResponseEntity.noContent().build();
    }

    private boolean isSelfOrStaff(User targetUser) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return false;
        String currentEmail = auth.getName();
        if (targetUser.getEmail().equalsIgnoreCase(currentEmail)) return true;
        for (GrantedAuthority authority : auth.getAuthorities()) {
            String authName = authority.getAuthority();
            if (Role.ADMIN.name().equals(authName) || Role.MODERATOR.name().equals(authName)) {
                return true;
            }
        }
        return false;
    }

    private UserDTO mapToDto(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt() != null ? user.getUpdatedAt() : null)
                .subscription(null)
                .build();
    }
}
