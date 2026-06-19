/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.identity.role.commands;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

/**
 * @author Roberto Díaz
 */
public record CreateRoleCommand(

        @NotBlank(message = "Name is mandatory")
        String name,

        @NotNull
        Set<String> permissions

) {
}
