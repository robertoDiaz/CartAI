/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.shop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * @author Roberto Díaz
 */
public record CreateCustomerRestRequest(
        @NotBlank(message = "Name is mandatory")
        String name,

        @Email
        @NotBlank(message = "Email is mandatory")
        String email
) {
}
