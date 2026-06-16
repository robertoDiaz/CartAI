package com.bikemmerce.commerce.infrastructure.in.rest.dto.customer;

import jakarta.validation.constraints.NotBlank;

public record UpdateCustomerRestRequest(
        @NotBlank(message = "Id is mandatory")
        String id,

        @NotBlank(message = "Name is mandatory")
        String name,

        @NotBlank(message = "Email is mandatory")
        String email
) {
}