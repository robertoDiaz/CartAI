package cart.ai.shopping.infrastructure.out.persistence.mongo.shop.adapters;

import cart.ai.shopping.domain.model.shop.Order;
import cart.ai.shopping.domain.model.shop.value.objects.OrderId;
import cart.ai.shopping.domain.ports.shop.repositories.OrderRepositoryPort;
import cart.ai.shopping.infrastructure.out.persistence.mongo.shop.OrderMongoRepository;
import cart.ai.shopping.infrastructure.out.persistence.mongo.shop.mapper.OrderMapper;
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