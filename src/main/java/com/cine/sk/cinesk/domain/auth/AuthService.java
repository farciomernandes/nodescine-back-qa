package com.cine.sk.cinesk.domain.auth;

import com.cine.sk.cinesk.domain.auth.dto.AuthRequestDTO;
import com.cine.sk.cinesk.domain.auth.dto.AuthResponseDTO;
import com.cine.sk.cinesk.domain.auth.dto.ChangePasswordRequestDTO;
import com.cine.sk.cinesk.domain.auth.enums.Role;
import com.cine.sk.cinesk.domain.transaction.Transaction;
import com.cine.sk.cinesk.domain.transaction.TransactionService;
import com.cine.sk.cinesk.domain.user.User;
import com.cine.sk.cinesk.domain.user.UserRepository;
import com.cine.sk.cinesk.domain.user.dto.TransactionDTO;
import com.cine.sk.cinesk.domain.user.enums.UserStatus;
import com.cine.sk.cinesk.domain.user.dto.CustomerRegisterDTO;
import com.cine.sk.cinesk.domain.user.dto.RegisterDTO;
import com.cine.sk.cinesk.domain.user.dto.UserDTO;
import com.cine.sk.cinesk.infrastructure.jwt.JwtService;
import com.cine.sk.cinesk.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final TransactionService transactionService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public ResponseEntity<String> register(RegisterDTO request) {
        var existingOpt = userRepository.findByEmail(request.getEmail());
        User user;
        if (existingOpt.isPresent()) {
            User existing = existingOpt.get();
            if (existing.getStatus() == UserStatus.INACTIVE) {
                existing.setName(request.getName());
                existing.setPassword(passwordEncoder.encode(request.getPassword()));
                existing.setRoles(request.getRoles());
                existing.setStatus(UserStatus.ACTIVE);
                existing.setAvatar(request.getAvatar());
                existing.setCpf(request.getCpf());
                existing.setAddress(request.getAddress());
                existing.setAddressNumber(request.getAddressNumber());
                existing.setComplement(request.getComplement());
                existing.setPostalCode(request.getPostalCode());
                existing.setPhone(request.getPhone());
                existing.setProvince(request.getProvince());
                user = existing;
            } else {
                throw new IllegalArgumentException("Email already in use");
            }
        } else {
            user = new User();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRoles(request.getRoles());
            user.setStatus(UserStatus.ACTIVE);
            user.setAvatar(request.getAvatar());
            user.setCpf(request.getCpf());
            user.setAddress(request.getAddress());
            user.setAddressNumber(request.getAddressNumber());
            user.setComplement(request.getComplement());
            user.setPostalCode(request.getPostalCode());
            user.setPhone(request.getPhone());
            user.setProvince(request.getProvince());
        }


        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created");
    }


    public ResponseEntity<String> registerCustomer(CustomerRegisterDTO request) {
        var existingOpt = userRepository.findByEmail(request.getEmail());
        User user;
        if (existingOpt.isPresent()) {
            User existing = existingOpt.get();
            if (existing.getStatus() == UserStatus.INACTIVE) {
                existing.setName(request.getName());
                existing.setPassword(passwordEncoder.encode(request.getPassword()));
                existing.setRoles(Set.of(Role.CUSTOMER));
                existing.setStatus(UserStatus.ACTIVE);
                existing.setAvatar(request.getAvatar());
                existing.setCpf(request.getCpf());
                existing.setAddress(request.getAddress());
                existing.setAddressNumber(request.getAddressNumber());
                existing.setComplement(request.getComplement());
                existing.setPostalCode(request.getPostalCode());
                existing.setPhone(request.getPhone());
                existing.setProvince(request.getProvince());
                user = existing;
            } else {
                throw new IllegalArgumentException("Email already in use");
            }
        } else {
            user = new User();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRoles(Set.of(Role.CUSTOMER));
            user.setStatus(UserStatus.ACTIVE);
            user.setCpf(request.getCpf());
            user.setAddress(request.getAddress());
            user.setAddressNumber(request.getAddressNumber());
            user.setComplement(request.getComplement());
            user.setPostalCode(request.getPostalCode());
            user.setPhone(request.getPhone());
            user.setProvince(request.getProvince());
        }

        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created");
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
        var authentication = SecurityContextHolder.getContext().getAuthentication();
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

    public ResponseEntity<UserDTO> me() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        List<TransactionDTO> transactions = user.getTransactions()
                .stream()
                .map(this::transactionToDTO)
                .collect(Collectors.toList());


        var dto = UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .status(user.getStatus())
                .cpf(user.getCpf())
                .address(user.getAddress())
                .addressNumber(user.getAddressNumber())
                .complement(user.getComplement())
                .postalCode(user.getPostalCode())
                .phone(user.getPhone())
                .province(user.getProvince())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .transactions(transactions)
                .build();
        return ResponseEntity.ok(dto);
    }

    private TransactionDTO transactionToDTO(Transaction transaction){
        return TransactionDTO.builder().transactionId(transaction.getId()).movie(transaction.getMovie()).build();
    }
}
