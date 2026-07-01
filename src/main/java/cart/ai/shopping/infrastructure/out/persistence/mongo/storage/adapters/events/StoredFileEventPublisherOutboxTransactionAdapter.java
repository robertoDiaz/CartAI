/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.out.persistence.mongo.storage.adapters.events;

import cart.ai.shopping.domain.model.storage.StoredFile;
import cart.ai.shopping.domain.model.storage.vos.StoredFileEvent;
import cart.ai.shopping.domain.ports.storage.StoredFileEventPublisherPort;
import cart.ai.shopping.infrastructure.out.persistence.mongo.common.documents.OutboxTransactionDocument;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Roberto Díaz
 */
@Component
@RequiredArgsConstructor
public class StoredFileEventPublisherOutboxTransactionAdapter implements StoredFileEventPublisherPort {

    private static final String DELETED_TOPIC = "storage-deleted-topic";
    private final MongoTemplate mongoTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void deletionConfirmed(StoredFileEvent event) {
        saveOutbox(event, DELETED_TOPIC);
    }

    private void saveOutbox(StoredFileEvent event, String topic) {
        try {
            OutboxTransactionDocument outboxTransactionDocument = OutboxTransactionDocument.builder()
                    .aggregateType(StoredFile.class.getSimpleName().toLowerCase())
                    .aggregateId(event.fileName())
                    .key(event.fileName())
                    .topic(topic)
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
