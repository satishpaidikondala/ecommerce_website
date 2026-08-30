package com.ecommerce.user.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.ecommerce.common.entity.User;
import com.ecommerce.common.entity.UserRole;
import com.ecommerce.user.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User newUser = User.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .active(true)
                .userRole(UserRole.CUSTOMER)
                .build();

        return userRepository.save(newUser);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
    }

    @Override
    public List<User> getUsersByUserRoleAndActive(UserRole userRole, boolean active) {
        return userRepository.findByUserRoleAndActive(userRole, active);
    }

    @Override
    public User updateUser(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setPhone(updatedUser.getPhone());

        return userRepository.save(existingUser);
    }

    @Override
    public User deactivateUser(Long id, Long currentUserId) {
        if (id.equals(currentUserId)) {
            throw new IllegalArgumentException("You cannot deactivate your own account");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        user.setActive(false);
        return userRepository.save(user);
    }

    @Override
    public String forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

        String token = UUID.randomUUID().toString();

        user.setResetToken(token);
        user.setTokenExpiry(LocalDateTime.now().plusHours(1));

        userRepository.save(user);

        return token;
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (user.getTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setTokenExpiry(null);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    @Override
    public User updatePassword(Long id, String newPassword) {
        User user = getUserById(id);
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    @Override
    public User updateAddress(Long id, User addressInfo) {
        User user = getUserById(id);
        user.setAddress(addressInfo.getAddress());
        user.setCity(addressInfo.getCity());
        user.setState(addressInfo.getState());
        user.setZipCode(addressInfo.getZipCode());
        user.setCountry(addressInfo.getCountry());
        return userRepository.save(user);
    }
}