/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.shop.mappers;

import cart.ai.shopping.application.usecases.shop.commands.CreateCustomerCommand;
import cart.ai.shopping.application.usecases.shop.commands.UpdateCustomerCommand;
import cart.ai.shopping.domain.model.shop.Customer;
import cart.ai.shopping.infrastructure.in.rest.shop.dtos.CreateCustomerRestRequest;
import cart.ai.shopping.infrastructure.in.rest.shop.dtos.CustomerRestResponse;
import cart.ai.shopping.infrastructure.in.rest.shop.dtos.UpdateCustomerRestRequest;

/**
 * @author Roberto Díaz
 */
public class CustomerRestMapper {

    public static CreateCustomerCommand toCreateCustomerCommand(CreateCustomerRestRequest request) {
        return new CreateCustomerCommand(request.name(), request.email());
    }

    public static UpdateCustomerCommand toUpdateCustomerCommand(UpdateCustomerRestRequest request) {
        return new UpdateCustomerCommand(request.id(), request.name(), request.email());
    }

    public static CustomerRestResponse toResponse(Customer customer) {
        return new CustomerRestResponse(
                customer.userId().value(), customer.name(), customer.email().value());
    }
}
