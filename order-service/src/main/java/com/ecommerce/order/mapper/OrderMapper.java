package com.ecommerce.order.mapper;

import java.util.stream.Collectors;
import com.ecommerce.common.entity.Order;
import com.ecommerce.order.dto.OrderItemResponse;
import com.ecommerce.order.dto.OrderResponse;

public class OrderMapper {
    private OrderMapper() {}
    public static OrderResponse toResponse(Order o) {
        String status = o.getStatus() != null ? o.getStatus().name() : null;
        java.util.List<OrderItemResponse> items = java.util.List.of();
        try {
            if (o.getOrderItems() != null) {
                items = o.getOrderItems().stream()
                        .map(i -> {
                            Long pid = null; String pname = null;
                            try { if (i.getProduct()!=null){ pid=i.getProduct().getId(); pname=i.getProduct().getName(); }} catch (org.hibernate.LazyInitializationException ex) {}
                            return new OrderItemResponse(pid,pname,i.getQuantity(), i.getPrice(), i.getSubtotal());
                        }).collect(Collectors.toList());
            }
        } catch (org.hibernate.LazyInitializationException e) {
            items = java.util.List.of();
        }
        return new OrderResponse(o.getId(), o.getOrderNumber(), status, o.getTotalAmount(),
                o.getUserId(), items, o.getCreatedAt());
    }
}
