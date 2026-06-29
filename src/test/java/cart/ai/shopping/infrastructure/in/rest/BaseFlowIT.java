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
 * Base class for all integration tests.
 * <p>
 * Provides:
 * <ul>
 *   <li>Spring context with embedded MongoDB, mocked Kafka and mocked MinIO</li>
 *   <li>Lazily initialized JWT tokens for ADMIN, VENDOR and CUSTOMER roles</li>
 *   <li>Helper methods to ensure each role is authenticated before a test runs</li>
 *   <li>MongoTemplate-based cleanup utilities for post-test data teardown</li>
 * </ul>
 * <p>
 * {@code @TestInstance(PER_CLASS)} allows subclasses to declare {@code @AfterAll}
 * as a non-static method while still having access to Spring-injected beans like
 * {@link MongoTemplate}. This is necessary because {@code @Transactional} rollback
 * does not work with MockMvc (HTTP requests run in a separate thread/transaction).
 * <p>
 * Bootstrap users (ADMIN, VENDOR, CUSTOMER) are protected from cleanup — only
 * test-specific documents should be removed in each subclass {@code @AfterAll}.
 *
 * @author Roberto Díaz
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestStorageConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseFlowIT {

    // Known MongoDB collection names (must match @Document annotations)
    protected static final String COLLECTION_USERS    = "users";
    protected static final String COLLECTION_PRODUCTS = "products";
    protected static final String COLLECTION_ROLES    = "roles";
    protected static final String COLLECTION_CART     = "cart";
    protected static final String COLLECTION_ORDER    = "order";

    // Credentials seeded by bootstrap (see test/resources/application.properties)
    protected static final String ADMIN_EMAIL    = "admin@test.com";
    protected static final String ADMIN_PASS     = "adminpass";
    protected static final String VENDOR_EMAIL   = "vendor@test.com";
    protected static final String VENDOR_PASS    = "vendorpass";
    protected static final String CUSTOMER_EMAIL = "customer@test.com";
    protected static final String CUSTOMER_PASS  = "customerpass";

    // Emails that must never be removed — they are the bootstrap seed users
    private static final List<String> PROTECTED_EMAILS = List.of(
            ADMIN_EMAIL, VENDOR_EMAIL, CUSTOMER_EMAIL
    );

    // Shared JWT state — populated lazily on first use per role
    protected static String adminToken;
    protected static String adminUserId;

    protected static String vendorToken;
    protected static String vendorUserId;

    protected static String customerToken;
    protected static String customerUserId;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MongoTemplate mongoTemplate;

    // =========================================================================
    // Auth helpers
    // =========================================================================

    /** Ensures admin is authenticated. No-op if already logged in. */
    protected void ensureAdminLoggedIn() throws Exception {
        if (adminToken != null) return;
        JsonNode response = login(ADMIN_EMAIL, ADMIN_PASS);
        adminToken = response.get("token").asText();
        adminUserId = response.get("userId").asText();
    }

    /** Ensures vendor is authenticated. No-op if already logged in. */
    protected void ensureVendorLoggedIn() throws Exception {
        if (vendorToken != null) return;
        JsonNode response = login(VENDOR_EMAIL, VENDOR_PASS);
        vendorToken = response.get("token").asText();
        vendorUserId = response.get("userId").asText();
    }

    /** Ensures customer is authenticated. No-op if already logged in. */
    protected void ensureCustomerLoggedIn() throws Exception {
        if (customerToken != null) return;
        JsonNode response = login(CUSTOMER_EMAIL, CUSTOMER_PASS);
        customerToken = response.get("token").asText();
        customerUserId = response.get("userId").asText();
    }

    /**
     * Resets the customer session state. Must be called after a logout test
     * to prevent subsequent ITs from using a blacklisted token.
     */
    protected void invalidateCustomerSession() {
        customerToken = null;
        customerUserId = null;
    }

    /**
     * Forces a fresh login for all three roles, discarding any previously cached tokens.
     * <p>
     * Call this from {@code @BeforeAll} in any IT class that may run after
     * {@link cart.ai.shopping.infrastructure.in.rest.identity.UserFlowIT}, whose logout test
     * blacklists the customer token, making it invalid for subsequent classes.
     */
    protected void refreshAllSessions() throws Exception {
        adminToken = null;
        vendorToken = null;
        customerToken = null;
        ensureAdminLoggedIn();
        ensureVendorLoggedIn();
        ensureCustomerLoggedIn();
    }

    /** Performs a real login and returns the parsed JSON response. */
    protected JsonNode login(String email, String password) throws Exception {
        LoginRestRequest req = new LoginRestRequest(email, password);
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString());
    }

    // =========================================================================
    // Cleanup helpers — call from @AfterAll in subclasses
    // =========================================================================

    /**
     * Removes users from the {@code users} collection by email, skipping bootstrap users.
     * Safe to call with any email list — protected emails are always excluded.
     *
     * @param emails the emails of test users to delete
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
     * Intended for collections that do not contain bootstrap data (e.g., products, orders).
     *
     * @param collectionNames names of the collections to wipe
     */
    protected void cleanCollections(String... collectionNames) {
        for (String name : collectionNames) {
            mongoTemplate.remove(new Query(), name);
        }
    }

    /**
     * Removes documents from a collection matching the given field value.
     *
     * @param collection the collection name
     * @param field      the field to filter on
     * @param values     the values to match
     */
    protected void removeByField(String collection, String field, Object... values) {
        mongoTemplate.remove(
                Query.query(Criteria.where(field).in(values)),
                collection
        );
    }
}
