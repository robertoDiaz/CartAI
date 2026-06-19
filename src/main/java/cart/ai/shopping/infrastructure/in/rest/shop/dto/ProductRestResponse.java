/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.shop.dto;

import cart.ai.shopping.domain.model.shop.value.objects.ProductId;

import java.math.BigDecimal;

/**
 * @author Roberto Díaz
 */
public record ProductRestResponse(
        ProductId id,
        String name,
        String description,
        BigDecimal price,
        Integer stock
) {
}
