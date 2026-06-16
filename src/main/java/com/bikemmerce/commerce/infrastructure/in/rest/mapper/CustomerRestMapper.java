package com.bikemmerce.commerce.infrastructure.in.rest.mapper;

import com.bikemmerce.commerce.application.usecases.commands.CreateCustomerCommand;
import com.bikemmerce.commerce.application.usecases.commands.UpdateCustomerCommand;
import com.bikemmerce.commerce.domain.model.Customer;
import com.bikemmerce.commerce.infrastructure.in.rest.dto.customer.CustomerRestResponse;
import com.bikemmerce.commerce.infrastructure.in.rest.dto.customer.UpdateCustomerRestRequest;
import com.bikemmerce.commerce.infrastructure.in.rest.dto.product.CreateCustomerRestRequest;

public class CustomerRestMapper {

    public static CreateCustomerCommand toCreateCustomerCommand(CreateCustomerRestRequest request) {
        return new CreateCustomerCommand(request.name(), request.email());
    }

    public static UpdateCustomerCommand toUpdateCustomerCommand(UpdateCustomerRestRequest request) {
        return new UpdateCustomerCommand(request.id(), request.name(), request.email());
    }

    public static CustomerRestResponse toResponse(Customer customer) {
        return new CustomerRestResponse(
                customer.getCustomerId().value(), customer.getName(), customer.getEmail().value());
    }
}
