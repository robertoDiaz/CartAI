/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.identity.user.commands;

import jakarta.validation.constraints.NotBlank;

/**
 * @author Roberto Díaz
 */
public record AuthenticateUserCommand(

        @NotBlank(message = "Email is mandatory")
        String email,

        @NotBlank(message = "Password is mandatory")
        String password

) {
}
