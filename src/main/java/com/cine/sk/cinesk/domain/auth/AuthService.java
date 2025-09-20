package com.cine.sk.cinesk.domain.auth;

import com.cine.sk.cinesk.domain.auth.dto.AuthRequestDTO;
import com.cine.sk.cinesk.domain.auth.dto.AuthResponseDTO;
import com.cine.sk.cinesk.domain.auth.dto.ChangePasswordRequestDTO;
import com.cine.sk.cinesk.domain.user.User;
import com.cine.sk.cinesk.domain.user.UserRepository;
import com.cine.sk.cinesk.domain.user.dto.RegisterDTO;
import com.cine.sk.cinesk.infrastructure.jwt.JwtService;
import com.cine.sk.cinesk.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public ResponseEntity<AuthResponseDTO> register(RegisterDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Set.of(Role.CUSTOMER));

        User savedUser = userRepository.save(user);
        String jwtToken = jwtService.generateToken(savedUser);

        AuthResponseDTO authResponseDTO = AuthResponseDTO.builder()
                .token(jwtToken)
                .name(savedUser.getName())
                .roles(user.getRoles())
                .email(savedUser.getEmail())
                .build();

        return ResponseEntity.ok(authResponseDTO);
    }


    public ResponseEntity<AuthResponseDTO> login(@Valid AuthRequestDTO authRequestDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequestDTO.getEmail(),
                            authRequestDTO.getPassword()
                    )
            );

            User user = userRepository.findByEmail(authRequestDTO.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            String token = jwtUtil.generateToken(user);

            AuthResponseDTO response = AuthResponseDTO.builder()
                    .token(token)
                    .email(user.getEmail())
                    .name(user.getName())
                    .roles(user.getRoles()).build();

            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @Transactional
    public void changePassword(ChangePasswordRequestDTO request) {
        var authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }

        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Incorrect old password");
        }
        if (!request.newPassword().equals(request.confirmNewPassword())) {
            throw new IllegalArgumentException("New passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }
}