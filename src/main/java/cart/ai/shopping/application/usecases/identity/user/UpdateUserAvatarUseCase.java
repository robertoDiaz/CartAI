/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.identity.user;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.application.usecases.identity.user.commands.UpdateUserAvatarCommand;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.model.identity.User;
import cart.ai.shopping.domain.model.identity.vos.UserId;
import cart.ai.shopping.domain.model.storage.StoredFile;
import cart.ai.shopping.domain.ports.common.IncrementIdGeneratorPort;
import cart.ai.shopping.domain.ports.identity.UserRepositoryPort;
import cart.ai.shopping.domain.ports.storage.StoragePort;
import cart.ai.shopping.domain.ports.storage.StoredFileRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static cart.ai.shopping.domain.common.result.ResultError.INTERNAL_ERROR;
import static cart.ai.shopping.domain.common.result.ResultError.NOT_FOUND;

/**
 * @author Roberto Díaz
 */
@RequiredArgsConstructor
@UseCase
@Slf4j
public class UpdateUserAvatarUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final StoragePort storagePort;
    private final StoredFileRepositoryPort storedFileRepositoryPort;
    private final IncrementIdGeneratorPort idGeneratorPort;

    private String getExtension(String originalFileName) {
        if (originalFileName == null) return "";
        int dotIndex = originalFileName.lastIndexOf('.');
        return dotIndex > 0 ? originalFileName.substring(dotIndex) : "";
    }

    public Result<User> execute(UpdateUserAvatarCommand command) {
        User user = userRepositoryPort.findByUserId(new UserId(command.userId()));
        if (user == null) {
            return Result.error(NOT_FOUND);
        }

        String oldAvatarId = user.avatarFileId();

        try {
            String id = idGeneratorPort.generate(StoredFile.class);
            String uniqueFileName = id + getExtension(command.fileName());

            StoredFile newAvatar = storagePort.uploadFile(
                    command.inputStream(),
                    uniqueFileName,
                    command.contentType(),
                    command.contentLength()
            );

            StoredFile storedFile = new StoredFile(
                    id,
                    uniqueFileName,
                    command.fileName(),
                    newAvatar.fileUrl(),
                    command.contentType(),
                    null // Public file
            );
            storedFileRepositoryPort.save(storedFile);

            if (oldAvatarId != null && !oldAvatarId.isBlank()) {
                try {
                    storagePort.deleteFile(oldAvatarId);
                } catch (Exception e) {
                    log.warn("Could not delete old avatar ({}): {}", oldAvatarId, e.getMessage());
                }
            }

            User updatedUser = new User(
                    user.userId(),
                    user.name(),
                    user.email(),
                    user.passwordHash(),
                    user.roles(),
                    id
            );

            return Result.success(userRepositoryPort.save(updatedUser));
        } catch (Exception e) {
            log.error("Error updating avatar for user {}: {}", command.userId(), e.getMessage(), e);
            return Result.error(INTERNAL_ERROR);
        }
    }
}
