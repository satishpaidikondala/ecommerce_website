package com.ecommerce.order.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ecommerce.common.entity.Order;
import com.ecommerce.common.entity.OrderStatus;
import com.ecommerce.common.entity.User;
import com.ecommerce.order.dto.CreateOrderRequest;
import com.ecommerce.order.repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
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
                .user(User.builder().id(req.getUserId()).build())
                .build();
        return orderRepository.save(order);
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
}
