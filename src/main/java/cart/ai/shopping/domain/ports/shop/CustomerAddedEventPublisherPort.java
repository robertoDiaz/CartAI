/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.domain.ports.shop;

import cart.ai.shopping.domain.model.shop.vos.CustomerAddedEvent;

/**
 * @author Roberto Díaz
 */
public interface CustomerAddedEventPublisherPort {
    void added(CustomerAddedEvent event);
}
