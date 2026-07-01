package cart.ai.shopping.domain.ports.shop;

import cart.ai.shopping.domain.model.shop.vos.ProductCreatedEvent;
import cart.ai.shopping.domain.model.shop.vos.ProductUpdatedEvent;

public interface ProductEventPublisherPort {
    void productCreated(ProductCreatedEvent event);

    void productUpdated(ProductUpdatedEvent event);
}
