/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.domain.ports.shop;

import cart.ai.shopping.domain.model.identity.vos.UserId;
import cart.ai.shopping.domain.model.shop.Cart;

import java.util.List;

/**
 * @author Roberto Díaz
 */
public interface CartRepositoryPort {

    void delete(UserId userId);

    Cart find(UserId userId);

    List<Cart> findAll();

    Cart save(Cart cart);
}
