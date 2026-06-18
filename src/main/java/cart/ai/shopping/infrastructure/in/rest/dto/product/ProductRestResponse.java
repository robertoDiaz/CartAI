package cart.ai.shopping.infrastructure.in.rest.dto.product;

import cart.ai.shopping.domain.model.shop.value.objects.ProductId;

import java.math.BigDecimal;

public record ProductRestResponse(
        ProductId id,
        String name,
        String description,
        BigDecimal price,
        Integer stock
) {
}
