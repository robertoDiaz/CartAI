package com.bikemmerce.commerce.infrastructure.config.beans;

import com.bikemmerce.commerce.application.usecases.cart.AddShoppingItemToCartUseCase;
import com.bikemmerce.commerce.application.usecases.cart.ClearCartUseCase;
import com.bikemmerce.commerce.application.usecases.cart.GetCartUseCase;
import com.bikemmerce.commerce.application.usecases.cart.RemoveShoppingItemFromCartUseCase;
import com.bikemmerce.commerce.domain.ports.CartRepositoryPort;
import com.bikemmerce.commerce.domain.ports.ProductRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CartUseCasesConfig {

    @Bean
    public AddShoppingItemToCartUseCase addShoppingItemToCartUseCase(
            CartRepositoryPort cartRepositoryPort, ProductRepositoryPort productRepositoryPort) {

        return new AddShoppingItemToCartUseCase(cartRepositoryPort, productRepositoryPort);
    }

    @Bean
    public GetCartUseCase getCartUseCase(CartRepositoryPort cartRepositoryPort) {
        return new GetCartUseCase(cartRepositoryPort);
    }

    @Bean
    public ClearCartUseCase clearCartUseCase(CartRepositoryPort cartRepositoryPort) {
        return new ClearCartUseCase(cartRepositoryPort);
    }

    @Bean
    public RemoveShoppingItemFromCartUseCase RemoveShoppingItemFromCartUseCase(
            CartRepositoryPort cartRepositoryPort, ProductRepositoryPort productRepositoryPort) {

        return new RemoveShoppingItemFromCartUseCase(cartRepositoryPort, productRepositoryPort);
    }

}