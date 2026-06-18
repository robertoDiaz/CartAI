package cart.ai.shopping.domain.ports.security.events;

import cart.ai.shopping.domain.model.security.value.objects.UserAddedEvent;

public interface UserAddedEventPublisherPort {
    void added(UserAddedEvent event);
}
