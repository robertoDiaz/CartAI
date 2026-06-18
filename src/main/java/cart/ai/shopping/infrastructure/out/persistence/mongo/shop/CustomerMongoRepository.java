package cart.ai.shopping.infrastructure.out.persistence.mongo.shop;

import cart.ai.shopping.domain.model.security.value.objects.Email;
import cart.ai.shopping.infrastructure.out.persistence.mongo.shop.documents.CustomerDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerMongoRepository extends MongoRepository<CustomerDocument, String> {

    Optional<CustomerDocument> findByEmail(Email email);

}
