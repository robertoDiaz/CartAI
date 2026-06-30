/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.domain.model.identity;

import cart.ai.shopping.domain.model.identity.vos.AddressId;
import lombok.NonNull;

/**
 * @author Roberto Díaz
 */
public record Address(
        @NonNull AddressId id,
        @NonNull String ownerId,
        @NonNull String ownerType, // e.g., "USER", "STORE"
        @NonNull String alias,
        @NonNull String firstName,
        @NonNull String lastName,
        String company,
        @NonNull String street,
        @NonNull String city,
        @NonNull String zipCode,
        String phone,
        String state,
        @NonNull String country,
        String notes,
        boolean isDefault
) {
}
