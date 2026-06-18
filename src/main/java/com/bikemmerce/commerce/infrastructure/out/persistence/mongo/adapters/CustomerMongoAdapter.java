package com.bikemmerce.commerce.infrastructure.out.persistence.mongo.adapters;

import com.bikemmerce.commerce.domain.model.Customer;
import com.bikemmerce.commerce.domain.model.value.objects.CustomerId;
import com.bikemmerce.commerce.domain.model.value.objects.Email;
import com.bikemmerce.commerce.domain.ports.CustomerRepositoryPort;
import com.bikemmerce.commerce.infrastructure.out.persistence.mongo.CustomerMongoRepository;
import com.bikemmerce.commerce.infrastructure.out.persistence.mongo.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomerMongoAdapter implements CustomerRepositoryPort {

    private final CustomerMongoRepository customerMongoRepository;

    @Override
    public void delete(CustomerId customerId) {
        customerMongoRepository.deleteById(customerId.value());
    }

    @Override
    public Customer findByCustomerId(CustomerId customerId) {
        return customerMongoRepository.findById(customerId.value()).map(CustomerMapper::toDomain).orElse(null);
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
