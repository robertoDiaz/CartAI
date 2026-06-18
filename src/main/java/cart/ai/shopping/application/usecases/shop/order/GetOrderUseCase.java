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
public class GetOrderUseCase {

    private final OrderRepositoryPort OrderRepositoryPort;

    public Result<Order> execute(OrderId orderId) {
        Order Order = OrderRepositoryPort.find(orderId);

        if (Order != null) {
            return Result.success(Order);
        }

        return Result.error(HttpStatus.NOT_FOUND.value());
    }

}