/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.shop.dtos;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Roberto Díaz
 */
public record ProductRestResponse(
        String id,
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        List<String> imageFileIds
) {
}
