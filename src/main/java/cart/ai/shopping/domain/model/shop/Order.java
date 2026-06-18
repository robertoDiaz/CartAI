package cart.ai.shopping.domain.model.shop;

import cart.ai.shopping.domain.model.constants.OrderStatus;
import cart.ai.shopping.domain.model.security.value.objects.UserId;
import cart.ai.shopping.domain.model.shop.value.objects.OrderId;
import cart.ai.shopping.domain.model.shop.value.objects.ShoppingItem;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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