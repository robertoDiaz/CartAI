/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.shop.mappers;

import cart.ai.shopping.application.usecases.shop.commands.CreateProductCommand;
import cart.ai.shopping.application.usecases.shop.commands.UpdateProductCommand;
import cart.ai.shopping.domain.model.shop.Product;
import cart.ai.shopping.infrastructure.in.rest.shop.dtos.CreateProductRestRequest;
import cart.ai.shopping.infrastructure.in.rest.shop.dtos.ProductRestResponse;
import cart.ai.shopping.infrastructure.in.rest.shop.dtos.UpdateProductRestRequest;

/**
 * @author Roberto Díaz
 */
public class ProductRestMapper {

    public static CreateProductCommand toCreateProductCommand(CreateProductRestRequest request) {
        return new CreateProductCommand(
                request.name(), request.description(), request.price(), request.stock(), request.imageFileIds());
    }

    public static UpdateProductCommand toUpdateProductCommand(UpdateProductRestRequest request) {
        return new UpdateProductCommand(
                request.id(), request.name(), request.description(), request.price(), request.stock(), request.imageFileIds());
    }

    public static ProductRestResponse toResponse(Product product) {
        return new ProductRestResponse(
                product.getId().value(), product.getName(), product.getDescription(), product.getPrice(), product.getStock(), product.getImageFileIds());
    }
}
