package cart.ai.shopping.infrastructure.out.persistence.mongo.shop.adapters.events;

import cart.ai.shopping.domain.model.shop.Product;
import cart.ai.shopping.domain.model.shop.vos.ProductCreatedEvent;
import cart.ai.shopping.domain.model.shop.vos.ProductUpdatedEvent;
import cart.ai.shopping.domain.ports.shop.ProductEventPublisherPort;
import cart.ai.shopping.infrastructure.out.persistence.mongo.common.documents.OutboxTransactionDocument;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;

import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductEventPublisherOutboxTransactionAdapter implements ProductEventPublisherPort {

    private static final String PRODUCT_CREATED_TOPIC = "product-created-topic";
    private static final String PRODUCT_UPDATED_TOPIC = "product-updated-topic";
    private final MongoTemplate mongoTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void productCreated(ProductCreatedEvent event) {
        saveOutbox(event.productId(), event, PRODUCT_CREATED_TOPIC);
    }

    @Override
    public void productUpdated(ProductUpdatedEvent event) {
        saveOutbox(event.productId(), event, PRODUCT_UPDATED_TOPIC);
    }

    private void saveOutbox(String aggregateId, Object event, String topic) {
        try {
            OutboxTransactionDocument outboxTransactionDocument = OutboxTransactionDocument.builder()
                    .aggregateType(Product.class.getSimpleName().toLowerCase())
                    .aggregateId(aggregateId)
                    .key(aggregateId)
                    .topic(topic)
                    .payload(objectMapper.writeValueAsString(event))
                    .status(OutboxTransactionDocument.PENDING)
                    .createdDate(new Date())
                    .build();

            mongoTemplate.save(outboxTransactionDocument);
        } catch (Exception exception) {
            log.error("Failed to save outbox event for topic {}: {}", topic, exception.getMessage(), exception);
            throw new RuntimeException(exception);
        }
    }
}
