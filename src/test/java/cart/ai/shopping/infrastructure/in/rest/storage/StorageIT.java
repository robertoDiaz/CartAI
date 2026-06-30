/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.storage;

import cart.ai.shopping.infrastructure.in.rest.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Atomic integration tests for the generic storage endpoints.
 * <p>
 * Evaluates the endpoints exposed by {@code StorageRestController}.
 * Each test is fully independent and cleans up after itself via
 * standard flapdoodle recreation or specific explicit steps.
 *
 * @author Roberto Díaz
 */
class StorageIT extends BaseIT {

    private static final byte[] FAKE_FILE_CONTENT = "fake-file-content".getBytes();

    private MockMultipartFile testFile(String filename) {
        return new MockMultipartFile("file", filename, MediaType.TEXT_PLAIN_VALUE, FAKE_FILE_CONTENT);
    }


    // =========================================================================
    // POST /api/storage/upload
    // =========================================================================

    @Test
    void customerCanUploadFile() throws Exception {
        var auth = login(CUSTOMER_EMAIL, CUSTOMER_PASS);
        String token = auth.get("token").asText();

        mockMvc.perform(multipart(HttpMethod.POST, "/api/storage/upload")
                        .file(testFile("test.txt"))
                        .header("Authorization", bearerOf(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.originalFileName").value("test.txt"))
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    void unauthenticatedUserCannotUploadFile() throws Exception {
        mockMvc.perform(multipart(HttpMethod.POST, "/api/storage/upload")
                        .file(testFile("test.txt")))
                .andExpect(status().isForbidden());
    }

    // =========================================================================
    // GET /api/storage/files/{id}
    // =========================================================================

    @Test
    void ownerCanDownloadFile() throws Exception {
        var auth = login(CUSTOMER_EMAIL, CUSTOMER_PASS);
        String token = auth.get("token").asText();

        // 1. Upload
        String response = mockMvc.perform(multipart(HttpMethod.POST, "/api/storage/upload")
                        .file(testFile("test-download.txt"))
                        .header("Authorization", bearerOf(token)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String fileId = objectMapper.readTree(response).get("id").asText();

        // 2. Download
        mockMvc.perform(get("/api/storage/files/" + fileId)
                        .header("Authorization", bearerOf(token)))
                .andExpect(status().isOk())
                .andExpect(content().string("fake-file-content"))
                .andExpect(header().string("Content-Disposition", "inline; filename=\"test-download.txt\""));
    }

    // =========================================================================
    // DELETE /api/storage/files/{id}
    // =========================================================================

    @Test
    void ownerCanDeleteFile() throws Exception {
        var auth = login(CUSTOMER_EMAIL, CUSTOMER_PASS);
        String token = auth.get("token").asText();

        // 1. Upload
        String response = mockMvc.perform(multipart(HttpMethod.POST, "/api/storage/upload")
                        .file(testFile("test-delete.txt"))
                        .header("Authorization", bearerOf(token)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String fileId = objectMapper.readTree(response).get("id").asText();

        // 2. Delete
        mockMvc.perform(delete("/api/storage/files/" + fileId)
                        .header("Authorization", bearerOf(token)))
                .andExpect(status().isOk());

        // 3. Verify deletion (download fails)
        mockMvc.perform(get("/api/storage/files/" + fileId)
                        .header("Authorization", bearerOf(token)))
                .andExpect(status().isNotFound()); // or isForbidden if logic handles it that way
    }
}
