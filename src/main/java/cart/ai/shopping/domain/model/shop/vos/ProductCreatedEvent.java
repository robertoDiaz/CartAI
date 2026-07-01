package cart.ai.shopping.domain.model.shop.vos;

import java.util.List;

public record ProductCreatedEvent(String productId, List<String> imageFileIds) {
}
