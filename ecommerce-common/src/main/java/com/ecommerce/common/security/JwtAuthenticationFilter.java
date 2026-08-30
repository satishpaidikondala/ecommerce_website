package com.ecommerce.common.security;

import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) { this.jwtUtil = jwtUtil; }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        String xUserId = request.getHeader("X-User-Id");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (jwtUtil.isValid(token)) {
                var claims = jwtUtil.parseToken(token);
                String role = claims.get("role", String.class);
                var auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + (role != null ? role : "CUSTOMER"))));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } else if (xUserId != null) {
            // Trusted gateway header
            String role = request.getHeader("X-User-Role");
            var auth = new UsernamePasswordAuthenticationToken("gateway", null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + (role != null ? role : "CUSTOMER"))));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        chain.doFilter(request, response);
    }
}
