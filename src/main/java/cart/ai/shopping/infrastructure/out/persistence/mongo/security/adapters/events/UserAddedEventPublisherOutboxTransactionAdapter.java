package cart.ai.shopping.infrastructure.out.persistence.mongo.security.adapters.events;

import cart.ai.shopping.domain.model.security.User;
import cart.ai.shopping.domain.model.security.value.objects.UserAddedEvent;
import cart.ai.shopping.domain.ports.security.events.UserAddedEventPublisherPort;
import cart.ai.shopping.infrastructure.out.persistence.mongo.common.documents.OutboxTransactionDocument;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserAddedEventPublisherOutboxTransactionAdapter implements UserAddedEventPublisherPort {

    private static final String TOPIC = "users-topic";
    private final MongoTemplate mongoTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void added(UserAddedEvent event) {
        try {
            OutboxTransactionDocument outboxTransactionDocument = OutboxTransactionDocument.builder()
                    .aggregateType(User.class.getSimpleName().toLowerCase())
                    .aggregateId(event.userId().value())
                    .key(event.userId().value())
                    .topic(TOPIC)
                    .payload(objectMapper.writeValueAsString(event))
                    .status(OutboxTransactionDocument.PENDING)
                    .createdDate(new Date())
                    .build();

            mongoTemplate.save(outboxTransactionDocument);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
