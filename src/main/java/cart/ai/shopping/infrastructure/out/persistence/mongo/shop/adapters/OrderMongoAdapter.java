/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.out.persistence.mongo.shop.adapters;

import cart.ai.shopping.domain.model.shop.Order;
import cart.ai.shopping.domain.model.shop.vos.OrderId;
import cart.ai.shopping.domain.ports.shop.OrderRepositoryPort;
import cart.ai.shopping.infrastructure.out.persistence.mongo.shop.mappers.OrderMapper;
import cart.ai.shopping.infrastructure.out.persistence.mongo.shop.repositories.OrderMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Roberto Díaz
 */
@Component
@RequiredArgsConstructor
public class OrderMongoAdapter implements OrderRepositoryPort {

    private final OrderMongoRepository orderMongoRepository;

    @Override
    public void delete(OrderId orderId) {
        orderMongoRepository.deleteById(orderId.value());
    }

    @Override
    public Order find(OrderId orderId) {
        return orderMongoRepository.findById(orderId.value()).map(OrderMapper::toDomain).orElse(null);
    }

    @Override
    public List<Order> findAll() {

        return orderMongoRepository.findAll().stream().map(OrderMapper::toDomain).toList();
    }

    @Override
    public Order save(Order order) {
        return OrderMapper.toDomain(orderMongoRepository.save(OrderMapper.toDocument(order)));
    }
}
