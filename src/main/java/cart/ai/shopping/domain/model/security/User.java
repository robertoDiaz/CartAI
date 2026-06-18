package cart.ai.shopping.domain.model.security;

import cart.ai.shopping.domain.model.security.value.objects.Email;
import cart.ai.shopping.domain.model.security.value.objects.UserId;
import lombok.NonNull;

import java.util.Set;

public record User(@NonNull UserId userId, @NonNull String name, @NonNull Email email, @NonNull String passwordHash,
                   @NonNull Set<Role> roles) {

}
