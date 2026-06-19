/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.shop.mappers;

import cart.ai.shopping.domain.model.shop.Order;
import cart.ai.shopping.infrastructure.in.rest.shop.dtos.OrderRestResponse;

/**
 * @author Roberto Díaz
 */
public class OrderRestMapper {

    public static OrderRestResponse toResponse(Order order) {
        return new OrderRestResponse(
                order.getOrderId().value(), order.getUserId().value(), order.getShoppingItems(),
                order.getStatus().getValue(), order.getCreateDate());
    }
}
