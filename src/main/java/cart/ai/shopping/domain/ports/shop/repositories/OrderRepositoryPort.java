package cart.ai.shopping.domain.ports.shop.repositories;

import cart.ai.shopping.domain.model.shop.Order;
import cart.ai.shopping.domain.model.shop.value.objects.OrderId;

import java.util.List;

public interface OrderRepositoryPort {

    void delete(OrderId orderId);

    Order find(OrderId orderId);

    List<Order> findAll();

    Order save(Order order);

}