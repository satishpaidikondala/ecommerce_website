package com.ecommerce.order.service;

import java.util.List;
import com.ecommerce.common.entity.Order;
import com.ecommerce.common.entity.OrderStatus;
import com.ecommerce.order.dto.CreateOrderRequest;

public interface OrderService {
    Order createOrder(CreateOrderRequest req);
    Order getOrderById(Long id);
    List<Order> getOrdersByUserId(Long userId);
    List<Order> getOrdersByStatus(OrderStatus status);
    Order updateOrderStatus(Long id, OrderStatus status);
}
