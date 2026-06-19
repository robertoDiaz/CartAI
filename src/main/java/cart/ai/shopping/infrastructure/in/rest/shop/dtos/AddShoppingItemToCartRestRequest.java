/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.shop.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * @author Roberto Díaz
 */
public record AddShoppingItemToCartRestRequest(

        @NotNull(message = "CustomerId is mandatory")
        String customerId,

        @NotNull(message = "ProductId is mandatory")
        String productId,

        @NotNull(message = "Stock is mandatory")
        @PositiveOrZero
        Integer quantity

) {
}
