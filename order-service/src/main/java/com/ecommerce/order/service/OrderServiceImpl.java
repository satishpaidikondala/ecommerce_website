package com.ecommerce.order.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ecommerce.common.entity.Order;
import com.ecommerce.common.entity.OrderStatus;
import com.ecommerce.order.dto.CreateOrderRequest;
import com.ecommerce.order.outbox.OutboxEvent;
import com.ecommerce.order.outbox.OutboxRepository;
import com.ecommerce.order.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public OrderServiceImpl(OrderRepository orderRepository, OutboxRepository outboxRepository, ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public Order createOrder(CreateOrderRequest req) {
        Order order = Order.builder()
                .orderNumber("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .status(OrderStatus.PENDING)
                .totalAmount(BigDecimal.ZERO)
                .shippingAddress(req.getShippingAddress())
                .shippingCity(req.getShippingCity())
                .shippingState(req.getShippingState())
                .shippingZip(req.getShippingZip())
                .shippingCountry(req.getShippingCountry())
                .userId(req.getUserId())
                .build();
        Order saved = orderRepository.save(order);
        try {
            String payload = objectMapper.writeValueAsString(new com.ecommerce.common.event.OrderCreatedEvent(
                    saved.getId(), saved.getOrderNumber(), req.getUserId(),
                    saved.getTotalAmount(), saved.getCreatedAt()));
            outboxRepository.save(OutboxEvent.builder()
                    .aggregateType("Order").aggregateId(saved.getId())
                    .eventType("OrderCreated").payload(payload).build());
        } catch (Exception e) { throw new RuntimeException("Outbox serialization failed", e); }
        return saved;
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
    }

    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    @Override
    @Transactional
    public Order updateOrderStatus(Long id, OrderStatus status) {
        Order order = getOrderById(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order cancelOrder(Long id) {
        Order order = getOrderById(id);
        if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED)
            throw new IllegalArgumentException("Cannot cancel shipped/delivered order");
        order.setStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    @Override
    public java.util.List<com.ecommerce.common.entity.OrderItem> getOrderItems(Long id) {
        return getOrderById(id).getOrderItems();
    }

    @Override
    public com.ecommerce.order.dto.OrderResponse getInvoice(Long id) {
        return com.ecommerce.order.mapper.OrderMapper.toResponse(getOrderById(id));
    }
}
