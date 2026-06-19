/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.domain.model.shop;

import cart.ai.shopping.domain.model.shop.vos.ProductId;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

/**
 * @author Roberto Díaz
 */
@Data
public class Product {

    @NonNull
    private ProductId id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    @NonNull
    private BigDecimal price;
    @NonNull
    private Integer stock;

    public Product(@NonNull ProductId id, @NonNull String name, @NonNull String description, @NonNull BigDecimal price, @NonNull Integer stock) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public void increaseStock(Integer count) {
        this.stock += count;
    }

    public void decreaseStock(Integer count) {
        this.stock -= count;
    }

    public void updatePrice(BigDecimal price) {
        this.price = price;
    }

}
