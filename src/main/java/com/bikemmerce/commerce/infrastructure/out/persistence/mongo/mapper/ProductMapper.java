package com.bikemmerce.commerce.infrastructure.out.persistence.mongo.mapper;

import com.bikemmerce.commerce.domain.model.Product;
import com.bikemmerce.commerce.domain.model.value.objects.ProductId;
import com.bikemmerce.commerce.infrastructure.out.persistence.mongo.documents.ProductDocument;

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
