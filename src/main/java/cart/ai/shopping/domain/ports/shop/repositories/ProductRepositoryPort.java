package cart.ai.shopping.domain.ports.shop.repositories;

import cart.ai.shopping.domain.model.shop.Product;
import cart.ai.shopping.domain.model.shop.value.objects.ProductId;

import java.util.List;

public interface ProductRepositoryPort {

    void delete(ProductId productId);

    Product find(ProductId productId);

    List<Product> findAll();

    Product save(Product product);
}
