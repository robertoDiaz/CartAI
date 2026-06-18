package com.bikemmerce.commerce.infrastructure.out.persistence.mongo.documents;

import java.math.BigDecimal;

public record ShoppingItemDocument(String id, Integer count, BigDecimal unitPrice) {

}
