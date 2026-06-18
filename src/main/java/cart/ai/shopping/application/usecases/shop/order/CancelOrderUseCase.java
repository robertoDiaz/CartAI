package cart.ai.shopping.application.usecases.shop.order;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.domain.model.shop.Order;
import cart.ai.shopping.domain.model.shop.value.objects.OrderId;
import cart.ai.shopping.domain.ports.shop.repositories.OrderRepositoryPort;
import cart.ai.shopping.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@UseCase
public class CancelOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    public Result<Order> execute(OrderId orderId) {
        Order order = orderRepositoryPort.find(orderId);

        if (order != null) {
            order.cancel();

            return Result.success(orderRepositoryPort.save(order));
        }

        return Result.error(HttpStatus.NOT_FOUND.value());
    }

}