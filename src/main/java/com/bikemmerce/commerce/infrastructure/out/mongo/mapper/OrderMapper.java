package com.bikemmerce.commerce.infrastructure.out.mongo.mapper;

import com.bikemmerce.commerce.domain.model.Order;
import com.bikemmerce.commerce.domain.model.value.objects.CustomerId;
import com.bikemmerce.commerce.domain.model.value.objects.OrderId;
import com.bikemmerce.commerce.infrastructure.out.mongo.documents.dto.OrderDocument;

public class OrderMapper {

    public static OrderDocument toDocument(Order order) {
        return new OrderDocument(
                order.getOrderId().value(), order.getCustomerId().value(),
                order.getShoppingItems().stream().map(ShoppingItemMapper::toDocument).toList(),
                order.getStatus(), order.getCreateDate());
    }

    public static Order toDomain(OrderDocument orderDocument) {
        return new Order(
                new OrderId(orderDocument.getOrderId()), new CustomerId(orderDocument.getCustomerId()),
                orderDocument.getShoppingItems().stream().map(ShoppingItemMapper::toDomain).toList(),
                orderDocument.getOrderStatus(), orderDocument.getCreateDate());
    }

}
