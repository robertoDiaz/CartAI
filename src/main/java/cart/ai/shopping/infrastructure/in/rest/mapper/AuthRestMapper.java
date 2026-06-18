package cart.ai.shopping.infrastructure.in.rest.mapper;

import cart.ai.shopping.application.usecases.security.commands.AuthenticateUserCommand;
import cart.ai.shopping.application.usecases.security.commands.CreateUserCommand;
import cart.ai.shopping.domain.model.security.Role;
import cart.ai.shopping.domain.model.security.User;
import cart.ai.shopping.infrastructure.in.rest.dto.security.AuthRestResponse;
import cart.ai.shopping.infrastructure.in.rest.dto.security.LoginRestRequest;
import cart.ai.shopping.infrastructure.in.rest.dto.security.RegisterRestRequest;

import java.util.Set;

public class AuthRestMapper {

    public static CreateUserCommand toCreateUserCommand(RegisterRestRequest request, Set<Role> roles) {
        return new CreateUserCommand(request.name(), request.email(), request.password(), roles);
    }

    public static AuthenticateUserCommand toAuthenticateUserCommand(LoginRestRequest request) {
        return new AuthenticateUserCommand(request.email(), request.password());
    }

    public static AuthRestResponse toResponse(User user, String token) {
        return new AuthRestResponse(
                token,
                user.email().value(),
                user.name(),
                user.roles().stream().map(Role::name).toList()
        );
    }
}
