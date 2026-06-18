package cart.ai.shopping.application.usecases.shop.product;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.domain.model.shop.Product;
import cart.ai.shopping.domain.ports.shop.repositories.ProductRepositoryPort;
import cart.ai.shopping.domain.result.Result;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@UseCase
public class ListProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public Result<List<Product>> execute() {
        return Result.success(productRepositoryPort.findAll());
    }

}