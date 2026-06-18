package cart.ai.shopping.application.usecases.shop.commands;

import jakarta.validation.constraints.NotBlank;

public record CreateCustomerCommand(
        @NotBlank(message = "Name is mandatory")
        String name,

        @NotBlank(message = "Email is mandatory")
        String email
) {
}