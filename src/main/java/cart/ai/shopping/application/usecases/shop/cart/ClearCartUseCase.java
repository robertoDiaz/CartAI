package cart.ai.shopping.application.usecases.shop.cart;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.domain.model.security.value.objects.UserId;
import cart.ai.shopping.domain.model.shop.Cart;
import cart.ai.shopping.domain.ports.shop.repositories.CartRepositoryPort;
import cart.ai.shopping.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@UseCase
public class ClearCartUseCase {

    private final CartRepositoryPort cartRepositoryPort;

    public Result<Cart> execute(UserId userId) {
        Cart cart = cartRepositoryPort.find(userId);

        if (cart != null) {
            cart.clearItems();

            return Result.success(cartRepositoryPort.save(cart));
        }

        return Result.error(HttpStatus.NOT_FOUND.value());
    }

}