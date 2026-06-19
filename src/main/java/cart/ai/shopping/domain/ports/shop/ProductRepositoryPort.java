/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.domain.ports.shop;

import cart.ai.shopping.domain.model.shop.Product;
import cart.ai.shopping.domain.model.shop.vos.ProductId;

import java.util.List;

/**
 * @author Roberto Díaz
 */
public interface ProductRepositoryPort {

    void delete(ProductId productId);

    Product find(ProductId productId);

    List<Product> findAll();

    Product save(Product product);
}
