package com.bikemmerce.commerce.infrastructure.out.persistence.mongo.shop.mapper;

import com.bikemmerce.commerce.domain.model.shop.Product;
import com.bikemmerce.commerce.domain.model.shop.value.objects.ProductId;
import com.bikemmerce.commerce.infrastructure.out.persistence.mongo.shop.documents.ProductDocument;

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
