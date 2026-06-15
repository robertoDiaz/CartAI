package com.bikemmerce.commerce.adapters.out.mongo.mapper;

import com.bikemmerce.commerce.adapters.out.mongo.documents.dto.CartDocument;
import com.bikemmerce.commerce.domain.model.Cart;
import com.bikemmerce.commerce.domain.model.value.objects.CustomerId;

public class CartMapper {

    public static CartDocument toDocument(Cart cart) {
        return new CartDocument(
            cart.getCustomerId().value(), cart.getShoppingItems().stream().map(ShoppingItemMapper::toDocument).toList());
    }

    public static Cart toDomain(CartDocument cartDocument) {
        return new Cart(
            new CustomerId(cartDocument.getCustomerId()),
            cartDocument.getShoppingItems().stream().map(ShoppingItemMapper::toDomain).toList());
    }

}
