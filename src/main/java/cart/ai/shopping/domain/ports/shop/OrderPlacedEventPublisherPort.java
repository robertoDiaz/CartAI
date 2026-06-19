/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.domain.ports.shop;

import cart.ai.shopping.domain.model.shop.vos.OrderPlacedEvent;

/**
 * @author Roberto Díaz
 */
public interface OrderPlacedEventPublisherPort {
    void publish(OrderPlacedEvent event);
}
