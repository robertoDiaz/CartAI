/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.out.persistence.mongo.shop.mappers;

import cart.ai.shopping.domain.model.shop.Product;
import cart.ai.shopping.domain.model.shop.vos.ProductId;
import cart.ai.shopping.infrastructure.out.persistence.mongo.shop.documents.ProductDocument;

/**
 * @author Roberto Díaz
 */
public class ProductMapper {

    public static ProductDocument toProductDocument(Product product) {
        return ProductDocument.builder()
                .id(product.getId().value())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .build();
    }

    public static Product toProduct(ProductDocument productDocument) {
        return new Product(
                new ProductId(productDocument.getId()),
                productDocument.getName(),
                productDocument.getDescription(),
                productDocument.getPrice(),
                productDocument.getStock()
        );
    }
}
