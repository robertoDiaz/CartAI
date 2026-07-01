package cart.ai.shopping.domain.model.shop.vos;

import java.util.List;

public record ProductUpdatedEvent(String productId, List<String> oldImageFileIds, List<String> newImageFileIds) {
}
