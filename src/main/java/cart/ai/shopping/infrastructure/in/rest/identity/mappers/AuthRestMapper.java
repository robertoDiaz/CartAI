/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.identity.mappers;

import cart.ai.shopping.application.usecases.identity.user.commands.AuthenticateUserCommand;
import cart.ai.shopping.application.usecases.identity.user.commands.CreateUserCommand;
import cart.ai.shopping.domain.model.identity.Role;
import cart.ai.shopping.domain.model.identity.User;
import cart.ai.shopping.infrastructure.in.rest.identity.dtos.AuthRestResponse;
import cart.ai.shopping.infrastructure.in.rest.identity.dtos.LoginRestRequest;
import cart.ai.shopping.infrastructure.in.rest.identity.dtos.RegisterRestRequest;

import java.util.Set;

/**
 * @author Roberto Díaz
 */
public class AuthRestMapper {

    public static CreateUserCommand toCreateUserCommand(RegisterRestRequest request, Set<Role> roles) {
        return new CreateUserCommand(request.name(), request.email(), request.password(), roles, request.avatarFileId());
    }

    public static AuthenticateUserCommand toAuthenticateUserCommand(LoginRestRequest request) {
        return new AuthenticateUserCommand(request.email(), request.password());
    }

    public static AuthRestResponse toResponse(User user, String token) {
        return new AuthRestResponse(
                user.userId().value(),
                token,
                user.email().value(),
                user.name(),
                user.roles().stream().map(Role::name).toList(),
                user.avatarFileId()
        );
    }
}
