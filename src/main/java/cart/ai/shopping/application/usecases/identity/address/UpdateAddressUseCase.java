/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.identity.address;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.application.usecases.identity.address.commands.UpdateAddressCommand;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.common.result.ResultError;
import cart.ai.shopping.domain.model.identity.Address;
import cart.ai.shopping.domain.model.identity.vos.AddressId;
import cart.ai.shopping.domain.ports.identity.AddressRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

/**
 * @author Roberto Díaz
 */
@UseCase
@RequiredArgsConstructor
public class UpdateAddressUseCase {

    private final AddressRepositoryPort addressRepositoryPort;

    public Result<Address> execute(UpdateAddressCommand command) {
        
        Optional<Address> existingOpt = addressRepositoryPort.findById(new AddressId(command.id()));
        if (existingOpt.isEmpty()) {
            return Result.error(ResultError.NOT_FOUND);
        }
        
        Address existing = existingOpt.get();
        if (!existing.ownerId().equals(command.ownerId()) || !existing.ownerType().equals(command.ownerType())) {
            return Result.error(ResultError.FORBIDDEN);
        }
        
        boolean makeDefault = command.isDefault();
        if (makeDefault && !existing.isDefault()) {
            resetDefaultAddresses(command.ownerId(), command.ownerType());
        } else if (!makeDefault && existing.isDefault()) {
            // Cannot unset default if it's the only one, maybe just let it be false if they have others.
            // A better approach is to force at least one default. If they unset default, we could just ignore or auto-set another.
            // Let's just allow unsetting it, or enforce they must select another one to be default.
            // For simplicity, we allow it.
        }

        Address updated = new Address(
                existing.id(),
                command.ownerId(),
                command.ownerType(),
                command.alias(),
                command.firstName(),
                command.lastName(),
                command.company(),
                command.street(),
                command.city(),
                command.zipCode(),
                command.phone(),
                command.state(),
                command.country(),
                command.notes(),
                makeDefault
        );

        return Result.success(addressRepositoryPort.save(updated));
    }

    private void resetDefaultAddresses(String ownerId, String ownerType) {
        List<Address> addresses = addressRepositoryPort.findByOwnerIdAndOwnerType(ownerId, ownerType);
        for (Address addr : addresses) {
            if (addr.isDefault()) {
                Address updated = new Address(
                        addr.id(), addr.ownerId(), addr.ownerType(), addr.alias(),
                        addr.firstName(), addr.lastName(), addr.company(),
                        addr.street(), addr.city(), addr.zipCode(), addr.phone(),
                        addr.state(), addr.country(), addr.notes(), false
                );
                addressRepositoryPort.save(updated);
            }
        }
    }
}
