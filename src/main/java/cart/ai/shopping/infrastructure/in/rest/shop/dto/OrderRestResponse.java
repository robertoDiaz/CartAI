/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.shop.dto;

import cart.ai.shopping.domain.model.shop.value.objects.ShoppingItem;

import java.util.Date;
import java.util.List;

/**
 * @author Roberto Díaz
 */
public record OrderRestResponse(
        String orderId,
        String customerId,
        List<ShoppingItem> shoppingItems,
        String status,
        Date createDate
) {
}
