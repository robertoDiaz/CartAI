package com.bikemmerce.commerce.infrastructure.out.mongo.adapters;

import com.bikemmerce.commerce.domain.model.Order;
import com.bikemmerce.commerce.domain.model.value.objects.OrderId;
import com.bikemmerce.commerce.domain.ports.OrderRepositoryPort;
import com.bikemmerce.commerce.infrastructure.out.mongo.OrderMongoRepository;
import com.bikemmerce.commerce.infrastructure.out.mongo.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

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