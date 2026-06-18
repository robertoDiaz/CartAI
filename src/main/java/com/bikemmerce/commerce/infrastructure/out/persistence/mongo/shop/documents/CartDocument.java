package com.bikemmerce.commerce.infrastructure.out.persistence.mongo.shop.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("cart")
@Data
public class CartDocument {

    @Id
    private final String customerId;

    private final List<ShoppingItemDocument> shoppingItems;

}
