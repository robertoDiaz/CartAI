/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.out.persistence.mongo.shop.mappers;

import cart.ai.shopping.domain.model.identity.vos.UserId;
import cart.ai.shopping.domain.model.shop.Cart;
import cart.ai.shopping.infrastructure.out.persistence.mongo.shop.documents.CartDocument;

/**
 * @author Roberto Díaz
 */
public class CartMapper {

    public static CartDocument toDocument(Cart cart) {
        return CartDocument.builder()
                .customerId(cart.getUserId().value())
                .shoppingItems(cart.getShoppingItems().stream()
                        .map(ShoppingItemMapper::toDocument)
                        .toList())
                .build();
    }

    public static Cart toDomain(CartDocument cartDocument) {
        return new Cart(
                new UserId(cartDocument.getCustomerId()),
                cartDocument.getShoppingItems() != null
                        ? cartDocument.getShoppingItems().stream().map(ShoppingItemMapper::toDomain).collect(java.util.stream.Collectors.toList())
                        : new java.util.ArrayList<>()
        );
    }
}
