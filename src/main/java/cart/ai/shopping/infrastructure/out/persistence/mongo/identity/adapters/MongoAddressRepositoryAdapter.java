/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.out.persistence.mongo.identity.adapters;

import cart.ai.shopping.domain.model.identity.Address;
import cart.ai.shopping.domain.model.identity.vos.AddressId;
import cart.ai.shopping.domain.ports.identity.AddressRepositoryPort;
import cart.ai.shopping.infrastructure.out.persistence.mongo.identity.mappers.AddressMapper;
import cart.ai.shopping.infrastructure.out.persistence.mongo.identity.repositories.SpringDataAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Roberto Díaz
 */
@Component
@RequiredArgsConstructor
public class MongoAddressRepositoryAdapter implements AddressRepositoryPort {

    private final SpringDataAddressRepository repository;

    @Override
    public Address save(Address address) {
        return AddressMapper.toDomain(repository.save(AddressMapper.toDocument(address)));
    }

    @Override
    public Optional<Address> findById(AddressId id) {
        return repository.findById(id.value()).map(AddressMapper::toDomain);
    }

    @Override
    public List<Address> findByOwnerIdAndOwnerType(String ownerId, String ownerType) {
        return repository.findByOwnerIdAndOwnerType(ownerId, ownerType).stream()
                .map(AddressMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(AddressId id) {
        repository.deleteById(id.value());
    }
}
