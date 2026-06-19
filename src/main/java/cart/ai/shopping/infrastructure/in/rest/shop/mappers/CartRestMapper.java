/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.shop.mappers;

import cart.ai.shopping.application.usecases.shop.commands.AddShoppingItemToCartCommand;
import cart.ai.shopping.application.usecases.shop.commands.RemoveShoppingItemToCartCommand;
import cart.ai.shopping.domain.model.identity.vos.UserId;
import cart.ai.shopping.domain.model.shop.Cart;
import cart.ai.shopping.domain.model.shop.vos.ProductId;
import cart.ai.shopping.infrastructure.in.rest.shop.dtos.AddShoppingItemToCartRestRequest;
import cart.ai.shopping.infrastructure.in.rest.shop.dtos.CartRestResponse;
import cart.ai.shopping.infrastructure.in.rest.shop.dtos.RemoveShoppingItemToCartRestRequest;

/**
 * @author Roberto Díaz
 */
public class CartRestMapper {

    public static AddShoppingItemToCartCommand toAddShoppingItemToCartCommand(AddShoppingItemToCartRestRequest request) {
        return new AddShoppingItemToCartCommand(
                new UserId(request.customerId()), new ProductId(request.productId()), request.quantity());
    }


    public static RemoveShoppingItemToCartCommand toRemoveShoppingItemToCartCommand(RemoveShoppingItemToCartRestRequest request) {
        return new RemoveShoppingItemToCartCommand(
                new UserId(request.customerId()), new ProductId(request.productId()), request.quantity());
    }

    public static CartRestResponse toResponse(Cart cart) {
        return new CartRestResponse(cart.getUserId().value(), cart.getShoppingItems());
    }
}
