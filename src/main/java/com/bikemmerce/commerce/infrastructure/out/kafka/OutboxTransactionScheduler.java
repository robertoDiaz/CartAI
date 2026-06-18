package com.bikemmerce.commerce.infrastructure.out.kafka;

import com.bikemmerce.commerce.infrastructure.out.persistence.mongo.documents.OutboxTransactionDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxTransactionScheduler {

    private final MongoTemplate mongoTemplate;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Scheduled(fixedDelay = 1000) // Se ejecuta cada segundo
    public void processOutbox() {
        Query query = new Query(Criteria.where("status").is("PENDING"));
        List<OutboxTransactionDocument> pendingEvents = mongoTemplate.find(query, OutboxTransactionDocument.class);

        for (OutboxTransactionDocument event : pendingEvents) {
            kafkaTemplate.send(event.getTopic(), event.getKey(), event.getPayload())
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            Query updateQuery = new Query(Criteria.where("id").is(event.getId()));
                            mongoTemplate.updateFirst(
                                    updateQuery, Update.update("status", "PROCESSED"), OutboxTransactionDocument.class);
                        } else {
                            log.error("Fallo al despachar outbox event {}", event.getId(), ex);

                            Query updateQuery = new Query(Criteria.where("id").is(event.getId()));

                            mongoTemplate.updateFirst(
                                    updateQuery, Update.update("status", "FAILED"), OutboxTransactionDocument.class);
                        }
                    });
        }
    }
}
