package cart.ai.shopping.domain.ports.shop.events;

import cart.ai.shopping.domain.model.shop.value.objects.CustomerAddedEvent;

public interface CustomerAddedEventPublisherPort {
    void added(CustomerAddedEvent event);
}
