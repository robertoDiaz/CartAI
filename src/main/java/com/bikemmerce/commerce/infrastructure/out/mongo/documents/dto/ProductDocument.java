package com.bikemmerce.commerce.infrastructure.out.mongo.documents.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document("products")
@Data
public class ProductDocument {

    @Id
    private final String id;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final Integer stock;

}
