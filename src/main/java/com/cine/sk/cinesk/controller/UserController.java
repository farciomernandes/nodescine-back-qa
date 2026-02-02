package com.cine.sk.cinesk.controller;

import com.cine.sk.cinesk.domain.user.service.UserService;
import com.cine.sk.cinesk.domain.user.dto.UpdateUserDTO;
import com.cine.sk.cinesk.domain.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAll() {
        return userService.getAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUserById(@PathVariable Long id, @RequestBody UpdateUserDTO request) {
        return userService.updateById(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        return userService.deleteById(id);
    }
}