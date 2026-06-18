package com.bikemmerce.commerce.application.usecases.shop.order;

import com.bikemmerce.commerce.application.annotations.UseCase;
import com.bikemmerce.commerce.domain.model.constants.OrderStatus;
import com.bikemmerce.commerce.domain.model.security.value.objects.UserId;
import com.bikemmerce.commerce.domain.model.shop.Cart;
import com.bikemmerce.commerce.domain.model.shop.Order;
import com.bikemmerce.commerce.domain.model.shop.value.objects.OrderId;
import com.bikemmerce.commerce.domain.model.shop.value.objects.OrderPlacedEvent;
import com.bikemmerce.commerce.domain.ports.common.IncrementIdGeneratorPort;
import com.bikemmerce.commerce.domain.ports.shop.CartRepositoryPort;
import com.bikemmerce.commerce.domain.ports.shop.OrderRepositoryPort;
import com.bikemmerce.commerce.domain.ports.shop.events.OrderPlacedEventPublisherPort;
import com.bikemmerce.commerce.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Date;

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
            return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        Cart cart = cartRepositoryPort.find(userId);

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
