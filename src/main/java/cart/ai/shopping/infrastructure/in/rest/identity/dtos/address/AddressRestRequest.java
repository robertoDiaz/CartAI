/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.identity.dtos.address;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

/**
 * @author Roberto Díaz
 */
@Builder
public record AddressRestRequest(
        @NotBlank String alias,
        @NotBlank String firstName,
        @NotBlank String lastName,
        String company,
        @NotBlank String street,
        @NotBlank String city,
        @NotBlank String zipCode,
        String phone,
        String state,
        @NotBlank String country,
        String notes,
        boolean isDefault
) {
}
