package com.ecommerce.user.dto;

public class AuthResponse {
    private String token; private Long userId; private String email; private String role;
    public AuthResponse() {}
    public AuthResponse(String token, Long userId, String email, String role) {
        this.token=token; this.userId=userId; this.email=email; this.role=role;
    }
    public String getToken() { return token; } public void setToken(String t) { this.token=t; }
    public Long getUserId() { return userId; } public void setUserId(Long u) { this.userId=u; }
    public String getEmail() { return email; } public void setEmail(String e) { this.email=e; }
    public String getRole() { return role; } public void setRole(String r) { this.role=r; }
}
