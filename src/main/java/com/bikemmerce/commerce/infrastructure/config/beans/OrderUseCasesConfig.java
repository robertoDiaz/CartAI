package com.bikemmerce.commerce.infrastructure.config.beans;

import com.bikemmerce.commerce.application.usecases.order.CancelOrderUseCase;
import com.bikemmerce.commerce.application.usecases.order.CreateOrderUseCase;
import com.bikemmerce.commerce.application.usecases.order.GetOrderUseCase;
import com.bikemmerce.commerce.domain.ports.CartRepositoryPort;
import com.bikemmerce.commerce.domain.ports.IncrementIdGeneratorPort;
import com.bikemmerce.commerce.domain.ports.OrderRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderUseCasesConfig {

    @Bean
    public CancelOrderUseCase cancelOrderUseCase(OrderRepositoryPort orderRepositoryPort) {
        return new CancelOrderUseCase(orderRepositoryPort);
    }

    @Bean
    public GetOrderUseCase getOrderUseCase(OrderRepositoryPort orderRepositoryPort) {
        return new GetOrderUseCase(orderRepositoryPort);
    }

    @Bean
    public CreateOrderUseCase createOrderUseCase(
            CartRepositoryPort cartRepositoryPort, OrderRepositoryPort orderRepositoryPort,
            IncrementIdGeneratorPort incrementIdGeneratorPort) {

        return new CreateOrderUseCase(cartRepositoryPort, orderRepositoryPort, incrementIdGeneratorPort);
    }

}