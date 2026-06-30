/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.bootstrap.initializers;

import cart.ai.shopping.domain.model.shop.Product;
import cart.ai.shopping.domain.ports.common.IncrementIdGeneratorPort;
import cart.ai.shopping.domain.ports.shop.ProductRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ProductInitializer.
 *
 * @author Roberto Díaz
 */
class ProductInitializerTest {

    private ProductRepositoryPort productRepositoryPort;
    private IncrementIdGeneratorPort idGeneratorPort;
    private ProductInitializer productInitializer;

    @BeforeEach
    void setUp() {
        productRepositoryPort = mock(ProductRepositoryPort.class);
        idGeneratorPort = mock(IncrementIdGeneratorPort.class);
        productInitializer = new ProductInitializer(productRepositoryPort, idGeneratorPort);
    }

    @Test
    void shouldNotSeedProductsIfDatabaseNotEmpty() {
        // Arrange
        Product existingProduct = mock(Product.class);
        when(productRepositoryPort.findAll()).thenReturn(List.of(existingProduct));

        // Act
        productInitializer.initializeProducts();

        // Assert
        verify(productRepositoryPort, never()).save(any(Product.class));
    }

    @Test
    void shouldSeedProductsIfDatabaseIsEmpty() {
        // Arrange
        when(productRepositoryPort.findAll()).thenReturn(Collections.emptyList());
        when(idGeneratorPort.generate(Product.class)).thenReturn("prod-1", "prod-2", "prod-3", "prod-4", "prod-5");

        // Act
        productInitializer.initializeProducts();

        // Assert
        verify(productRepositoryPort, times(5)).save(any(Product.class));
    }
}
