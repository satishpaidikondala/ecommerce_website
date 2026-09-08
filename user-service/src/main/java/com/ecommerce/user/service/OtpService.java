package com.ecommerce.user.service;

import java.time.Duration;
import java.util.Random;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class OtpService {

    private final StringRedisTemplate redisTemplate;
    private final JavaMailSender mailSender;
    private final String fromEmail;
    private static final long TTL_SECONDS = 300;

    public OtpService(StringRedisTemplate redisTemplate, JavaMailSender mailSender,
                      @Value("${spring.mail.username:noreply@ecommerce.com}") String fromEmail) {
        this.redisTemplate = redisTemplate;
        this.mailSender = mailSender;
        this.fromEmail = fromEmail;
    }

    public String generateOtp(String email) {
        String code = String.format("%06d", new Random().nextInt(1_000_000));
        redisTemplate.opsForValue().set(email, code, Duration.ofSeconds(TTL_SECONDS));
        // Prod: real email via SMTP (Gmail SES) — same SETEX before send
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(fromEmail);
            msg.setTo(email);
            msg.setSubject("Your GramSetu OTP");
            msg.setText("Your OTP is: " + code + " (expires in 5 minutes)");
            mailSender.send(msg);
        } catch (Exception e) {
            // Dev fallback: log if SMTP not configured
            System.out.println("[2FA] OTP for " + email + ": " + code + " (mail failed: " + e.getMessage() + ")");
        }
        return code;
    }

    public boolean verifyOtp(String email, String code) {
        String stored = redisTemplate.opsForValue().get(email);
        if (stored == null) return false;
        boolean ok = stored.equals(code);
        if (ok) redisTemplate.delete(email);
        return ok;
    }
}
