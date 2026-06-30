/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.identity.mappers;

import cart.ai.shopping.application.usecases.identity.user.commands.UpdateUserCommand;
import cart.ai.shopping.domain.model.identity.Role;
import cart.ai.shopping.domain.model.identity.User;
import cart.ai.shopping.infrastructure.in.rest.identity.dtos.UpdateUserRestRequest;
import cart.ai.shopping.infrastructure.in.rest.identity.dtos.UserRestResponse;

/**
 * @author Roberto Díaz
 */
public class UserRestMapper {

    public static UpdateUserCommand toUpdateUserCommand(UpdateUserRestRequest request, java.util.Set<Role> roles) {
        return new UpdateUserCommand(
                request.id(),
                request.name(),
                roles,
                request.avatarFileId(),
                request.oldPassword(),
                request.newPassword(),
                request.phone(),
                request.taxId(),
                request.preferredLanguage()
        );
    }

    public static UserRestResponse toResponse(User user) {
        if (user == null) {
            return null;
        }
        return new UserRestResponse(
                user.userId().value(),
                user.name(),
                user.email().value(),
                user.roles().stream().map(Role::name).toList(),
                user.avatarFileId(),
                user.phone(),
                user.taxId(),
                user.preferredLanguage()
        );
    }
}
