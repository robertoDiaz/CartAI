/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.config;

import cart.ai.shopping.domain.model.storage.StoredFile;
import cart.ai.shopping.domain.ports.storage.StoragePort;
import cart.ai.shopping.domain.ports.storage.TempStoragePort;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Replaces MinIO-backed storage beans with no-op stubs for integration tests.
 * This prevents connection attempts to a real MinIO instance during testing.
 *
 * @author Roberto Díaz
 */
@TestConfiguration
public class TestStorageConfig {

    @Bean
    @Primary
    public StoragePort storagePort() {
        return new StoragePort() {
            @Override
            public StoredFile uploadFile(InputStream inputStream, String fileName, String contentType, long contentLength) {
                return new StoredFile("id-" + fileName, fileName, fileName, "http://test/" + fileName, contentType, null);
            }

            @Override
            public InputStream downloadFile(String fileName) {
                return new java.io.ByteArrayInputStream("fake-file-content".getBytes());
            }

            @Override
            public void deleteFile(String fileName) {
                // No-op: simulates successful file deletion
            }

            @Override
            public void promoteFile(String fileName, String sourceBucketName) {
                // No-op: simulates successful file promotion
            }
        };
    }

    @Bean
    @Primary
    public TempStoragePort tempStoragePort() {
        return new TempStoragePort() {
            @Override
            public StoredFile uploadFile(InputStream inputStream, String fileName, String contentType, long contentLength) {
                return new StoredFile("temp-id-" + fileName, fileName, fileName, "http://temp/" + fileName, contentType, null);
            }

            @Override
            public void deleteFile(String fileName) {
                // No-op
            }

            @Override
            public String getBucketName() {
                return "test-temp-bucket";
            }
        };
    }

    /**
     * Provides a mock KafkaTemplate to satisfy the OutboxTransactionScheduler bean dependency.
     * No messages are actually sent to Kafka during tests.
     */
    @Bean
    @Primary
    @SuppressWarnings("unchecked")
    public KafkaTemplate<String, Object> kafkaTemplate() {
        KafkaTemplate<String, Object> mockTemplate = mock(KafkaTemplate.class);
        when(mockTemplate.send(any(ProducerRecord.class)))
                .thenReturn(CompletableFuture.completedFuture(mock(SendResult.class)));
        when(mockTemplate.send(any(String.class), any(), any()))
                .thenReturn(CompletableFuture.completedFuture(mock(SendResult.class)));
        return mockTemplate;
    }

    /**
     * Provides a mock KafkaTemplate<Object,Object> to satisfy the KafkaErrorHandlerConfig dependency.
     */
    @Bean
    @SuppressWarnings("unchecked")
    public KafkaTemplate<Object, Object> kafkaObjectTemplate() {
        KafkaTemplate<Object, Object> mockTemplate = mock(KafkaTemplate.class);
        when(mockTemplate.send(any(ProducerRecord.class)))
                .thenReturn(CompletableFuture.completedFuture(mock(SendResult.class)));
        when(mockTemplate.send(any(String.class), any(), any()))
                .thenReturn(CompletableFuture.completedFuture(mock(SendResult.class)));
        return mockTemplate;
    }
}
