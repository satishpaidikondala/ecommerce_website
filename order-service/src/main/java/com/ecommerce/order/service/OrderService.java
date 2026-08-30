package com.ecommerce.order.service;

import java.util.List;
import com.ecommerce.common.entity.Order;
import com.ecommerce.common.entity.OrderStatus;
import com.ecommerce.order.dto.CreateOrderRequest;
import com.ecommerce.order.dto.OrderResponse;

public interface OrderService {
    Order createOrder(CreateOrderRequest req);
    Order getOrderById(Long id);
    List<Order> getOrdersByUserId(Long userId);
    List<Order> getOrdersByStatus(OrderStatus status);
    Order updateOrderStatus(Long id, OrderStatus status);
    Order cancelOrder(Long id);
    java.util.List<com.ecommerce.common.entity.OrderItem> getOrderItems(Long id);
    OrderResponse getInvoice(Long id);
}
