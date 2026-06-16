package com.bikemmerce.commerce.infrastructure.in.rest.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddShoppingItemToCartRestRequest(

        @NotNull(message = "CustomerId is mandatory")
        String customerId,

        @NotNull(message = "ProductId is mandatory")
        String productId,

        @NotNull(message = "Stock is mandatory")
        @Min(value = 0, message = "Stock could not be negative")
        Integer quantity

) {
}
