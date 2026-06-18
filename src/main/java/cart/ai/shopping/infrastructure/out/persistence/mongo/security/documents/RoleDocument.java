package cart.ai.shopping.infrastructure.out.persistence.mongo.security.documents;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document("roles")
@Data
@Builder
public class RoleDocument {

    @Id
    private final String id;

    private final String name;

    private final Set<String> permissions;

}
