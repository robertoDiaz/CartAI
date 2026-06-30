/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.identity.dtos.address;

import lombok.Builder;

/**
 * @author Roberto Díaz
 */
@Builder
public record AddressRestResponse(
        String id,
        String alias,
        String firstName,
        String lastName,
        String company,
        String street,
        String city,
        String zipCode,
        String phone,
        String state,
        String country,
        String notes,
        boolean isDefault
) {
}
