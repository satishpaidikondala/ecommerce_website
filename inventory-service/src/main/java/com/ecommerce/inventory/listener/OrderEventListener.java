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

    @RabbitListener(queues = "order.created.queue")
    public void onOrderCreated(OrderCreatedEvent event) {
        // Saga step 1: reserve stock (simplified: assume 1 unit per order for Gram Setu produce)
        boolean ok = inventoryService.reserveStock(event.getOrderId(), 1);
        if (ok) log.info("Saga: stock reserved for order {}", event.getOrderNumber());
        else log.warn("Saga: stock insufficient for order {} — compensate: cancel order", event.getOrderNumber());
        // Real saga: publish StockReserved / StockFailed event for next step (payment)
    }
}
