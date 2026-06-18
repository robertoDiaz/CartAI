package cart.ai.shopping.infrastructure.in.rest.dto.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateCustomerRestRequest(
        @NotBlank(message = "Name is mandatory")
        String name,

        @Email
        @NotBlank(message = "Email is mandatory")
        String email
) {
}