/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.domain.ports.storage;

import cart.ai.shopping.domain.model.storage.vos.StoredFileEvent;

/**
 * @author Roberto Díaz
 */
public interface StoredFileEventPublisherPort {

    void deletionConfirmed(StoredFileEvent event);
}
