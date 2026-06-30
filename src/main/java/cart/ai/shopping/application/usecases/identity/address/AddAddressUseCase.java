/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.identity.address;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.application.usecases.identity.address.commands.AddAddressCommand;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.model.identity.Address;
import cart.ai.shopping.domain.model.identity.vos.AddressId;
import cart.ai.shopping.domain.ports.identity.AddressRepositoryPort;
import cart.ai.shopping.domain.ports.common.IncrementIdGeneratorPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author Roberto Díaz
 */
@UseCase
@RequiredArgsConstructor
public class AddAddressUseCase {

    private final AddressRepositoryPort addressRepositoryPort;
    private final IncrementIdGeneratorPort idGeneratorPort;

    public Result<Address> execute(AddAddressCommand command) {
        
        if (command.isDefault()) {
            resetDefaultAddresses(command.ownerId(), command.ownerType());
        } else {
            // If it's the first address, make it default automatically
            List<Address> existing = addressRepositoryPort.findByOwnerIdAndOwnerType(command.ownerId(), command.ownerType());
            if (existing.isEmpty()) {
                // we can't modify the command directly as it's a record, we'll handle it during Address creation
            }
        }
        
        boolean makeDefault = command.isDefault();
        if (!makeDefault) {
             List<Address> existing = addressRepositoryPort.findByOwnerIdAndOwnerType(command.ownerId(), command.ownerType());
             if (existing.isEmpty()) {
                 makeDefault = true;
             }
        }

        Address address = new Address(
                new AddressId(String.valueOf(idGeneratorPort.generate(Address.class))),
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

        return Result.success(addressRepositoryPort.save(address));
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
