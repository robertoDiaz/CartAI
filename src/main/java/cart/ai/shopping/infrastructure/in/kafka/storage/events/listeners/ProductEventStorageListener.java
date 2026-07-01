package cart.ai.shopping.infrastructure.in.kafka.storage.events.listeners;

import cart.ai.shopping.domain.model.shop.vos.ProductCreatedEvent;
import cart.ai.shopping.domain.model.shop.vos.ProductUpdatedEvent;
import cart.ai.shopping.domain.model.storage.StoredFile;
import cart.ai.shopping.domain.ports.storage.StoragePort;
import cart.ai.shopping.domain.ports.storage.StoredFileRepositoryPort;
import cart.ai.shopping.domain.ports.storage.TempStoragePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductEventStorageListener {

    private final StoragePort storagePort;
    private final TempStoragePort tempStoragePort;
    private final StoredFileRepositoryPort storedFileRepositoryPort;

    @KafkaListener(topics = "product-created-topic", groupId = "storage-processor")
    public void onProductCreated(ProductCreatedEvent event) {
        if (event.imageFileIds() != null) {
            for (String fileId : event.imageFileIds()) {
                promoteFileById(fileId);
            }
        }
    }

    @KafkaListener(topics = "product-updated-topic", groupId = "storage-processor")
    public void onProductUpdated(ProductUpdatedEvent event) {
        List<String> oldFileIds = event.oldImageFileIds() != null ? event.oldImageFileIds() : List.of();
        List<String> newFileIds = event.newImageFileIds() != null ? event.newImageFileIds() : List.of();

        List<String> toPromote = newFileIds.stream().filter(id -> !oldFileIds.contains(id)).toList();
        List<String> toDelete = oldFileIds.stream().filter(id -> !newFileIds.contains(id)).toList();

        for (String fileId : toPromote) {
            promoteFileById(fileId);
        }

        for (String fileId : toDelete) {
            deleteFileById(fileId);
        }
    }

    private void promoteFileById(String fileId) {
        try {
            StoredFile storedFile = storedFileRepositoryPort.findById(fileId);
            if (storedFile != null) {
                storagePort.promoteFile(storedFile.fileName(), tempStoragePort.getBucketName());
                log.debug("Promoted physical file successfully for product update: {}", storedFile.fileName());
            }
        } catch (Exception e) {
            log.error("Could not promote file {} for product update: {}", fileId, e.getMessage());
        }
    }

    private void deleteFileById(String fileId) {
        try {
            StoredFile storedFile = storedFileRepositoryPort.findById(fileId);
            if (storedFile != null) {
                storagePort.deleteFile(storedFile.fileName());
                log.debug("Deleted physical file successfully after product update: {}", storedFile.fileName());
            }
        } catch (Exception e) {
            log.error("Could not delete file {} after product update: {}", fileId, e.getMessage());
        }
    }
}
