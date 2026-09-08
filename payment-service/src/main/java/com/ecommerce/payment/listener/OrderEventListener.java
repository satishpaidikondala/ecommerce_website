package com.ecommerce.payment.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.ecommerce.common.event.OrderCreatedEvent;
import com.ecommerce.payment.config.RabbitConfig;

@Component
public class OrderEventListener {

    private static final Logger log = LoggerFactory.getLogger(OrderEventListener.class);

    @RabbitListener(queues = RabbitConfig.ORDER_CREATED_QUEUE)
    public void handleOrderCreated(OrderCreatedEvent event) {
        try {
            log.info("Received OrderCreated: orderId={}, orderNumber={}, userId={}, amount={}",
                    event.getOrderId(), event.getOrderNumber(), event.getUserId(), event.getTotalAmount());
            // Idempotency / saga: create pending payment if not exists
            // paymentService.createPendingPayment(event);
        } catch (Exception e) {
            log.error("Failed handling OrderCreated {}: {}", event.getOrderId(), e.getMessage(), e);
            throw e;
        }
    }
}
