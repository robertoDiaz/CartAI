package cart.ai.shopping.infrastructure.in.rest.mapper;

import cart.ai.shopping.domain.model.shop.Order;
import cart.ai.shopping.infrastructure.in.rest.dto.order.OrderRestResponse;

public class OrderRestMapper {

    public static OrderRestResponse toResponse(Order order) {
        return new OrderRestResponse(
                order.getOrderId().value(), order.getUserId().value(), order.getShoppingItems(),
                order.getStatus().getValue(), order.getCreateDate());
    }
}
