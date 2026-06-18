package cart.ai.shopping.infrastructure.out.persistence.mongo.common.documents;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Builder
@Data
@Document("outbox_transaction")
public class OutboxTransactionDocument {
    public static final int PENDING = 0;
    public static final int SUCCESS = 1;
    public static final int FAIL = 2;
    public static final int PROCESSING = 3;
    @Id
    private final String id;
    private String aggregateType;
    private String aggregateId;
    private String key;
    private String topic;
    private String payload;
    private int status;
    private Date createdDate;
}
