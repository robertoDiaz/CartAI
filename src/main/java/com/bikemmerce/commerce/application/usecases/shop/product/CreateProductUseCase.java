package com.bikemmerce.commerce.application.usecases.shop.product;

import com.bikemmerce.commerce.application.annotations.UseCase;
import com.bikemmerce.commerce.application.usecases.shop.commands.CreateProductCommand;
import com.bikemmerce.commerce.domain.model.shop.Product;
import com.bikemmerce.commerce.domain.model.shop.value.objects.ProductId;
import com.bikemmerce.commerce.domain.ports.common.IncrementIdGeneratorPort;
import com.bikemmerce.commerce.domain.ports.shop.ProductRepositoryPort;
import com.bikemmerce.commerce.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@UseCase
public class CreateProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private final IncrementIdGeneratorPort incrementIdGeneratorPort;

    public Result<Product> execute(CreateProductCommand command) {
        ProductId productId = new ProductId(incrementIdGeneratorPort.generate(Product.class));

        if (productRepositoryPort.find(productId) != null) {
            return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        Product product = new Product(
                productId,
                command.name(),
                command.description(),
                command.price(),
                command.stock()
        );

        return Result.success(productRepositoryPort.save(product));
    }
}