/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.identity;

import cart.ai.shopping.infrastructure.in.rest.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Atomic integration tests for the avatar upload endpoint ({@code PUT /api/users/avatar/{id}}).
 * <p>
 * Each test is fully self-contained:
 * <ul>
 *   <li>Performs its own login to obtain a fresh JWT token</li>
 *   <li>Does not share or depend on state left by other tests</li>
 *   <li>Can run in any order or in isolation</li>
 * </ul>
 *
 * @author Roberto Díaz
 */
class AvatarIT extends BaseIT {

    private static final byte[] FAKE_IMAGE = "fake-image-bytes".getBytes();

    // =========================================================================
    // Helpers
    // =========================================================================

    private MockMultipartFile imageFile(String filename) {
        return new MockMultipartFile("file", filename, MediaType.IMAGE_JPEG_VALUE, FAKE_IMAGE);
    }

    private MockMultipartFile imageFile(String filename, String contentType) {
        return new MockMultipartFile("file", filename, contentType, FAKE_IMAGE);
    }

    // =========================================================================
    // Upload succeeds for own avatar
    // =========================================================================

    @Test
    void customerCanUploadOwnAvatar() throws Exception {
        var auth = login(CUSTOMER_EMAIL, CUSTOMER_PASS);
        String token = auth.get("token").asText();
        String userId = auth.get("userId").asText();

        mockMvc.perform(multipart(HttpMethod.PUT, "/api/users/avatar/" + userId)
                        .file(imageFile("profile.jpg"))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.avatarFileId").isNotEmpty());
    }

    @Test
    void vendorCanUploadOwnAvatar() throws Exception {
        var auth = login(VENDOR_EMAIL, VENDOR_PASS);
        String token = auth.get("token").asText();
        String userId = auth.get("userId").asText();

        mockMvc.perform(multipart(HttpMethod.PUT, "/api/users/avatar/" + userId)
                        .file(imageFile("vendor-profile.png", MediaType.IMAGE_PNG_VALUE))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.avatarFileId").isNotEmpty());
    }

    // =========================================================================
    // Persistence: avatarFileId appears when fetching the user
    // =========================================================================

    @Test
    void avatarFileIdIsReturnedWhenFetchingUserAfterUpload() throws Exception {
        var auth = login(CUSTOMER_EMAIL, CUSTOMER_PASS);
        String token  = auth.get("token").asText();
        String userId = auth.get("userId").asText();

        // Act — upload avatar
        mockMvc.perform(multipart(HttpMethod.PUT, "/api/users/avatar/" + userId)
                        .file(imageFile("my-avatar.jpg"))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        // Assert — avatarFileId persisted on the user document
        mockMvc.perform(get("/api/users/" + userId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.avatarFileId").isNotEmpty());
    }

    // =========================================================================
    // Admin can upload for any user
    // =========================================================================

    @Test
    void adminCanUploadAvatarForAnyUser() throws Exception {
        var adminAuth = login(ADMIN_EMAIL, ADMIN_PASS);
        String adminToken = adminAuth.get("token").asText();

        var customerAuth = login(CUSTOMER_EMAIL, CUSTOMER_PASS);
        String customerToken = customerAuth.get("token").asText();
        String customerId = customerAuth.get("userId").asText();

        // Act — admin uploads avatar for customer
        mockMvc.perform(multipart(HttpMethod.PUT, "/api/users/avatar/" + customerId)
                        .file(imageFile("admin-override.jpg"))
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.avatarFileId").isNotEmpty());

        // Assert — customer sees the change when fetching their own profile
        mockMvc.perform(get("/api/users/" + customerId)
                        .header("Authorization", "Bearer " + customerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.avatarFileId").isNotEmpty());
    }

    // =========================================================================
    // Access control
    // =========================================================================

    @Test
    void customerCannotUploadAvatarForAnotherUser() throws Exception {
        var adminAuth = login(ADMIN_EMAIL, ADMIN_PASS);
        String adminId = adminAuth.get("userId").asText();

        var customerAuth = login(CUSTOMER_EMAIL, CUSTOMER_PASS);
        String customerToken = customerAuth.get("token").asText();

        mockMvc.perform(multipart(HttpMethod.PUT, "/api/users/avatar/" + adminId)
                        .file(imageFile("hack-attempt.jpg"))
                        .header("Authorization", "Bearer " + customerToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void unauthenticatedRequestIsRejected() throws Exception {
        var auth = login(CUSTOMER_EMAIL, CUSTOMER_PASS);
        String userId = auth.get("userId").asText();

        mockMvc.perform(multipart(HttpMethod.PUT, "/api/users/avatar/" + userId)
                        .file(imageFile("profile.jpg")))
                // No Authorization header
                .andExpect(status().isForbidden());
    }

    // =========================================================================
    // Input validation
    // =========================================================================

    @Test
    void uploadRejectsEmptyFile() throws Exception {
        var auth = login(CUSTOMER_EMAIL, CUSTOMER_PASS);
        String token = auth.get("token").asText();
        String userId = auth.get("userId").asText();

        MockMultipartFile emptyFile = new MockMultipartFile(
                "file", "profile.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[0]
        );

        mockMvc.perform(multipart(HttpMethod.PUT, "/api/users/avatar/" + userId)
                        .file(emptyFile)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    @Test
    void uploadRejectsNonImageContentType() throws Exception {
        var auth = login(CUSTOMER_EMAIL, CUSTOMER_PASS);
        String token = auth.get("token").asText();
        String userId = auth.get("userId").asText();

        mockMvc.perform(multipart(HttpMethod.PUT, "/api/users/avatar/" + userId)
                        .file(imageFile("document.pdf", MediaType.APPLICATION_PDF_VALUE))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    @Test
    void uploadRejectsFileLargerThan2MB() throws Exception {
        var auth = login(CUSTOMER_EMAIL, CUSTOMER_PASS);
        String token = auth.get("token").asText();
        String userId = auth.get("userId").asText();

        byte[] oversizedContent = new byte[2 * 1024 * 1024 + 1]; // 2MB + 1 byte

        mockMvc.perform(multipart(HttpMethod.PUT, "/api/users/avatar/" + userId)
                        .file(new MockMultipartFile("file", "big.jpg", MediaType.IMAGE_JPEG_VALUE, oversizedContent))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isPayloadTooLarge());
    }
}
