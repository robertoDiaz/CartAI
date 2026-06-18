package cart.ai.shopping.domain.ports.shop.repositories;

import cart.ai.shopping.domain.model.security.value.objects.UserId;
import cart.ai.shopping.domain.model.shop.Cart;

import java.util.List;

public interface CartRepositoryPort {

    void delete(UserId userId);

    Cart find(UserId userId);

    List<Cart> findAll();

    Cart save(Cart cart);
}
