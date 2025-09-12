package com.cine.sk.cinesk.controller;

import com.cine.sk.cinesk.domain.user.UserEntity;
import com.cine.sk.cinesk.domain.user.UserService;
import com.cine.sk.cinesk.domain.user.dto.UpdateProfileRequestDTO;
import com.cine.sk.cinesk.domain.user.dto.UpdateProfileResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UserEntity> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<UpdateProfileResponseDTO> updateUser(@PathVariable Long id, @RequestBody UpdateProfileRequestDTO user) {
        return ResponseEntity.ok(userService.updateUserProfile(id, user).getBody());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}