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
public class GetProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public Result<Product> execute(String id) {
        Product product = productRepositoryPort.find(new ProductId(id));

        if (product != null) {
            return Result.success(product);
        }

        return Result.error(HttpStatus.NOT_FOUND.value());
    }
}