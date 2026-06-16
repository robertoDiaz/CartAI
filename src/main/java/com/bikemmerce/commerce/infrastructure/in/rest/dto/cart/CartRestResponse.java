package com.bikemmerce.commerce.infrastructure.in.rest.dto.cart;

import com.bikemmerce.commerce.domain.model.ShoppingItem;

import java.util.List;

public record CartRestResponse(
        String customerId,
        List<ShoppingItem> shoppingItems
) {
}
