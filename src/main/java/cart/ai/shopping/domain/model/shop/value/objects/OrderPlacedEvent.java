package cart.ai.shopping.domain.model.shop.value.objects;

import cart.ai.shopping.domain.model.constants.OrderStatus;
import cart.ai.shopping.domain.model.security.value.objects.UserId;

import java.math.BigDecimal;
import java.util.Date;

public record OrderPlacedEvent(
        OrderId orderId,
        UserId userId,
        BigDecimal totalPrice,
        OrderStatus orderStatus,
        Date createDate
) {
}