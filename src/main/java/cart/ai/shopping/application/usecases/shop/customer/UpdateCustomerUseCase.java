package cart.ai.shopping.application.usecases.shop.customer;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.application.usecases.shop.commands.UpdateCustomerCommand;
import cart.ai.shopping.domain.model.security.value.objects.Email;
import cart.ai.shopping.domain.model.security.value.objects.UserId;
import cart.ai.shopping.domain.model.shop.Customer;
import cart.ai.shopping.domain.ports.shop.repositories.CustomerRepositoryPort;
import cart.ai.shopping.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@UseCase

public class UpdateCustomerUseCase {

    private final CustomerRepositoryPort customerRepositoryPort;

    public Result<Customer> execute(UpdateCustomerCommand command) {
        UserId userId = new UserId(command.id());
        Email email = new Email(command.email());

        if (customerRepositoryPort.findByEmail(email) != null) {
            return Result.error(HttpStatus.CONFLICT.value());
        }

        Customer customer = new Customer(userId, command.name(), email);

        return Result.success(customerRepositoryPort.save(customer));
    }
}
