package com.bikemmerce.commerce.infrastructure.out.persistence.mongo.mapper;

import com.bikemmerce.commerce.domain.model.Cart;
import com.bikemmerce.commerce.domain.model.value.objects.CustomerId;
import com.bikemmerce.commerce.infrastructure.out.persistence.mongo.documents.CartDocument;

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
