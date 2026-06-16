package com.bikemmerce.commerce.infrastructure.in.rest.dto.customer;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateProductRestRequest(
        @NotBlank(message = "Name is mandatory")
        String name,

        @NotBlank(message = "Description is mandatory")
        String description,

        @NotNull(message = "Price is mandatory")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be more than 0")
        BigDecimal price,

        @NotNull(message = "Stock is mandatory")
        @Min(value = 0, message = "Stock could not be negative")
        Integer stock
) {
}