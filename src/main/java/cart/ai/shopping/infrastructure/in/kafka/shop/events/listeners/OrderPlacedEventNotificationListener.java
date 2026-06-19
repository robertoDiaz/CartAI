/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.kafka.shop.events.listeners;

import cart.ai.shopping.domain.model.shop.Cart;
import cart.ai.shopping.domain.model.shop.Customer;
import cart.ai.shopping.domain.model.shop.vos.CustomerAddedEvent;
import cart.ai.shopping.domain.model.shop.vos.OrderPlacedEvent;
import cart.ai.shopping.domain.ports.shop.CartRepositoryPort;
import cart.ai.shopping.domain.ports.shop.CustomerRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author Roberto Díaz
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OrderPlacedEventNotificationListener {

    private final CustomerRepositoryPort customerRepositoryPort;
    private final CartRepositoryPort cartRepositoryPort;

    @KafkaListener(topics = "orders-topic", groupId = "email-notification")
    public void notify(OrderPlacedEvent orderPlacedEvent) {

        Customer customer = customerRepositoryPort.findByCustomerId(orderPlacedEvent.userId());

        log.info("email sent for {} to {}", orderPlacedEvent.orderId(), customer.email().value());
    }

    @KafkaListener(topics = "orders-topic", groupId = "post-processor")
    public void postProcess(CustomerAddedEvent event) {
        Cart cart = cartRepositoryPort.find(event.userId());

        if (cart == null) {
            return;
        }

        cart.clearItems();

        cartRepositoryPort.save(cart);
    }
}
