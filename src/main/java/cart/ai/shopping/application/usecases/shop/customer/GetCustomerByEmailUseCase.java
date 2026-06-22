/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.shop.customer;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.model.identity.vos.Email;
import cart.ai.shopping.domain.model.shop.Customer;
import cart.ai.shopping.domain.ports.shop.CustomerRepositoryPort;
import lombok.RequiredArgsConstructor;

import static cart.ai.shopping.domain.common.result.ResultError.NOT_FOUND;

/**
 * @author Roberto Díaz
 */
@RequiredArgsConstructor
@UseCase
public class GetCustomerByEmailUseCase {

    private final CustomerRepositoryPort customerRepositoryPort;

    public Result<Customer> execute(Email email) {
        Customer customer = customerRepositoryPort.findByEmail(email);

        if (customer == null) {
            return Result.error(NOT_FOUND);
        }

        return Result.success(customer);
    }
}
