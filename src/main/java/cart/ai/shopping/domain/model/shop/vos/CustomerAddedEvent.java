/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.domain.model.shop.vos;

import cart.ai.shopping.domain.model.identity.vos.Email;
import cart.ai.shopping.domain.model.identity.vos.UserId;

/**
 * @author Roberto Díaz
 */
public record CustomerAddedEvent(
        UserId userId,
        String name,
        Email email
) {
}
