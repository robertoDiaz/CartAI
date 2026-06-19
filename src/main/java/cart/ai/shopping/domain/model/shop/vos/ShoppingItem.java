/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.domain.model.shop.vos;

import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

/**
 * @author Roberto Díaz
 */
@Data
public class ShoppingItem {

    @NonNull
    private ProductId productId;
    @NonNull
    private Integer count;
    @NonNull
    private BigDecimal unitPrice;

    public ShoppingItem(@NonNull ProductId productId, @NonNull Integer count, @NonNull BigDecimal unitPrice) {
        this.productId = productId;
        this.count = count;
        this.unitPrice = unitPrice;
    }
}
