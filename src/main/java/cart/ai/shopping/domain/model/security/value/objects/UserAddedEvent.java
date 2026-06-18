package cart.ai.shopping.domain.model.security.value.objects;

import cart.ai.shopping.domain.model.security.Role;
import lombok.NonNull;

import java.util.Set;

public record UserAddedEvent(
        @NonNull UserId userId,
        @NonNull String name,
        @NonNull Email email,
        @NonNull Set<Role> roles
) {
}
