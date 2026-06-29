/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.identity;

import cart.ai.shopping.infrastructure.in.rest.BaseIT;
import cart.ai.shopping.infrastructure.in.rest.identity.dtos.RegisterRestRequest;
import cart.ai.shopping.infrastructure.in.rest.identity.dtos.UpdateUserRestRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Atomic integration tests for user management endpoints.
 * <p>
 * Each test performs its own login and creates/deletes only what it needs.
 * No shared state, no ordering, fully independent.
 *
 * @author Roberto Díaz
 */
class UserIT extends BaseIT {

    /** Tracks emails created during a test so @AfterEach can clean them up. */
    private final List<String> createdUserEmails = new ArrayList<>();

    @AfterEach
    void cleanup() {
        if (!createdUserEmails.isEmpty()) {
            removeTestUsers(createdUserEmails.toArray(new String[0]));
            createdUserEmails.clear();
        }
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private String bearerOf(String token) {
        return "Bearer " + token;
    }

    /** Registers a new user and tracks their email for post-test cleanup. */
    private String registerAndTrack(String name, String email, String password) throws Exception {
        RegisterRestRequest req = new RegisterRestRequest(name, email, password, null);
        MvcResult result = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andReturn();
        createdUserEmails.add(email);
        return objectMapper.readTree(result.getResponse().getContentAsString())
                .get("userId").asText();
    }

    // =========================================================================
    // Registration
    // =========================================================================

    @Test
    void newUserCanRegister() throws Exception {
        RegisterRestRequest req = new RegisterRestRequest(
                "New User", "newuser@test.com", "password123", null
        );
        createdUserEmails.add("newuser@test.com");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.userId").isNotEmpty())
                .andExpect(jsonPath("$.roles", hasItem("CUSTOMER")));
    }

    // =========================================================================
    // Login
    // =========================================================================

    @Test
    void adminCanLogin() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new cart.ai.shopping.infrastructure.in.rest.identity.dtos.LoginRestRequest(ADMIN_EMAIL, ADMIN_PASS))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.roles", hasItem("ADMIN")));
    }

    @Test
    void vendorCanLogin() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new cart.ai.shopping.infrastructure.in.rest.identity.dtos.LoginRestRequest(VENDOR_EMAIL, VENDOR_PASS))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roles", hasItem("VENDOR")));
    }

    @Test
    void customerCanLogin() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new cart.ai.shopping.infrastructure.in.rest.identity.dtos.LoginRestRequest(CUSTOMER_EMAIL, CUSTOMER_PASS))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roles", hasItem("CUSTOMER")));
    }

    @Test
    void loginFailsWithWrongPassword() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new cart.ai.shopping.infrastructure.in.rest.identity.dtos.LoginRestRequest(ADMIN_EMAIL, "wrong"))))
                .andExpect(status().isUnauthorized());
    }

    // =========================================================================
    // Logout
    // =========================================================================

    @Test
    void tokenIsBlacklistedAfterLogout() throws Exception {
        var auth  = login(CUSTOMER_EMAIL, CUSTOMER_PASS);
        String token  = auth.get("token").asText();
        String userId = auth.get("userId").asText();

        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", bearerOf(token)))
                .andExpect(status().isOk());

        // Token must now be rejected
        mockMvc.perform(get("/api/users/" + userId)
                        .header("Authorization", bearerOf(token)))
                .andExpect(status().isForbidden());
    }

    // =========================================================================
    // GET /api/users — list all users
    // =========================================================================

    @Test
    void adminCanListAllUsers() throws Exception {
        var auth = login(ADMIN_EMAIL, ADMIN_PASS);
        mockMvc.perform(get("/api/users")
                        .header("Authorization", bearerOf(auth.get("token").asText())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())));
    }

    @Test
    void vendorCannotListAllUsers() throws Exception {
        var auth = login(VENDOR_EMAIL, VENDOR_PASS);
        mockMvc.perform(get("/api/users")
                        .header("Authorization", bearerOf(auth.get("token").asText())))
                .andExpect(status().isForbidden());
    }

    @Test
    void customerCannotListAllUsers() throws Exception {
        var auth = login(CUSTOMER_EMAIL, CUSTOMER_PASS);
        mockMvc.perform(get("/api/users")
                        .header("Authorization", bearerOf(auth.get("token").asText())))
                .andExpect(status().isForbidden());
    }

    // =========================================================================
    // GET /api/users/{id} — get user profile
    // =========================================================================

    @Test
    void userCanGetOwnProfile() throws Exception {
        var auth  = login(CUSTOMER_EMAIL, CUSTOMER_PASS);
        String token  = auth.get("token").asText();
        String userId = auth.get("userId").asText();

        mockMvc.perform(get("/api/users/" + userId)
                        .header("Authorization", bearerOf(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(CUSTOMER_EMAIL));
    }

    @Test
    void adminCanGetAnyUsersProfile() throws Exception {
        var adminAuth    = login(ADMIN_EMAIL, ADMIN_PASS);
        String adminToken = adminAuth.get("token").asText();

        var vendorAuth  = login(VENDOR_EMAIL, VENDOR_PASS);
        String vendorId = vendorAuth.get("userId").asText();

        mockMvc.perform(get("/api/users/" + vendorId)
                        .header("Authorization", bearerOf(adminToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(VENDOR_EMAIL));
    }

    @Test
    void customerCannotGetAnotherUsersProfile() throws Exception {
        var adminAuth  = login(ADMIN_EMAIL, ADMIN_PASS);
        String adminId = adminAuth.get("userId").asText();

        var customerAuth    = login(CUSTOMER_EMAIL, CUSTOMER_PASS);
        String customerToken = customerAuth.get("token").asText();

        mockMvc.perform(get("/api/users/" + adminId)
                        .header("Authorization", bearerOf(customerToken)))
                .andExpect(status().isForbidden());
    }

    // =========================================================================
    // PUT /api/users/{id} — update user
    // =========================================================================

    @Test
    void userCanUpdateOwnProfile() throws Exception {
        var auth  = login(CUSTOMER_EMAIL, CUSTOMER_PASS);
        String token  = auth.get("token").asText();
        String userId = auth.get("userId").asText();

        UpdateUserRestRequest req = new UpdateUserRestRequest(
                userId, "Updated Name", CUSTOMER_EMAIL, Set.of("CUSTOMER"), null
        );

        mockMvc.perform(put("/api/users/" + userId)
                        .header("Authorization", bearerOf(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    void adminCanUpdateAnyUsersProfile() throws Exception {
        var adminAuth    = login(ADMIN_EMAIL, ADMIN_PASS);
        String adminToken = adminAuth.get("token").asText();

        var vendorAuth  = login(VENDOR_EMAIL, VENDOR_PASS);
        String vendorId = vendorAuth.get("userId").asText();

        UpdateUserRestRequest req = new UpdateUserRestRequest(
                vendorId, "Admin Renamed Vendor", VENDOR_EMAIL, Set.of("VENDOR"), null
        );

        mockMvc.perform(put("/api/users/" + vendorId)
                        .header("Authorization", bearerOf(adminToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Admin Renamed Vendor"));
    }

    @Test
    void customerCannotUpdateAnotherUsersProfile() throws Exception {
        var adminAuth  = login(ADMIN_EMAIL, ADMIN_PASS);
        String adminId = adminAuth.get("userId").asText();

        var customerAuth    = login(CUSTOMER_EMAIL, CUSTOMER_PASS);
        String customerToken = customerAuth.get("token").asText();

        UpdateUserRestRequest req = new UpdateUserRestRequest(
                adminId, "Hacked", ADMIN_EMAIL, Set.of("ADMIN"), null
        );

        mockMvc.perform(put("/api/users/" + adminId)
                        .header("Authorization", bearerOf(customerToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isForbidden());
    }

    // =========================================================================
    // DELETE /api/users/{id}
    // =========================================================================

    @Test
    void adminCanDeleteUser() throws Exception {
        String disposableId = registerAndTrack("Temp User", "temp-delete@test.com", "pass123");

        var adminAuth    = login(ADMIN_EMAIL, ADMIN_PASS);
        String adminToken = adminAuth.get("token").asText();

        mockMvc.perform(delete("/api/users/" + disposableId)
                        .header("Authorization", bearerOf(adminToken)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/users/" + disposableId)
                        .header("Authorization", bearerOf(adminToken)))
                .andExpect(status().isNotFound());

        // Already deleted — remove from cleanup list to avoid spurious warnings
        createdUserEmails.remove("temp-delete@test.com");
    }

    @Test
    void customerCannotDeleteAnotherUser() throws Exception {
        var adminAuth  = login(ADMIN_EMAIL, ADMIN_PASS);
        String adminId = adminAuth.get("userId").asText();

        var customerAuth    = login(CUSTOMER_EMAIL, CUSTOMER_PASS);
        String customerToken = customerAuth.get("token").asText();

        mockMvc.perform(delete("/api/users/" + adminId)
                        .header("Authorization", bearerOf(customerToken)))
                .andExpect(status().isForbidden());
    }
}
