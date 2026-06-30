/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.identity.address;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.common.result.ResultError;
import cart.ai.shopping.domain.model.identity.Address;
import cart.ai.shopping.domain.model.identity.vos.AddressId;
import cart.ai.shopping.domain.ports.identity.AddressRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * @author Roberto Díaz
 */
@UseCase
@RequiredArgsConstructor
public class DeleteAddressUseCase {

    private final AddressRepositoryPort addressRepositoryPort;

    public Result<Void> execute(String id, String ownerId, String ownerType) {
        Optional<Address> existingOpt = addressRepositoryPort.findById(new AddressId(id));
        if (existingOpt.isEmpty()) {
            return Result.error(ResultError.NOT_FOUND);
        }
        
        Address existing = existingOpt.get();
        if (!existing.ownerId().equals(ownerId) || !existing.ownerType().equals(ownerType)) {
            return Result.error(ResultError.FORBIDDEN);
        }

        addressRepositoryPort.deleteById(existing.id());
        return Result.success(null);
    }
}
