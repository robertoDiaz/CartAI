package cart.ai.shopping.application.usecases.shop.customer;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.domain.model.security.value.objects.UserId;
import cart.ai.shopping.domain.model.shop.Customer;
import cart.ai.shopping.domain.ports.shop.repositories.CustomerRepositoryPort;
import cart.ai.shopping.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@UseCase
public class GetCustomerUseCase {

    private final CustomerRepositoryPort customerRepositoryPort;

    public Result<Customer> execute(UserId userId) {
        Customer customer = customerRepositoryPort.findByCustomerId(userId);

        if (customer == null) {
            return Result.error(HttpStatus.NOT_FOUND.value());
        }

        return Result.success(customer);
    }
}