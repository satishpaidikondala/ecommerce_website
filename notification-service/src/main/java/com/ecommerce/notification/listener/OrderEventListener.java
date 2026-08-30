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
        log.info("[{}] Notification received OrderCreated: {}", event.getOrderNumber(), event.getOrderId());
        notificationService.sendOrderConfirmation(event.getUserId(), event.getOrderNumber());
    }
}
