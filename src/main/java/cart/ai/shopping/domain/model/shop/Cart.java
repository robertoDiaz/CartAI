package cart.ai.shopping.domain.model.shop;

import cart.ai.shopping.domain.model.security.value.objects.UserId;
import cart.ai.shopping.domain.model.shop.value.objects.ProductId;
import cart.ai.shopping.domain.model.shop.value.objects.ShoppingItem;
import lombok.Data;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

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
