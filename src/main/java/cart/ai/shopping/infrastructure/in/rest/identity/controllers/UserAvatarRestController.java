/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.identity.controllers;

import cart.ai.shopping.application.usecases.identity.user.UpdateUserAvatarUseCase;
import cart.ai.shopping.application.usecases.identity.user.UploadAvatarUseCase;
import cart.ai.shopping.application.usecases.identity.user.commands.UpdateUserAvatarCommand;
import cart.ai.shopping.application.usecases.identity.user.commands.UploadAvatarCommand;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.model.identity.User;
import cart.ai.shopping.domain.model.storage.StoredFile;
import cart.ai.shopping.infrastructure.in.rest.common.ResultErrorHttpStatusMapper;
import cart.ai.shopping.infrastructure.in.rest.identity.mappers.UserRestMapper;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * @author Roberto Díaz
 */
@RestController
@RequestMapping("/api/users/avatar")
@RequiredArgsConstructor
public class UserAvatarRestController {

    private final UploadAvatarUseCase uploadAvatarUseCase;
    private final UpdateUserAvatarUseCase updateUserAvatarUseCase;

    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024; // 2MB

    private static @Nullable ResponseEntity<String> validate(MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File size exceeds 2MB limit");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.badRequest().body("Only image files are allowed");
        }

        return null;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadAvatar(@RequestParam("file") MultipartFile file) {
        ResponseEntity<String> validation = validate(file);

        if (validation != null) {
            return validation;
        }

        try {
            UploadAvatarCommand command = new UploadAvatarCommand(
                    file.getInputStream(),
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getSize()
            );

            Result<StoredFile> result = uploadAvatarUseCase.execute(command);

            if (result.hasError()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not upload avatar");
            }

            return ResponseEntity.ok(Map.of("avatarFileId", result.getValue().fileName()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing file");
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('WRITE_USERS') or principal.username == #id")
    public ResponseEntity<?> updateAvatar(@PathVariable String id, @RequestParam("file") MultipartFile file) {
        ResponseEntity<String> validation = validate(file);

        if (validation != null) {
            return validation;
        }

        try {
            UpdateUserAvatarCommand command = new UpdateUserAvatarCommand(
                    id,
                    file.getInputStream(),
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getSize()
            );

            Result<User> result = updateUserAvatarUseCase.execute(command);

            if (result.hasError()) {
                return ResponseEntity.status(ResultErrorHttpStatusMapper.toHttpStatus(result.getError())).body("Could not update user avatar.");
            }

            return ResponseEntity.ok(UserRestMapper.toResponse(result.getValue()));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error processing file");
        }
    }
}
