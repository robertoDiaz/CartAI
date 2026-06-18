package cart.ai.shopping.infrastructure.in.rest.mapper;

import cart.ai.shopping.application.usecases.shop.commands.CreateProductCommand;
import cart.ai.shopping.application.usecases.shop.commands.UpdateProductCommand;
import cart.ai.shopping.domain.model.shop.Product;
import cart.ai.shopping.infrastructure.in.rest.dto.product.CreateProductRestRequest;
import cart.ai.shopping.infrastructure.in.rest.dto.product.ProductRestResponse;
import cart.ai.shopping.infrastructure.in.rest.dto.product.UpdateProductRestRequest;

public class ProductRestMapper {

    public static CreateProductCommand toCreateProductCommand(CreateProductRestRequest request) {
        return new CreateProductCommand(
                request.name(), request.description(), request.price(), request.stock());
    }

    public static UpdateProductCommand toUpdateProductCommand(UpdateProductRestRequest request) {
        return new UpdateProductCommand(
                request.id(), request.name(), request.description(), request.price(), request.stock());
    }

    public static ProductRestResponse toResponse(Product product) {
        return new ProductRestResponse(
                product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getStock());
    }
}
