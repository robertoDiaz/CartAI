package com.bikemmerce.commerce.infrastructure.out.mongo.documents.dto;

import com.bikemmerce.commerce.domain.model.constants.OrderStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document("order")
@Data
public class OrderDocument {

    @Id
    private final String orderId;
    private final String customerId;
    private final List<ShoppingItemDocument> shoppingItems;
    private final OrderStatus orderStatus;
    private final Date createDate;

}
