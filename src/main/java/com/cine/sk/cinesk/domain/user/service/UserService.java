package com.cine.sk.cinesk.domain.user.service;

import com.cine.sk.cinesk.domain.auth.enums.Role;
import com.cine.sk.cinesk.domain.auth.usertoken.UserTokenRepository;
import com.cine.sk.cinesk.domain.movie.Movie;
import com.cine.sk.cinesk.domain.movie.enhanced.EnhancedFilmDTO;
import com.cine.sk.cinesk.domain.movie.genre.GenreDTO;
import com.cine.sk.cinesk.domain.transaction.Transaction;
import com.cine.sk.cinesk.domain.transaction.TransactionRepository;
import com.cine.sk.cinesk.domain.user.User;
import com.cine.sk.cinesk.domain.user.UserRepository;
import com.cine.sk.cinesk.domain.user.dto.TransactionDTO;
import com.cine.sk.cinesk.domain.user.enums.UserStatus;
import com.cine.sk.cinesk.domain.user.dto.UpdateUserDTO;
import com.cine.sk.cinesk.domain.user.dto.UserDTO;
import com.cine.sk.cinesk.domain.util.ConverterUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.cine.sk.cinesk.domain.util.ConverterUtil.movieToEnhancedFilmDTO;

@Service
@RequiredArgsConstructor
public class UserService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;

    public ResponseEntity<UserDTO> getById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return ResponseEntity.ok(mapToDtoWithTransaction(user));
    }

    public long countUsers() {
        return userRepository.count();
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
        return ResponseEntity.ok(mapToDtoWithTransaction(saved));
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
            if (Role.MOVIE_DIRECTOR.name().equals(authName) || Role.MODERATOR.name().equals(authName)) {
                return true;
            }
        }
        return false;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    private TransactionDTO transactionToDTOsoVai(Transaction transaction){
        return TransactionDTO.builder().transactionId(transaction.getId()).movie(movieToEnhancedFilmDTO(transaction.getMovie())).build();
    }

    private UserDTO mapToDtoWithTransaction(User user) {
        List<TransactionDTO> transactionsDTO = transactionRepository.findAllByUser_Id(user.getId())
            .stream().map(this::transactionToDTOsoVai).toList();

        return UserDTO.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .status(user.getStatus())
            .createdAt(user.getCreatedAt())
            .complement(user.getComplement())
            .roles(user.getRoles())
            .walletId(user.getWalletId())
            .cpf(user.getCpf())
            .address(user.getAddress())
            .addressNumber(user.getAddressNumber())
            .phone(user.getPhone())
            .province(user.getProvince())
            .postalCode(user.getPostalCode())
            .updatedAt(user.getUpdatedAt() != null ? user.getUpdatedAt() : null)
            .transactions(transactionsDTO)
            .build();
    }

    private UserDTO mapToDto(User user) {
        return UserDTO.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .status(user.getStatus())
            .createdAt(user.getCreatedAt())
            .complement(user.getComplement())
            .roles(user.getRoles())
            .walletId(user.getWalletId())
            .cpf(user.getCpf())
            .address(user.getAddress())
            .addressNumber(user.getAddressNumber())
            .phone(user.getPhone())
            .province(user.getProvince())
            .postalCode(user.getPostalCode())
            .updatedAt(user.getUpdatedAt() != null ? user.getUpdatedAt() : null)
            .transactions(null)
            .build();
    }

    public ResponseEntity<List<UserDTO>> getAll() {
        List<User> users = userRepository.findAll();
        var usersDTO = users.stream().map(this::mapToDtoWithTransaction).toList();
        return ResponseEntity.ok(usersDTO);
    }
}
