package com.bikemmerce.commerce.infrastructure.out.persistence.mongo.shop.mapper;

import com.bikemmerce.commerce.domain.model.shop.value.objects.ProductId;
import com.bikemmerce.commerce.domain.model.shop.value.objects.ShoppingItem;
import com.bikemmerce.commerce.infrastructure.out.persistence.mongo.shop.documents.ShoppingItemDocument;

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
