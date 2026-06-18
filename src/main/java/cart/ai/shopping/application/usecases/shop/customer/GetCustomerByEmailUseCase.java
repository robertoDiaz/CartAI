package cart.ai.shopping.application.usecases.shop.customer;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.domain.model.security.value.objects.Email;
import cart.ai.shopping.domain.model.shop.Customer;
import cart.ai.shopping.domain.ports.shop.repositories.CustomerRepositoryPort;
import cart.ai.shopping.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@UseCase
public class GetCustomerByEmailUseCase {

    private final CustomerRepositoryPort customerRepositoryPort;

    public Result<Customer> execute(Email email) {
        Customer customer = customerRepositoryPort.findByEmail(email);

        if (customer == null) {
            return Result.error(HttpStatus.NOT_FOUND.value());
        }

        return Result.success(customer);
    }
}
