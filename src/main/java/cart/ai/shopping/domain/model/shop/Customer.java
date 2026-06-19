/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.domain.model.shop;

import cart.ai.shopping.domain.model.identity.vos.Email;
import cart.ai.shopping.domain.model.identity.vos.UserId;
import lombok.NonNull;

/**
 * @author Roberto Díaz
 */
public record Customer(@NonNull UserId userId, @NonNull String name, @NonNull Email email) {

}
