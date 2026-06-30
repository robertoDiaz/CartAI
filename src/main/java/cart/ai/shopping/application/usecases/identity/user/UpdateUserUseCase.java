/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.identity.user;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.application.usecases.identity.user.commands.UpdateUserCommand;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.common.result.ResultError;
import cart.ai.shopping.domain.model.identity.User;
import cart.ai.shopping.domain.model.identity.vos.Email;
import cart.ai.shopping.domain.model.identity.vos.UserId;
import cart.ai.shopping.domain.model.identity.vos.UserUpdatedEvent;
import cart.ai.shopping.domain.ports.identity.PasswordEncoderPort;
import cart.ai.shopping.domain.ports.identity.UserRepositoryPort;
import cart.ai.shopping.domain.ports.identity.UserUpdatedEventPublisherPort;
import cart.ai.shopping.domain.ports.storage.StoragePort;
import cart.ai.shopping.domain.ports.storage.TempStoragePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Roberto Díaz
 */
@UseCase
@RequiredArgsConstructor
@Slf4j
public class UpdateUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final UserUpdatedEventPublisherPort userUpdatedEventPublisherPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final StoragePort storagePort;
    private final TempStoragePort tempStoragePort;

    public Result<User> execute(UpdateUserCommand command) {
        User existingUser = userRepositoryPort.findByUserId(new UserId(command.id()));

        if (existingUser == null) {
            return Result.error(ResultError.NOT_FOUND);
        }



        String finalAvatarId = existingUser.avatarFileId();
        String newAvatarFileId = command.avatarFileId();

        if (newAvatarFileId != null && !newAvatarFileId.isBlank()
                && !newAvatarFileId.equals(existingUser.avatarFileId())) {
            try {
                storagePort.promoteFile(newAvatarFileId, tempStoragePort.getBucketName());
                finalAvatarId = newAvatarFileId;
                if (existingUser.avatarFileId() != null) {
                    storagePort.deleteFile(existingUser.avatarFileId());
                }
            } catch (Exception e) {
                log.warn("Could not promote temp avatar {}: {}", newAvatarFileId, e.getMessage());
            }
        }

        String finalPasswordHash = existingUser.passwordHash();
        if (command.newPassword() != null && !command.newPassword().isBlank()) {
            if (command.oldPassword() == null || command.oldPassword().isBlank()) {
                return Result.error(ResultError.BAD_REQUEST);
            }
            if (!passwordEncoderPort.matches(command.oldPassword(), existingUser.passwordHash())) {
                return Result.error(ResultError.UNAUTHORIZED);
            }
            finalPasswordHash = passwordEncoderPort.encode(command.newPassword());
        }

        User updatedUser = new User(
                existingUser.userId(),
                command.name(),
                existingUser.email(),
                finalPasswordHash,
                command.roles(),
                finalAvatarId
        );

        User savedUser = userRepositoryPort.save(updatedUser);

        userUpdatedEventPublisherPort.updated(new UserUpdatedEvent(
                savedUser.userId(),
                savedUser.name(),
                savedUser.email(),
                savedUser.roles()
        ));

        return Result.success(savedUser);
    }
}
