package cart.ai.shopping.infrastructure.out.persistence.mongo.shop.adapters;

import cart.ai.shopping.domain.model.shop.Product;
import cart.ai.shopping.domain.model.shop.value.objects.ProductId;
import cart.ai.shopping.domain.ports.shop.repositories.ProductRepositoryPort;
import cart.ai.shopping.infrastructure.out.persistence.mongo.shop.ProductMongoRepository;
import cart.ai.shopping.infrastructure.out.persistence.mongo.shop.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductMongoAdapter implements ProductRepositoryPort {

    private final ProductMongoRepository productMongoRepository;

    @Override
    public void delete(ProductId productId) {
        productMongoRepository.deleteById(productId.value());
    }

    @Override
    public Product find(ProductId productId) {
        return productMongoRepository.findById(productId.value()).map(ProductMapper::toProduct).orElse(null);
    }

    @Override
    public List<Product> findAll() {
        return productMongoRepository.findAll().stream().map(ProductMapper::toProduct).toList();
    }

    @Override
    public Product save(Product product) {
        return ProductMapper.toProduct(productMongoRepository.save(ProductMapper.toProductDocument(product)));
    }
}
