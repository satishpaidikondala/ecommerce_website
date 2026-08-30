package com.ecommerce.order.mapper;

import java.util.stream.Collectors;
import com.ecommerce.common.entity.Order;
import com.ecommerce.order.dto.OrderItemResponse;
import com.ecommerce.order.dto.OrderResponse;

public class OrderMapper {
    private OrderMapper() {}
    public static OrderResponse toResponse(Order o) {
        return new OrderResponse(o.getId(), o.getOrderNumber(), o.getStatus().name(), o.getTotalAmount(),
                o.getUser()!=null?o.getUser().getId():null,
                o.getOrderItems()==null?java.util.List.of():o.getOrderItems().stream()
                        .map(i -> new OrderItemResponse(
                                i.getProduct()!=null?i.getProduct().getId():null,
                                i.getProduct()!=null?i.getProduct().getName():null,
                                i.getQuantity(), i.getPrice(), i.getSubtotal()))
                        .collect(Collectors.toList()),
                o.getCreatedAt());
    }
}
