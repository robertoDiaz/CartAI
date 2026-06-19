/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.domain.model.shop;

import cart.ai.shopping.domain.model.identity.vos.UserId;
import cart.ai.shopping.domain.model.shop.vos.ProductId;
import cart.ai.shopping.domain.model.shop.vos.ShoppingItem;
import lombok.Data;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

/**
 * @author Roberto Díaz
 */
@Data
public class Cart {

    @NonNull
    private UserId userId;
    @NonNull
    private List<ShoppingItem> shoppingItems;

    public Cart(@NonNull UserId userId, @NonNull List<ShoppingItem> shoppingItems) {
        this.userId = userId;
        this.shoppingItems = shoppingItems;
    }

    public void addItem(Product product, Integer count) {
        ProductId productId = product.getId();

        Optional<ShoppingItem> existingItem = shoppingItems.stream()
                .filter(item -> productId.equals(item.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {
            ShoppingItem item = existingItem.get();

            item.setCount(item.getCount() + count);
        } else {
            shoppingItems.add(
                    new ShoppingItem(productId, count, product.getPrice()));
        }
    }

    public void removeItem(ProductId productId) {
        shoppingItems.removeIf(
                shoppingItem -> productId.equals(shoppingItem.getProductId()));
    }

    public void clearItems() {
        shoppingItems.clear();
    }

}
