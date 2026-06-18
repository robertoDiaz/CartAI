package cart.ai.shopping.infrastructure.out.persistence.mongo.shop.adapters;

import cart.ai.shopping.domain.model.security.value.objects.Email;
import cart.ai.shopping.domain.model.security.value.objects.UserId;
import cart.ai.shopping.domain.model.shop.Customer;
import cart.ai.shopping.domain.ports.shop.repositories.CustomerRepositoryPort;
import cart.ai.shopping.infrastructure.out.persistence.mongo.shop.CustomerMongoRepository;
import cart.ai.shopping.infrastructure.out.persistence.mongo.shop.mapper.CustomerMapper;
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
