/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.storage;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.application.usecases.storage.commands.UploadFileCommand;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.model.identity.User;
import cart.ai.shopping.domain.model.identity.vos.UserId;
import cart.ai.shopping.domain.model.storage.StoredFile;
import cart.ai.shopping.domain.ports.common.IncrementIdGeneratorPort;
import cart.ai.shopping.domain.ports.identity.UserRepositoryPort;
import cart.ai.shopping.domain.ports.storage.StoredFileRepositoryPort;
import cart.ai.shopping.domain.ports.storage.TempStoragePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.transaction.annotation.Transactional;

import static cart.ai.shopping.domain.common.result.ResultError.*;

/**
 * @author Roberto Díaz
 */
@RequiredArgsConstructor
@UseCase
@Slf4j
public class UploadFileUseCase {

    private final TempStoragePort tempStoragePort;
    private final StoredFileRepositoryPort storedFileRepositoryPort;
    private final IncrementIdGeneratorPort idGeneratorPort;
    private final UserRepositoryPort userRepositoryPort;

    private static @NonNull String getExtension(UploadFileCommand command) {
        String originalFileName = command.originalFileName();

        String extension = "";

        int dotIndex = originalFileName.lastIndexOf('.');

        if (dotIndex > 0) {
            extension = originalFileName.substring(dotIndex);
        }
        return extension;
    }

    @Transactional
    public Result<StoredFile> execute(UploadFileCommand command) {
        if (command == null || command.originalFileName().isBlank()) {
            return Result.error(BAD_REQUEST);
        }

        User requester = userRepositoryPort.findByUserId(new UserId(command.requesterUserId()));
        if (requester == null) {
            return Result.error(UNAUTHORIZED);
        }

        boolean isAdmin = requester.roles().stream()
                .anyMatch(role -> role.name().equals("ADMIN"));

        if (command.ownerId() != null) {
            if (!command.ownerId().equals(command.requesterUserId()) && !isAdmin) {
                return Result.error(FORBIDDEN);
            }
        } else {
            boolean hasWritePermission = requester.roles().stream()
                    .flatMap(role -> role.permissions().stream())
                    .anyMatch(permission -> permission.value().equals("WRITE_PRODUCTS"));
            if (!hasWritePermission && !isAdmin) {
                return Result.error(FORBIDDEN);
            }
        }

        String id = idGeneratorPort.generate(StoredFile.class);
        String uniqueFileName = id + getExtension(command);

        StoredFile result;
        try {
            result = tempStoragePort.uploadFile(command.inputStream(), uniqueFileName, command.contentType(), command.contentLength());
        } catch (Exception e) {
            return Result.error(INTERNAL_ERROR);
        }

        StoredFile storedFile = new StoredFile(
                id,
                uniqueFileName,
                command.originalFileName(),
                result.fileUrl(),
                result.contentType(),
                command.ownerId()
        );

        try {
            StoredFile saved = storedFileRepositoryPort.save(storedFile);
            return Result.success(saved);
        } catch (Exception e) {
            try {
                tempStoragePort.deleteFile(uniqueFileName);
            } catch (Exception ex) {
                log.error(
                    "Error deleting file ({}): {}", uniqueFileName, ex.getMessage(), ex);
            }
            return Result.error(INTERNAL_ERROR);
        }
    }
}
