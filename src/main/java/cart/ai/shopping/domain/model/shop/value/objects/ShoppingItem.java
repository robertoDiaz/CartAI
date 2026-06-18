package cart.ai.shopping.domain.model.shop.value.objects;

import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

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
