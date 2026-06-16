package com.bikemmerce.commerce.infrastructure.in.rest.dto.product;

import jakarta.validation.constraints.NotBlank;

public record CreateCustomerRestRequest(
        @NotBlank(message = "Name is mandatory")
        String name,

        @NotBlank(message = "Email is mandatory")
        String email
) {
}