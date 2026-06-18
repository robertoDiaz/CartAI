package cart.ai.shopping.infrastructure.in.rest.dto.cart;

import cart.ai.shopping.domain.model.shop.value.objects.ShoppingItem;

import java.util.List;

public record CartRestResponse(
        String customerId,
        List<ShoppingItem> shoppingItems
) {
}
