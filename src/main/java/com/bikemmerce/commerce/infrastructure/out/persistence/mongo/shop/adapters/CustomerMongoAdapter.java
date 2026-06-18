package com.bikemmerce.commerce.infrastructure.out.persistence.mongo.shop.adapters;

import com.bikemmerce.commerce.domain.model.security.value.objects.Email;
import com.bikemmerce.commerce.domain.model.security.value.objects.UserId;
import com.bikemmerce.commerce.domain.model.shop.Customer;
import com.bikemmerce.commerce.domain.ports.shop.CustomerRepositoryPort;
import com.bikemmerce.commerce.infrastructure.out.persistence.mongo.shop.CustomerMongoRepository;
import com.bikemmerce.commerce.infrastructure.out.persistence.mongo.shop.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

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
