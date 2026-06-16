package com.bikemmerce.commerce.infrastructure.config.beans;

import com.bikemmerce.commerce.application.usecases.product.*;
import com.bikemmerce.commerce.domain.ports.ProductRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductUseCasesConfig {

    @Bean
    public CreateProductUseCase createProductUseCase(ProductRepositoryPort productRepositoryPort) {
        return new CreateProductUseCase(productRepositoryPort);
    }

    @Bean
    public GetProductUseCase getProductUseCase(ProductRepositoryPort productRepositoryPort) {
        return new GetProductUseCase(productRepositoryPort);
    }

    @Bean
    public DeleteProductUseCase deleteProductUseCase(ProductRepositoryPort productRepositoryPort) {
        return new DeleteProductUseCase(productRepositoryPort);
    }

    @Bean
    public ListProductUseCase listProductUseCase(ProductRepositoryPort productRepositoryPort) {
        return new ListProductUseCase(productRepositoryPort);
    }

    @Bean
    public UpdateProductUseCase updateProductUseCase(ProductRepositoryPort productRepositoryPort) {
        return new UpdateProductUseCase(productRepositoryPort);
    }
}