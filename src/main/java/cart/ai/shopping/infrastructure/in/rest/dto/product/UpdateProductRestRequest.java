package cart.ai.shopping.infrastructure.in.rest.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record UpdateProductRestRequest(
        @NotBlank(message = "Id is mandatory")
        String id,

        @NotBlank(message = "Name is mandatory")
        String name,

        @NotBlank(message = "Description is mandatory")
        String description,

        @NotNull(message = "Price is mandatory")
        @PositiveOrZero
        BigDecimal price,

        @NotNull(message = "Stock is mandatory")
        @PositiveOrZero
        Integer stock
) {
}