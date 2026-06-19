/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.domain.model.identity;

import cart.ai.shopping.domain.model.identity.vos.Permission;
import cart.ai.shopping.domain.model.identity.vos.RoleId;
import lombok.NonNull;

import java.util.Set;

/**
 * @author Roberto Díaz
 */
public record Role(@NonNull RoleId id, @NonNull String name, @NonNull Set<Permission> permissions) {

}
