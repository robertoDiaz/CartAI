/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.domain.model.identity;

import cart.ai.shopping.domain.model.identity.vos.Email;
import cart.ai.shopping.domain.model.identity.vos.UserId;
import lombok.NonNull;

import java.util.Set;

/**
 * @author Roberto Díaz
 */
public record User(@NonNull UserId userId, @NonNull String name, @NonNull Email email, @NonNull String passwordHash,
                   @NonNull Set<Role> roles) {

}
