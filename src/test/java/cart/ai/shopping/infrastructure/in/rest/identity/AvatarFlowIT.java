/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.identity;

import cart.ai.shopping.infrastructure.in.rest.BaseFlowIT;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the user avatar upload flow.
 * <p>
 * Verifies that:
 * <ul>
 *   <li>A user can upload their own avatar</li>
 *   <li>After upload, the avatarFileId is persisted and returned when fetching the user</li>
 *   <li>A user cannot upload an avatar for another user</li>
 *   <li>An admin can upload an avatar for any user</li>
 * </ul>
 * <p>
 * The storage layer is replaced by a no-op stub ({@code TestStorageConfig}) so no
 * real MinIO connection is needed. The stub returns the original filename as the
 * stored file name, which becomes the {@code avatarFileId} on the user document.
 *
 * @author Roberto Díaz
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AvatarFlowIT extends BaseFlowIT {

    private static final String AVATAR_FIELD_NAME = "file";
    private static final String AVATAR_FILE_NAME  = "profile.jpg";
    private static final byte[] AVATAR_CONTENT    = "fake-image-bytes".getBytes();

    @BeforeAll
    void setUp() throws Exception {
        // Force fresh login to avoid inheriting stale/blacklisted tokens from other ITs.
        refreshAllSessions();
    }

    @AfterAll
    void tearDown() {
        // Avatar metadata is stored on the user document — no separate collection to clean.
        // Bootstrap users retain their updated avatarFileId until next test run (flapdoodle is ephemeral).
    }

    // =========================================================================
    // Own avatar upload
    // =========================================================================

    @Test
    @Order(1)
    void customerCanUploadOwnAvatar() throws Exception {
        ensureCustomerLoggedIn();

        MockMultipartFile file = new MockMultipartFile(
                AVATAR_FIELD_NAME, AVATAR_FILE_NAME, MediaType.IMAGE_JPEG_VALUE, AVATAR_CONTENT
        );

        mockMvc.perform(multipart(HttpMethod.PUT, "/api/users/avatar/" + customerUserId)
                        .file(file)
                        .header("Authorization", "Bearer " + customerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.avatarFileId").value(AVATAR_FILE_NAME));
    }

    @Test
    @Order(2)
    void avatarFileIdIsPersistedOnUserAfterUpload() throws Exception {
        ensureCustomerLoggedIn();

        // Upload avatar first
        MockMultipartFile file = new MockMultipartFile(
                AVATAR_FIELD_NAME, AVATAR_FILE_NAME, MediaType.IMAGE_JPEG_VALUE, AVATAR_CONTENT
        );
        mockMvc.perform(multipart(HttpMethod.PUT, "/api/users/avatar/" + customerUserId)
                        .file(file)
                        .header("Authorization", "Bearer " + customerToken))
                .andExpect(status().isOk());

        // Fetch user and verify avatarFileId is present
        mockMvc.perform(get("/api/users/" + customerUserId)
                        .header("Authorization", "Bearer " + customerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.avatarFileId").value(AVATAR_FILE_NAME));
    }

    @Test
    @Order(3)
    void vendorCanUploadOwnAvatar() throws Exception {
        ensureVendorLoggedIn();

        MockMultipartFile file = new MockMultipartFile(
                AVATAR_FIELD_NAME, "vendor-avatar.png", MediaType.IMAGE_PNG_VALUE, AVATAR_CONTENT
        );

        mockMvc.perform(multipart(HttpMethod.PUT, "/api/users/avatar/" + vendorUserId)
                        .file(file)
                        .header("Authorization", "Bearer " + vendorToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.avatarFileId").value("vendor-avatar.png"));
    }

    // =========================================================================
    // Cross-user access — must be denied
    // =========================================================================

    @Test
    @Order(4)
    void customerCannotUploadAvatarForAnotherUser() throws Exception {
        ensureAdminLoggedIn();
        ensureCustomerLoggedIn();

        MockMultipartFile file = new MockMultipartFile(
                AVATAR_FIELD_NAME, AVATAR_FILE_NAME, MediaType.IMAGE_JPEG_VALUE, AVATAR_CONTENT
        );

        // Customer tries to upload to admin's avatar endpoint
        mockMvc.perform(multipart(HttpMethod.PUT, "/api/users/avatar/" + adminUserId)
                        .file(file)
                        .header("Authorization", "Bearer " + customerToken))
                .andExpect(status().isForbidden());
    }

    // =========================================================================
    // Admin privileges
    // =========================================================================

    @Test
    @Order(5)
    void adminCanUploadAvatarForAnyUser() throws Exception {
        ensureAdminLoggedIn();
        ensureCustomerLoggedIn();

        MockMultipartFile file = new MockMultipartFile(
                AVATAR_FIELD_NAME, "admin-set-avatar.jpg", MediaType.IMAGE_JPEG_VALUE, AVATAR_CONTENT
        );

        mockMvc.perform(multipart(HttpMethod.PUT, "/api/users/avatar/" + customerUserId)
                        .file(file)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.avatarFileId").value("admin-set-avatar.jpg"));

        // Verify it's persisted
        mockMvc.perform(get("/api/users/" + customerUserId)
                        .header("Authorization", "Bearer " + customerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.avatarFileId").value("admin-set-avatar.jpg"));
    }

    // =========================================================================
    // Validation
    // =========================================================================

    @Test
    @Order(6)
    void uploadRejectsNonImageFile() throws Exception {
        ensureCustomerLoggedIn();

        MockMultipartFile file = new MockMultipartFile(
                AVATAR_FIELD_NAME, "document.pdf", MediaType.APPLICATION_PDF_VALUE, AVATAR_CONTENT
        );

        mockMvc.perform(multipart(HttpMethod.PUT, "/api/users/avatar/" + customerUserId)
                        .file(file)
                        .header("Authorization", "Bearer " + customerToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(7)
    void uploadRejectsEmptyFile() throws Exception {
        ensureCustomerLoggedIn();

        MockMultipartFile emptyFile = new MockMultipartFile(
                AVATAR_FIELD_NAME, AVATAR_FILE_NAME, MediaType.IMAGE_JPEG_VALUE, new byte[0]
        );

        mockMvc.perform(multipart(HttpMethod.PUT, "/api/users/avatar/" + customerUserId)
                        .file(emptyFile)
                        .header("Authorization", "Bearer " + customerToken))
                .andExpect(status().isBadRequest());
    }
}
