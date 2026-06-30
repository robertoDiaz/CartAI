/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.identity.mappers.address;

import cart.ai.shopping.application.usecases.identity.address.commands.AddAddressCommand;
import cart.ai.shopping.application.usecases.identity.address.commands.UpdateAddressCommand;
import cart.ai.shopping.domain.model.identity.Address;
import cart.ai.shopping.infrastructure.in.rest.identity.dtos.address.AddressRestRequest;
import cart.ai.shopping.infrastructure.in.rest.identity.dtos.address.AddressRestResponse;

/**
 * @author Roberto Díaz
 */
public class AddressRestMapper {

    public static AddAddressCommand toAddCommand(String ownerId, String ownerType, AddressRestRequest request) {
        return AddAddressCommand.builder()
                .ownerId(ownerId)
                .ownerType(ownerType)
                .alias(request.alias())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .company(request.company())
                .street(request.street())
                .city(request.city())
                .zipCode(request.zipCode())
                .phone(request.phone())
                .state(request.state())
                .country(request.country())
                .notes(request.notes())
                .isDefault(request.isDefault())
                .build();
    }

    public static UpdateAddressCommand toUpdateCommand(String id, String ownerId, String ownerType, AddressRestRequest request) {
        return UpdateAddressCommand.builder()
                .id(id)
                .ownerId(ownerId)
                .ownerType(ownerType)
                .alias(request.alias())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .company(request.company())
                .street(request.street())
                .city(request.city())
                .zipCode(request.zipCode())
                .phone(request.phone())
                .state(request.state())
                .country(request.country())
                .notes(request.notes())
                .isDefault(request.isDefault())
                .build();
    }

    public static AddressRestResponse toResponse(Address address) {
        return AddressRestResponse.builder()
                .id(address.id().value())
                .alias(address.alias())
                .firstName(address.firstName())
                .lastName(address.lastName())
                .company(address.company())
                .street(address.street())
                .city(address.city())
                .zipCode(address.zipCode())
                .phone(address.phone())
                .state(address.state())
                .country(address.country())
                .notes(address.notes())
                .isDefault(address.isDefault())
                .build();
    }
}
