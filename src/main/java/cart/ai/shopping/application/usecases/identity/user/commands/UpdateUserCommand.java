/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.identity.user.commands;

import cart.ai.shopping.domain.model.identity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

/**
 * @author Roberto Díaz
 */
public record UpdateUserCommand(
        @NotBlank(message = "Id is mandatory")
        String id,

        @NotBlank(message = "Name is mandatory")
        String name,

        @NotNull
        Set<Role> roles,

        String avatarFileId,

        String oldPassword,

        String newPassword
) {
}
