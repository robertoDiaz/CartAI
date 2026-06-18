package cart.ai.shopping.infrastructure.in.rest.mapper;

import cart.ai.shopping.application.usecases.shop.commands.AddShoppingItemToCartCommand;
import cart.ai.shopping.application.usecases.shop.commands.RemoveShoppingItemToCartCommand;
import cart.ai.shopping.domain.model.security.value.objects.UserId;
import cart.ai.shopping.domain.model.shop.Cart;
import cart.ai.shopping.domain.model.shop.value.objects.ProductId;
import cart.ai.shopping.infrastructure.in.rest.dto.cart.AddShoppingItemToCartRestRequest;
import cart.ai.shopping.infrastructure.in.rest.dto.cart.CartRestResponse;
import cart.ai.shopping.infrastructure.in.rest.dto.cart.RemoveShoppingItemToCartRestRequest;

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
