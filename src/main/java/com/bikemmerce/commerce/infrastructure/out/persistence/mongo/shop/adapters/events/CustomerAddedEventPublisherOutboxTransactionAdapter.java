package com.bikemmerce.commerce.infrastructure.out.persistence.mongo.shop.adapters.events;

import com.bikemmerce.commerce.domain.model.shop.Order;
import com.bikemmerce.commerce.domain.model.shop.value.objects.CustomerAddedEvent;
import com.bikemmerce.commerce.domain.ports.shop.events.CustomerAddedEventPublisherPort;
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
public class CustomerAddedEventPublisherOutboxTransactionAdapter implements CustomerAddedEventPublisherPort {

    private static final String TOPIC = "customers-topic";
    private final MongoTemplate mongoTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void added(CustomerAddedEvent event) {
        try {
            OutboxTransactionDocument outboxTransactionDocument = OutboxTransactionDocument.builder()
                    .aggregateType(Order.class.getSimpleName().toLowerCase())
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
