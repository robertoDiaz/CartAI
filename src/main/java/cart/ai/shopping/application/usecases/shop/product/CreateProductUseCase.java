/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.shop.product;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.application.usecases.shop.commands.CreateProductCommand;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.model.shop.Product;
import cart.ai.shopping.domain.model.shop.vos.ProductId;
import cart.ai.shopping.domain.ports.common.IncrementIdGeneratorPort;
import cart.ai.shopping.domain.ports.shop.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author Roberto Díaz
 */
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
