package com.ecommerce.user.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ecommerce.user.dto.AuthResponse;
import com.ecommerce.user.dto.LoginRequest;
import com.ecommerce.user.service.AuthService;
import com.ecommerce.user.service.OtpService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final OtpService otpService;

    public AuthController(AuthService authService, OtpService otpService) {
        this.authService = authService;
        this.otpService = otpService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestParam String email) {
        String code = otpService.generateOtp(email);
        return ResponseEntity.ok("OTP sent to " + email + " code: " + code + " (check server log, expires in 5 min)");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String code) {
        boolean ok = otpService.verifyOtp(email, code);
        return ok ? ResponseEntity.ok("OTP verified") : ResponseEntity.status(401).body("Invalid or expired OTP");
    }
}
