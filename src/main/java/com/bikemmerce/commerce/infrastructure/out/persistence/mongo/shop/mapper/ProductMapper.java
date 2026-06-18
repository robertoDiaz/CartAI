package com.bikemmerce.commerce.infrastructure.out.persistence.mongo.shop.mapper;

import com.bikemmerce.commerce.domain.model.shop.Product;
import com.bikemmerce.commerce.domain.model.shop.value.objects.ProductId;
import com.bikemmerce.commerce.infrastructure.out.persistence.mongo.shop.documents.ProductDocument;

public class ProductMapper {

    public static ProductDocument toProductDocument(Product product) {
        return new ProductDocument(
                product.getId().value(), product.getName(), product.getDescription(),
                product.getPrice(), product.getStock());

    }

    public static Product toProduct(ProductDocument productDocument) {
        return new Product(
                new ProductId(productDocument.getId()), productDocument.getName(), productDocument.getDescription(), productDocument.getPrice(), productDocument.getStock()
        );
    }

}
