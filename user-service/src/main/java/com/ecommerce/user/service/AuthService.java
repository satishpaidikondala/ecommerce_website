package com.ecommerce.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ecommerce.common.entity.User;
import com.ecommerce.user.dto.AuthResponse;
import com.ecommerce.user.dto.LoginRequest;
import com.ecommerce.user.repository.UserRepository;
import com.ecommerce.common.security.JwtUtil;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        if (!user.isActive()) throw new IllegalArgumentException("Account deactivated");
        String token = jwtUtil.generateToken(user.getId(), user.getEmail(),
                user.getUserRole()!=null?user.getUserRole().name():"CUSTOMER");
        return new AuthResponse(token, user.getId(), user.getEmail(),
                user.getUserRole()!=null?user.getUserRole().name():"CUSTOMER");
    }
}
