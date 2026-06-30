/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.out.persistence.mongo.identity.mappers;

import cart.ai.shopping.domain.model.identity.Address;
import cart.ai.shopping.domain.model.identity.vos.AddressId;
import cart.ai.shopping.infrastructure.out.persistence.mongo.identity.documents.AddressDocument;

/**
 * @author Roberto Díaz
 */
public class AddressMapper {

    public static AddressDocument toDocument(Address address) {
        return AddressDocument.builder()
                .id(address.id() != null ? address.id().value() : null)
                .ownerId(address.ownerId())
                .ownerType(address.ownerType())
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

    public static Address toDomain(AddressDocument document) {
        return new Address(
                new AddressId(document.getId()),
                document.getOwnerId(),
                document.getOwnerType(),
                document.getAlias(),
                document.getFirstName(),
                document.getLastName(),
                document.getCompany(),
                document.getStreet(),
                document.getCity(),
                document.getZipCode(),
                document.getPhone(),
                document.getState(),
                document.getCountry(),
                document.getNotes(),
                document.isDefault()
        );
    }
}
