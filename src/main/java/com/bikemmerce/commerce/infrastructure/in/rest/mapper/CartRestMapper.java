package com.bikemmerce.commerce.infrastructure.in.rest.mapper;

import com.bikemmerce.commerce.application.usecases.commands.AddShoppingItemToCartCommand;
import com.bikemmerce.commerce.application.usecases.commands.RemoveShoppingItemToCartCommand;
import com.bikemmerce.commerce.domain.model.Cart;
import com.bikemmerce.commerce.domain.model.value.objects.CustomerId;
import com.bikemmerce.commerce.domain.model.value.objects.ProductId;
import com.bikemmerce.commerce.infrastructure.in.rest.dto.cart.AddShoppingItemToCartRestRequest;
import com.bikemmerce.commerce.infrastructure.in.rest.dto.cart.CartRestResponse;
import com.bikemmerce.commerce.infrastructure.in.rest.dto.cart.RemoveShoppingItemToCartRestRequest;

public class CartRestMapper {

    public static AddShoppingItemToCartCommand toAddShoppingItemToCartCommand(AddShoppingItemToCartRestRequest request) {
        return new AddShoppingItemToCartCommand(
                new CustomerId(request.customerId()), new ProductId(request.productId()), request.quantity());
    }


    public static RemoveShoppingItemToCartCommand toRemoveShoppingItemToCartCommand(RemoveShoppingItemToCartRestRequest request) {
        return new RemoveShoppingItemToCartCommand(
                new CustomerId(request.customerId()), new ProductId(request.productId()), request.quantity());
    }

    public static CartRestResponse toResponse(Cart cart) {
        return new CartRestResponse(cart.getCustomerId().value(), cart.getShoppingItems());
    }
}
