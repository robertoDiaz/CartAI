package cart.ai.shopping.infrastructure.out.persistence.mongo.shop.documents;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("customers")
@Data
@Builder
public class CustomerDocument {

    @Id
    private final String id;

    private final String name;

    private final String email;

}
