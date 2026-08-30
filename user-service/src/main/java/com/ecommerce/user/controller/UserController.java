package com.ecommerce.user.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.common.entity.User;
import com.ecommerce.common.entity.UserRole;
import com.ecommerce.user.dto.CreateUserRequest;
import com.ecommerce.user.dto.UserResponse;
import com.ecommerce.user.mapper.UserMapper;
import com.ecommerce.user.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest req) {
        User user = User.builder()
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .email(req.getEmail())
                .password(req.getPassword())
                .phone(req.getPhone())
                .build();
        User saved = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toResponse(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(UserMapper.toResponse(userService.getUserById(id)));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(UserMapper.toResponse(userService.getUserByEmail(email)));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsersByRoleAndActive(
            @RequestParam UserRole role, @RequestParam boolean active) {
        List<UserResponse> list = userService.getUsersByUserRoleAndActive(role, active)
                .stream().map(UserMapper::toResponse).toList();
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,
                                                    @RequestBody User updatedUser) {
        return ResponseEntity.ok(UserMapper.toResponse(userService.updateUser(id, updatedUser)));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<UserResponse> deactivateUser(@PathVariable Long id,
                                                        @RequestHeader("X-User-Id") Long currentUserId) {
        return ResponseEntity.ok(UserMapper.toResponse(userService.deactivateUser(id, currentUserId)));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        return ResponseEntity.ok(userService.forgotPassword(email));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestParam String token,
                                              @RequestParam String newPassword) {
        userService.resetPassword(token, newPassword);
        return ResponseEntity.ok().build();
    }
}
