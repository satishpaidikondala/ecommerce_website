package com.ecommerce.notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    public void sendOrderConfirmation(Long userId, String orderNumber) {
        // Real: integrate JavaMailSender + SMS gateway (Twilio)
        log.info("[Notification] Email to userId={} — Order {} confirmed! SMS sent.", userId, orderNumber);
    }
}
