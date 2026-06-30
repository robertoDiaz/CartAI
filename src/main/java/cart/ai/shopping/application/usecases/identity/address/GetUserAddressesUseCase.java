/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.identity.address;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.domain.model.identity.Address;
import cart.ai.shopping.domain.ports.identity.AddressRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author Roberto Díaz
 */
@UseCase
@RequiredArgsConstructor
public class GetUserAddressesUseCase {

    private final AddressRepositoryPort addressRepositoryPort;

    public List<Address> execute(String ownerId) {
        return addressRepositoryPort.findByOwnerIdAndOwnerType(ownerId, "USER");
    }
}
