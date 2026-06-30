/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.domain.ports.identity;

import cart.ai.shopping.domain.model.identity.Address;
import cart.ai.shopping.domain.model.identity.vos.AddressId;

import java.util.List;
import java.util.Optional;

/**
 * @author Roberto Díaz
 */
public interface AddressRepositoryPort {
    
    Address save(Address address);
    
    Optional<Address> findById(AddressId id);
    
    List<Address> findByOwnerIdAndOwnerType(String ownerId, String ownerType);
    
    void deleteById(AddressId id);
}
