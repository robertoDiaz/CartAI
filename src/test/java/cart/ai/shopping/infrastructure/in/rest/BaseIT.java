/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest;

import cart.ai.shopping.infrastructure.config.TestStorageConfig;
import cart.ai.shopping.infrastructure.in.rest.identity.dtos.LoginRestRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Minimal base class for integration tests.
 * <p>
 * Provides only infrastructure (Spring context, MockMvc, MongoTemplate) and
 * stateless helpers. It deliberately holds <strong>no shared session state</strong>:
 * every test class and every test method is responsible for its own login calls.
 * <p>
 * This design enforces the atomic test pattern — each test is self-contained,
 * independently runnable, and free of ordering dependencies.
 *
 * @author Roberto Díaz
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestStorageConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseIT {

    // ── Seed credentials (populated by bootstrap on context startup) ──────────
    protected static final String ADMIN_EMAIL = "admin@test.com";
    protected static final String ADMIN_PASS = "adminpass";
    protected static final String VENDOR_EMAIL = "vendor@test.com";
    protected static final String VENDOR_PASS = "vendorpass";
    protected static final String CUSTOMER_EMAIL = "customer@test.com";
    protected static final String CUSTOMER_PASS = "customerpass";

    // ── Known collection names (must match @Document annotations) ─────────────
    protected static final String COLLECTION_USERS = "users";
    protected static final String COLLECTION_PRODUCTS = "products";
    protected static final String COLLECTION_CART = "cart";
    protected static final String COLLECTION_ORDER = "order";

    // ── Emails that must never be removed ─────────────────────────────────────
    private static final List<String> PROTECTED_EMAILS = List.of(
            ADMIN_EMAIL, VENDOR_EMAIL, CUSTOMER_EMAIL
    );

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected MongoTemplate mongoTemplate;

    // =========================================================================
    // Auth helper
    // =========================================================================

    /**
     * Performs a real HTTP login and returns the full response body as a JsonNode.
     * Call this at the start of each test that needs an authenticated session.
     */
    protected JsonNode login(String email, String password) throws Exception {
        LoginRestRequest req = new LoginRestRequest(email, password);
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString());
    }

    /**
     * Helper to format a token as a Bearer token header value.
     */
    protected String bearerOf(String token) {
        return "Bearer " + token;
    }

    // =========================================================================
    // Cleanup helpers
    // =========================================================================

    /**
     * Removes users by email, skipping the protected bootstrap accounts.
     */
    protected void removeTestUsers(String... emails) {
        List<String> toRemove = List.of(emails).stream()
                .filter(e -> !PROTECTED_EMAILS.contains(e))
                .toList();
        if (!toRemove.isEmpty()) {
            mongoTemplate.remove(
                    Query.query(Criteria.where("email").in(toRemove)),
                    COLLECTION_USERS
            );
        }
    }

    /**
     * Removes all documents from the given collections.
     * Safe for collections without bootstrap data (products, orders, etc.).
     */
    protected void cleanCollections(String... collectionNames) {
        for (String name : collectionNames) {
            mongoTemplate.remove(new Query(), name);
        }
    }
}
