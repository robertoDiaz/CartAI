/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.storage;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.application.usecases.storage.commands.DownloadFileCommand;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.model.identity.User;
import cart.ai.shopping.domain.model.identity.vos.UserId;
import cart.ai.shopping.domain.model.storage.FileDownloadStream;
import cart.ai.shopping.domain.model.storage.StoredFile;
import cart.ai.shopping.domain.ports.identity.UserRepositoryPort;
import cart.ai.shopping.domain.ports.storage.StoragePort;
import cart.ai.shopping.domain.ports.storage.StoredFileRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.io.InputStream;

import static cart.ai.shopping.domain.common.result.ResultError.*;

/**
 * @author Roberto Díaz
 */
@RequiredArgsConstructor
@UseCase
public class DownloadFileUseCase {

    private final StoragePort storagePort;
    private final StoredFileRepositoryPort storedFileRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    public Result<FileDownloadStream> execute(DownloadFileCommand command) {
        if (command == null || command.id().isBlank()) {
            return Result.error(BAD_REQUEST);
        }

        String id = command.id();
        StoredFile storedFile = storedFileRepositoryPort.findById(id);
        if (storedFile == null) {
            return Result.error(NOT_FOUND);
        }

        if (storedFile.ownerId() != null) {
            User requester = userRepositoryPort.findByUserId(new UserId(command.requesterUserId()));
            if (requester == null) {
                return Result.error(UNAUTHORIZED);
            }

            boolean isAdmin = requester.roles().stream()
                    .anyMatch(role -> role.name().equals("ADMIN"));

            if (!storedFile.ownerId().equals(command.requesterUserId()) && !isAdmin) {
                return Result.error(FORBIDDEN);
            }
        }

        try {
            InputStream inputStream = storagePort.downloadFile(storedFile.fileName());
            FileDownloadStream downloadStream = new FileDownloadStream(
                    inputStream,
                    storedFile.originalFileName(),
                    storedFile.contentType()
            );
            return Result.success(downloadStream);
        } catch (Exception e) {
            return Result.error(NOT_FOUND);
        }
    }
}
