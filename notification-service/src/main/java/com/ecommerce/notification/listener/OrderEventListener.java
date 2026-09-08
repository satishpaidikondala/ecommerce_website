package com.ecommerce.notification.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.ecommerce.common.event.OrderCreatedEvent;
import com.ecommerce.notification.config.RabbitConfig;
import com.ecommerce.notification.service.NotificationService;

@Component
public class OrderEventListener {
    private static final Logger log = LoggerFactory.getLogger(OrderEventListener.class);
    private final NotificationService notificationService;

    public OrderEventListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = RabbitConfig.ORDER_CREATED_QUEUE)
    public void onOrderCreated(OrderCreatedEvent event) {
        try {
            log.info("Notification received OrderCreated: orderId={}, orderNumber={}, userId={}", event.getOrderId(), event.getOrderNumber(), event.getUserId());
            notificationService.sendOrderConfirmation(event.getUserId(), event.getOrderNumber());
        } catch (Exception e) {
            log.error("Notification handling failed for order {}: {}", event.getOrderNumber(), e.getMessage(), e);
            // swallow to prevent infinite requeue; could send to DLQ
        }
    }
}
