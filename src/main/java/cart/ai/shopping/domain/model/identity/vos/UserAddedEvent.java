/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.domain.model.identity.vos;

import cart.ai.shopping.domain.model.identity.Role;
import lombok.NonNull;

import java.util.Set;

/**
 * @author Roberto Díaz
 */
public record UserAddedEvent(
        @NonNull UserId userId,
        @NonNull String name,
        @NonNull Email email,
        @NonNull Set<Role> roles
) {
}
