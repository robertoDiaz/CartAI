/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.domain.model.shop;

import cart.ai.shopping.domain.model.identity.vos.UserId;
import cart.ai.shopping.domain.model.shop.constants.OrderStatus;
import cart.ai.shopping.domain.model.shop.vos.OrderId;
import cart.ai.shopping.domain.model.shop.vos.ShoppingItem;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Roberto Díaz
 */
@Data
public class Order {

    @NonNull
    private OrderId orderId;
    @NonNull
    private UserId userId;
    @NonNull
    private List<ShoppingItem> shoppingItems;
    @NonNull
    private OrderStatus status;
    @NonNull
    private Date createDate;

    public Order(@NonNull OrderId orderId, @NonNull UserId userId, @NonNull List<ShoppingItem> shoppingItems, @NonNull OrderStatus status, @NonNull Date createDate) {
        this.orderId = orderId;
        this.userId = userId;
        this.shoppingItems = shoppingItems;
        this.status = status;
        this.createDate = createDate;
    }

    public void cancel() {
        status = OrderStatus.CANCELLED;
    }

    public void confirm() {
        status = OrderStatus.CONFIRMED;
    }

    public BigDecimal getTotalPrice() {
        return shoppingItems.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getCount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
