package com.ecommerce.user.service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class OtpService {

    private final Map<String, OtpEntry> store = new ConcurrentHashMap<>();
    private static final long TTL_MS = 5 * 60 * 1000;

    private record OtpEntry(String code, long expiry) {}

    public String generateOtp(String email) {
        String code = String.format("%06d", new Random().nextInt(1_000_000));
        store.put(email, new OtpEntry(code, System.currentTimeMillis() + TTL_MS));
        // Real: send via SMS (Twilio) / Email (JavaMailSender)
        System.out.println("[2FA] OTP for " + email + ": " + code + " (expires in 5 min)");
        return code;
    }

    public boolean verifyOtp(String email, String code) {
        OtpEntry entry = store.get(email);
        if (entry == null) return false;
        if (System.currentTimeMillis() > entry.expiry()) {
            store.remove(email);
            return false;
        }
        boolean ok = entry.code().equals(code);
        if (ok) store.remove(email);
        return ok;
    }
}
