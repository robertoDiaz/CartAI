/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.shop.product;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.application.usecases.shop.commands.CreateProductCommand;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.model.shop.Product;
import cart.ai.shopping.domain.model.shop.vos.ProductCreatedEvent;
import cart.ai.shopping.domain.model.shop.vos.ProductId;
import cart.ai.shopping.domain.ports.common.IncrementIdGeneratorPort;
import cart.ai.shopping.domain.ports.shop.ProductEventPublisherPort;
import cart.ai.shopping.domain.ports.shop.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static cart.ai.shopping.domain.common.result.ResultError.INTERNAL_ERROR;

/**
 * @author Roberto Díaz
 */
@RequiredArgsConstructor
@UseCase
@Slf4j
public class CreateProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private final IncrementIdGeneratorPort incrementIdGeneratorPort;
    private final ProductEventPublisherPort productEventPublisherPort;

    public Result<Product> execute(CreateProductCommand command) {
        ProductId productId = new ProductId(incrementIdGeneratorPort.generate(Product.class));

        if (productRepositoryPort.find(productId) != null) {
            return Result.error(INTERNAL_ERROR);
        }

        Product product = new Product(
                productId,
                command.name(),
                command.description(),
                command.price(),
                command.stock(),
                command.imageFileIds()
        );

        Product saved = productRepositoryPort.save(product);

        productEventPublisherPort.productCreated(
                new ProductCreatedEvent(productId.value(), command.imageFileIds()
        ));

        return Result.success(saved);
    }
}
