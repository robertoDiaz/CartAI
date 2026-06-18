package cart.ai.shopping.domain.ports.shop.events;

import cart.ai.shopping.domain.model.shop.value.objects.OrderPlacedEvent;

public interface OrderPlacedEventPublisherPort {
    void publish(OrderPlacedEvent event);
}
