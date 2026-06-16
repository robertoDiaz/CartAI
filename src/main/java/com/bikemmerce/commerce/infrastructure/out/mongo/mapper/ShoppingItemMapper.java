package com.bikemmerce.commerce.infrastructure.out.mongo.mapper;

import com.bikemmerce.commerce.domain.model.ShoppingItem;
import com.bikemmerce.commerce.domain.model.value.objects.ProductId;
import com.bikemmerce.commerce.infrastructure.out.mongo.documents.dto.ShoppingItemDocument;

public class ShoppingItemMapper {

    public static ShoppingItemDocument toDocument(ShoppingItem shoppingItem) {
        return new ShoppingItemDocument(
                shoppingItem.getProductId().value(), shoppingItem.getCount(), shoppingItem.getUnitPrice());
    }

    public static ShoppingItem toDomain(ShoppingItemDocument shoppingItemDocument) {
        return new ShoppingItem(
                new ProductId(shoppingItemDocument.id()), shoppingItemDocument.count(),
                shoppingItemDocument.unitPrice());
    }

}
