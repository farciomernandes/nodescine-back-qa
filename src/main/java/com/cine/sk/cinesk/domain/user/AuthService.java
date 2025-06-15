package com.cine.sk.cinesk.domain.user;

import com.cine.sk.cinesk.domain.movie.Role;
import com.cine.sk.cinesk.util.JwtUtil;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO authRequestDTO) {
        try {
            logger.info("Login attempt for email: {}", authRequestDTO.getEmail());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequestDTO.getEmail(),
                            authRequestDTO.getPassword()
                    )
            );

            logger.info("Successful authentication for email: {}", authRequestDTO.getEmail());
            UserEntity user = userRepository.findByEmail(authRequestDTO.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            String token = jwtUtil.generateToken(user);
            logger.debug("Token generated: {}", token);

            AuthResponseDTO response = new AuthResponseDTO();
            response.setToken(token);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            logger.warn("Invalid credentials for {}", authRequestDTO.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            logger.error("Internal error during login", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public void registerAdmin(RegisterDTO requestDTO) {
        if (userRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            logger.warn("Attempted registration with existing email address: {}", requestDTO.getEmail());
            throw new IllegalArgumentException("Email already exists");
        }
        UserEntity user = new UserEntity();
        user.setEmail(requestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        user.setName(requestDTO.getName());
        user.setRoles(Set.of(Role.ADMIN));
        userRepository.save(user);
    }

    public void registerCustomer(RegisterDTO requestDTO) {
        if (userRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            logger.warn("Attempted registration with existing email address: {}", requestDTO.getEmail());
            throw new IllegalArgumentException("Email already exists");
        }
        UserEntity user = new UserEntity();
        user.setEmail(requestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        user.setName(requestDTO.getName());
        user.setRoles(Set.of(Role.CUSTOMER));
        userRepository.save(user);
    }

    public ResponseEntity<Page<List<UserEntity>>> getAll(Pageable pageable) {
        return ResponseEntity.ok(userRepository.findAllActive(pageable));
    }
}

