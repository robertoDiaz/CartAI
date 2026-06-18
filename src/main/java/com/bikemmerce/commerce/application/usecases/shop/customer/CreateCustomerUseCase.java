package com.bikemmerce.commerce.application.usecases.shop.customer;

import com.bikemmerce.commerce.application.annotations.UseCase;
import com.bikemmerce.commerce.application.usecases.shop.commands.CreateCustomerCommand;
import com.bikemmerce.commerce.domain.model.security.value.objects.Email;
import com.bikemmerce.commerce.domain.model.security.value.objects.UserId;
import com.bikemmerce.commerce.domain.model.shop.Customer;
import com.bikemmerce.commerce.domain.model.shop.value.objects.CustomerAddedEvent;
import com.bikemmerce.commerce.domain.ports.common.IncrementIdGeneratorPort;
import com.bikemmerce.commerce.domain.ports.shop.CustomerRepositoryPort;
import com.bikemmerce.commerce.domain.ports.shop.events.CustomerAddedEventPublisherPort;
import com.bikemmerce.commerce.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@UseCase
public class CreateCustomerUseCase {

    private final CustomerRepositoryPort customerRepositoryPort;
    private final CustomerAddedEventPublisherPort customerAddedEventPublisherPort;
    private final IncrementIdGeneratorPort incrementIdGeneratorPort;

    public Result<Customer> execute(CreateCustomerCommand command) {
        UserId userId = new UserId(incrementIdGeneratorPort.generate(Customer.class));

        Email email = new Email(command.email());

        if (customerRepositoryPort.findByEmail(email) != null) {
            return Result.error(HttpStatus.CONFLICT.value());
        }

        Customer customer = customerRepositoryPort.save(
                new Customer(userId, command.name(), email));

        customerAddedEventPublisherPort.added(
                new CustomerAddedEvent(userId, customer.name(), customer.email()));

        return Result.success(customerRepositoryPort.save(customer));
    }
}
