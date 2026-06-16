package com.bikemmerce.commerce.infrastructure.config.beans;

import com.bikemmerce.commerce.application.usecases.customer.CreateCustomerUseCase;
import com.bikemmerce.commerce.application.usecases.customer.GetCustomerUseCase;
import com.bikemmerce.commerce.application.usecases.customer.UpdateCustomerUseCase;
import com.bikemmerce.commerce.domain.ports.CustomerRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerUseCasesConfig {

    @Bean
    public CreateCustomerUseCase createCustomerUseCase(CustomerRepositoryPort customerRepositoryPort) {
        return new CreateCustomerUseCase(customerRepositoryPort);
    }

    @Bean
    public GetCustomerUseCase getCustomerUseCase(CustomerRepositoryPort customerRepositoryPort) {
        return new GetCustomerUseCase(customerRepositoryPort);
    }

    @Bean
    public UpdateCustomerUseCase udpateCustomerUseCase(CustomerRepositoryPort customerRepositoryPort) {
        return new UpdateCustomerUseCase(customerRepositoryPort);
    }

}