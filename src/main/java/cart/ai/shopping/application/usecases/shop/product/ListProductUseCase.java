/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.shop.product;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.model.shop.Product;
import cart.ai.shopping.domain.ports.shop.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author Roberto Díaz
 */
@RequiredArgsConstructor
@UseCase
public class ListProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public Result<List<Product>> execute() {
        return Result.success(productRepositoryPort.findAll());
    }

}
