/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.storage.controllers;

import cart.ai.shopping.application.usecases.storage.DeleteFileUseCase;
import cart.ai.shopping.application.usecases.storage.DownloadFileUseCase;
import cart.ai.shopping.application.usecases.storage.UploadFileUseCase;
import cart.ai.shopping.application.usecases.storage.commands.DeleteFileCommand;
import cart.ai.shopping.application.usecases.storage.commands.DownloadFileCommand;
import cart.ai.shopping.application.usecases.storage.commands.UploadFileCommand;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.model.storage.FileDownloadStream;
import cart.ai.shopping.domain.model.storage.StoredFile;
import cart.ai.shopping.infrastructure.in.rest.common.ResultErrorHttpStatusMapper;
import cart.ai.shopping.infrastructure.in.rest.storage.dtos.StorageRestResponse;
import cart.ai.shopping.infrastructure.security.services.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Objects;

/**
 * @author Roberto Díaz
 */
@RestController
@RequestMapping("/api/storage")
@RequiredArgsConstructor
public class StorageRestController {

    private final UploadFileUseCase uploadFileUseCase;
    private final DownloadFileUseCase downloadFileUseCase;
    private final DeleteFileUseCase deleteFileUseCase;
    private final UserAuthService userAuthService;


    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestHeader("Authorization") String authHeader) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File cannot be empty.");
        }

        String userId = userAuthService.getUserId(authHeader);

        try (InputStream inputStream = file.getInputStream()) {
            UploadFileCommand command = new UploadFileCommand(
                    inputStream,
                    Objects.requireNonNull(file.getOriginalFilename()),
                    Objects.requireNonNull(file.getContentType()),
                    file.getSize(),
                    userId,
                    userId
            );

            Result<StoredFile> result = uploadFileUseCase.execute(command);

            if (result.hasError()) {
                return ResponseEntity.status(ResultErrorHttpStatusMapper.toHttpStatus(result.getError())).body("Could not upload file.");
            }

            StoredFile stored = result.getValue();
            StorageRestResponse response = new StorageRestResponse(
                    stored.id(),
                    stored.originalFileName(),
                    stored.fileUrl(),
                    stored.contentType()
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing file: " + e.getMessage());
        }
    }

    @GetMapping("/files/{id:.+}")
    public ResponseEntity<?> getFile(
            @PathVariable String id,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        String userId = authHeader != null ? userAuthService.getUserId(authHeader) : null;

        DownloadFileCommand command = new DownloadFileCommand(
                id,
                userId
        );

        Result<FileDownloadStream> result = downloadFileUseCase.execute(command);

        if (result.hasError()) {
            return ResponseEntity.status(ResultErrorHttpStatusMapper.toHttpStatus(result.getError())).body("Access denied or file not found.");
        }

        FileDownloadStream downloadStream = result.getValue();
        InputStreamResource resource = new InputStreamResource(downloadStream.inputStream());

        MediaType mediaType = MediaType.parseMediaType(downloadStream.contentType());

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + downloadStream.originalFileName() + "\"")
                .body(resource);
    }

    @DeleteMapping("/files/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteFile(
            @PathVariable String id,
            @RequestHeader("Authorization") String authHeader) {
        String userId = userAuthService.getUserId(authHeader);

        DeleteFileCommand command = new DeleteFileCommand(
                id,
                userId
        );

        Result<Void> result = deleteFileUseCase.execute(command);

        if (result.hasError()) {
            return ResponseEntity.status(ResultErrorHttpStatusMapper.toHttpStatus(result.getError())).body("Access denied or file not found.");
        }

        return ResponseEntity.ok("File deleted successfully.");
    }
}
