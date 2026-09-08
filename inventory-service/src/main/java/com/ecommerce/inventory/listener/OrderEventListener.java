package com.ecommerce.inventory.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.ecommerce.common.event.OrderCreatedEvent;
import com.ecommerce.inventory.service.InventoryService;

@Component
public class OrderEventListener {
    private static final Logger log = LoggerFactory.getLogger(OrderEventListener.class);
    private final InventoryService inventoryService;
    public OrderEventListener(InventoryService inventoryService) { this.inventoryService = inventoryService; }

    @RabbitListener(queues = com.ecommerce.inventory.config.RabbitConfig.ORDER_CREATED_QUEUE)
    public void onOrderCreated(OrderCreatedEvent event) {
        // OrderCreatedEvent currently carries no product list; inventory reservation
        // would require productId/qty. Log event and skip phantom reservation.
        log.info("Inventory received OrderCreated: orderId={}, orderNumber={} — no product items in event, skipping stock reservation", event.getOrderId(), event.getOrderNumber());
        // TODO: extend OrderCreatedEvent to include List<OrderItem> and iterate reserveStock(productId, qty)
    }
}
