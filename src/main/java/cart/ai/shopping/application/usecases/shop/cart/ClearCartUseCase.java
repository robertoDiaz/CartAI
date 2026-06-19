/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.shop.cart;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.model.identity.vos.UserId;
import cart.ai.shopping.domain.model.shop.Cart;
import cart.ai.shopping.domain.ports.shop.CartRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author Roberto Díaz
 */
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
