package cart.ai.shopping.application.usecases.shop.product;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.domain.model.shop.Product;
import cart.ai.shopping.domain.model.shop.value.objects.ProductId;
import cart.ai.shopping.domain.ports.shop.repositories.ProductRepositoryPort;
import cart.ai.shopping.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@UseCase
public class DeleteProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public Result<Product> execute(String id) {
        ProductId productId = new ProductId(id);

        Product product = productRepositoryPort.find(productId);

        if (product == null) {
            return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        productRepositoryPort.delete(productId);

        return Result.success(product);
    }
}