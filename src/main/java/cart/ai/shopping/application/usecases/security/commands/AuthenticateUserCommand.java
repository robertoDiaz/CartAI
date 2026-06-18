package cart.ai.shopping.application.usecases.security.commands;

import jakarta.validation.constraints.NotBlank;

public record AuthenticateUserCommand(

        @NotBlank(message = "Email is mandatory")
        String email,

        @NotBlank(message = "Password is mandatory")
        String password

) {
}
