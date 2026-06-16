package com.bikemmerce.commerce.infrastructure.in.rest.dto.order;

import com.bikemmerce.commerce.domain.model.ShoppingItem;

import java.util.Date;
import java.util.List;

public record OrderRestResponse(
        String orderId,
        String customerId,
        List<ShoppingItem> shoppingItems,
        String status,
        Date createDate
) {
}
