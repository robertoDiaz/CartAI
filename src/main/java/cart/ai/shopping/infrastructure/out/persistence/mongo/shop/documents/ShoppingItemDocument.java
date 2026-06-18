package cart.ai.shopping.infrastructure.out.persistence.mongo.shop.documents;

import java.math.BigDecimal;

public record ShoppingItemDocument(String id, Integer count, BigDecimal unitPrice) {

}
