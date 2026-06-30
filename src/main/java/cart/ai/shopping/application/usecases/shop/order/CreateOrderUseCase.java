/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.shop.order;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.model.identity.vos.UserId;
import cart.ai.shopping.domain.model.shop.Cart;
import cart.ai.shopping.domain.model.shop.Order;
import cart.ai.shopping.domain.model.shop.constants.OrderStatus;
import cart.ai.shopping.domain.model.shop.vos.OrderId;
import cart.ai.shopping.domain.model.shop.vos.OrderPlacedEvent;
import cart.ai.shopping.domain.ports.common.IncrementIdGeneratorPort;
import cart.ai.shopping.domain.ports.shop.CartRepositoryPort;
import cart.ai.shopping.domain.ports.shop.OrderPlacedEventPublisherPort;
import cart.ai.shopping.domain.ports.shop.OrderRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.Date;

import static cart.ai.shopping.domain.common.result.ResultError.BAD_REQUEST;
import static cart.ai.shopping.domain.common.result.ResultError.INTERNAL_ERROR;

/**
 * @author Roberto Díaz
 */
@RequiredArgsConstructor
@UseCase
public class CreateOrderUseCase {

    private final CartRepositoryPort cartRepositoryPort;
    private final OrderRepositoryPort orderRepositoryPort;
    private final IncrementIdGeneratorPort incrementIdGeneratorPort;
    private final OrderPlacedEventPublisherPort orderPlacedEventPublisherPort;

    public Result<Order> execute(UserId userId) {
        OrderId orderId = new OrderId(incrementIdGeneratorPort.generate(Order.class));

        if (orderRepositoryPort.find(orderId) != null) {
            return Result.error(INTERNAL_ERROR);
        }

        Cart cart = cartRepositoryPort.find(userId);
        if (cart == null || cart.getShoppingItems().isEmpty()) {
            return Result.error(BAD_REQUEST);
        }

        Order order = orderRepositoryPort.save(new Order(
                orderId,
                cart.getUserId(),
                cart.getShoppingItems(),
                OrderStatus.CREATED,
                new Date()));

        orderPlacedEventPublisherPort.publish(
                new OrderPlacedEvent(
                        orderId, order.getUserId(), order.getTotalPrice(), order.getStatus(), order.getCreateDate()));

        return Result.success(order);
    }
}
