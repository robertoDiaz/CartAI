/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.out.persistence.mongo.shop.adapters;

import cart.ai.shopping.domain.model.identity.vos.Email;
import cart.ai.shopping.domain.model.identity.vos.UserId;
import cart.ai.shopping.domain.model.shop.Customer;
import cart.ai.shopping.domain.ports.shop.CustomerRepositoryPort;
import cart.ai.shopping.infrastructure.out.persistence.mongo.shop.mappers.CustomerMapper;
import cart.ai.shopping.infrastructure.out.persistence.mongo.shop.repositories.CustomerMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Roberto Díaz
 */
@Component
@RequiredArgsConstructor
public class CustomerMongoAdapter implements CustomerRepositoryPort {

    private final CustomerMongoRepository customerMongoRepository;

    @Override
    public void delete(UserId userId) {
        customerMongoRepository.deleteById(userId.value());
    }

    @Override
    public Customer findByCustomerId(UserId userId) {
        return customerMongoRepository.findById(userId.value()).map(CustomerMapper::toDomain).orElse(null);
    }

    @Override
    public Customer findByEmail(Email email) {
        return customerMongoRepository.findByEmail(email).map(CustomerMapper::toDomain).orElse(null);
    }

    @Override
    public List<Customer> findAll() {
        return customerMongoRepository.findAll().stream().map(CustomerMapper::toDomain).toList();
    }

    @Override
    public Customer save(Customer customer) {
        return CustomerMapper.toDomain(customerMongoRepository.save(CustomerMapper.toDocument(customer)));
    }

}
