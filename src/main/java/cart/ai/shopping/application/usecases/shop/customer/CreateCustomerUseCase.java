/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.shop.customer;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.application.usecases.shop.commands.CreateCustomerCommand;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.model.identity.vos.Email;
import cart.ai.shopping.domain.model.identity.vos.UserId;
import cart.ai.shopping.domain.model.shop.Customer;
import cart.ai.shopping.domain.model.shop.vos.CustomerAddedEvent;
import cart.ai.shopping.domain.ports.common.IncrementIdGeneratorPort;
import cart.ai.shopping.domain.ports.shop.CustomerAddedEventPublisherPort;
import cart.ai.shopping.domain.ports.shop.CustomerRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author Roberto Díaz
 */
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
