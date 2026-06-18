package com.bikemmerce.commerce.application.usecases.shop.customer;

import com.bikemmerce.commerce.application.annotations.UseCase;
import com.bikemmerce.commerce.domain.model.security.value.objects.Email;
import com.bikemmerce.commerce.domain.model.shop.Customer;
import com.bikemmerce.commerce.domain.ports.shop.CustomerRepositoryPort;
import com.bikemmerce.commerce.domain.result.Result;
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
