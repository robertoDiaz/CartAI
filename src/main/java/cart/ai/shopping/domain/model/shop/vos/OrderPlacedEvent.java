/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.domain.model.shop.vos;

import cart.ai.shopping.domain.model.identity.vos.UserId;
import cart.ai.shopping.domain.model.shop.constants.OrderStatus;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Roberto Díaz
 */
public record OrderPlacedEvent(
        OrderId orderId,
        UserId userId,
        BigDecimal totalPrice,
        OrderStatus orderStatus,
        Date createDate
) {
}
