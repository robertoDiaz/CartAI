/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.out.persistence.mongo.shop.mappers;

import cart.ai.shopping.domain.model.identity.vos.UserId;
import cart.ai.shopping.domain.model.shop.Order;
import cart.ai.shopping.domain.model.shop.vos.OrderId;
import cart.ai.shopping.infrastructure.out.persistence.mongo.shop.documents.OrderDocument;

/**
 * @author Roberto Díaz
 */
public class OrderMapper {

    public static OrderDocument toDocument(Order order) {
        return OrderDocument.builder()
                .orderId(order.getOrderId().value())
                .customerId(order.getUserId().value())
                .shoppingItems(order.getShoppingItems().stream()
                        .map(ShoppingItemMapper::toDocument)
                        .toList())
                .orderStatus(order.getStatus())
                .createDate(order.getCreateDate())
                .build();
    }

    public static Order toDomain(OrderDocument orderDocument) {
        return new Order(
                new OrderId(orderDocument.getOrderId()),
                new UserId(orderDocument.getCustomerId()),
                orderDocument.getShoppingItems().stream()
                        .map(ShoppingItemMapper::toDomain)
                        .toList(),
                orderDocument.getOrderStatus(),
                orderDocument.getCreateDate()
        );
    }
}
