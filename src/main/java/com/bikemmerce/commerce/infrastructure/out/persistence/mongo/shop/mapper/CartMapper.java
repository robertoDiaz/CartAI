package com.bikemmerce.commerce.infrastructure.out.persistence.mongo.shop.mapper;

import com.bikemmerce.commerce.domain.model.security.value.objects.UserId;
import com.bikemmerce.commerce.domain.model.shop.Cart;
import com.bikemmerce.commerce.infrastructure.out.persistence.mongo.shop.documents.CartDocument;

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
                cartDocument.getShoppingItems().stream()
                        .map(ShoppingItemMapper::toDomain)
                        .toList()
        );
    }
}
