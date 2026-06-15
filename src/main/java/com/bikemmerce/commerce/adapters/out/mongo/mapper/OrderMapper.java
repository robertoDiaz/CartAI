package com.bikemmerce.commerce.adapters.out.mongo.mapper;

import com.bikemmerce.commerce.adapters.out.mongo.documents.dto.OrderDocument;
import com.bikemmerce.commerce.domain.model.Order;
import com.bikemmerce.commerce.domain.model.value.objects.CustomerId;
import com.bikemmerce.commerce.domain.model.value.objects.OrderId;

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
