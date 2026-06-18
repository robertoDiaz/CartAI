package cart.ai.shopping.infrastructure.out.persistence.mongo.shop;

import cart.ai.shopping.infrastructure.out.persistence.mongo.shop.documents.ProductDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductMongoRepository extends MongoRepository<ProductDocument, String> {
}
