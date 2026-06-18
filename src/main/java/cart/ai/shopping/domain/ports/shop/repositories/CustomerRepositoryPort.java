package cart.ai.shopping.domain.ports.shop.repositories;

import cart.ai.shopping.domain.model.security.value.objects.Email;
import cart.ai.shopping.domain.model.security.value.objects.UserId;
import cart.ai.shopping.domain.model.shop.Customer;

import java.util.List;

public interface CustomerRepositoryPort {

    void delete(UserId userId);

    Customer findByCustomerId(UserId userId);

    Customer findByEmail(Email email);

    List<Customer> findAll();

    Customer save(Customer product);

}