package com.bikemmerce.commerce.infrastructure.out.kafka;

import com.bikemmerce.commerce.infrastructure.out.persistence.mongo.common.documents.OutboxTransactionDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxTransactionScheduler {

    private final MongoTemplate mongoTemplate;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Scheduled(fixedDelay = 1000) // Se ejecuta cada segundo
    public void processOutbox() {
        Query query = new Query(Criteria.where("status").is(OutboxTransactionDocument.PENDING));

        Update update = Update.update("status", OutboxTransactionDocument.PROCESSING);

        OutboxTransactionDocument event;

        while ((event = mongoTemplate.findAndModify(query, update, OutboxTransactionDocument.class)) != null) {
            final OutboxTransactionDocument curEvent = event;

            kafkaTemplate.send(event.getTopic(), event.getKey(), event.getPayload())
                    .whenComplete((result, throwable) -> {
                        if (throwable == null) {
                            Query updateQuery = new Query(Criteria.where("id").is(curEvent.getId()));
                            mongoTemplate.updateFirst(
                                    updateQuery,
                                    Update.update(
                                            "status", OutboxTransactionDocument.SUCCESS), OutboxTransactionDocument.class);
                        } else {
                            log.error("Error handling outbox event {}", curEvent.getId(), throwable);

                            Query updateQuery = new Query(Criteria.where("id").is(curEvent.getId()));

                            mongoTemplate.updateFirst(
                                    updateQuery,
                                    Update.update(
                                            "status", OutboxTransactionDocument.FAIL), OutboxTransactionDocument.class);
                        }
                    });
        }
    }
}
