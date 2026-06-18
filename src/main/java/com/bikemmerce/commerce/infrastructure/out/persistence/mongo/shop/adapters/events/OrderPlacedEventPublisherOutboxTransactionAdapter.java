package com.bikemmerce.commerce.infrastructure.out.persistence.mongo.shop.adapters.events;

import com.bikemmerce.commerce.domain.model.shop.Order;
import com.bikemmerce.commerce.domain.model.shop.value.objects.OrderPlacedEvent;
import com.bikemmerce.commerce.domain.ports.shop.events.OrderPlacedEventPublisherPort;
import com.bikemmerce.commerce.infrastructure.out.persistence.mongo.common.documents.OutboxTransactionDocument;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderPlacedEventPublisherOutboxTransactionAdapter implements OrderPlacedEventPublisherPort {

    private static final String TOPIC = "orders-topic";
    private final MongoTemplate mongoTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void publish(OrderPlacedEvent event) {
        try {
            OutboxTransactionDocument outboxTransactionDocument = OutboxTransactionDocument.builder()
                    .aggregateType(Order.class.getSimpleName().toLowerCase())
                    .aggregateId(event.orderId().value())
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
