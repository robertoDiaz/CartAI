package com.bikemmerce.commerce.infrastructure.out.mongo.documents.dto;

import java.math.BigDecimal;

public record ShoppingItemDocument(String id, Integer count, BigDecimal unitPrice) {

}
