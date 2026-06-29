/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.shop;

import cart.ai.shopping.infrastructure.in.rest.BaseFlowIT;
import cart.ai.shopping.infrastructure.in.rest.shop.dtos.CreateProductRestRequest;
import cart.ai.shopping.infrastructure.in.rest.shop.dtos.UpdateProductRestRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for Product management endpoints.
 * <p>
 * Extends {@link BaseFlowIT} to reuse shared infrastructure and JWT session helpers.
 * Uses real JWT tokens (no {@code @WithMockUser}) for a realistic end-to-end flow.
 *
 * @author Roberto Díaz
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductFlowIT extends BaseFlowIT {

    private static String sharedProductId;

    @BeforeAll
    void setUp() throws Exception {
        // Force fresh login for all roles used in this IT.
        // Tokens from previous ITs (e.g. a blacklisted customer token from UserFlowIT) must not leak here.
        refreshAllSessions();
    }

    @AfterAll
    void tearDown() {
        // Wipe all products created during this IT.
        // The last test already deletes the product, but this guards against test failure mid-run.
        cleanCollections(COLLECTION_PRODUCTS);
    }

    @Test
    @Order(1)
    void customerCannotCreateProduct() throws Exception {
        ensureCustomerLoggedIn();
        CreateProductRestRequest request = new CreateProductRestRequest("Laptop", "Desc", new BigDecimal("1500.00"), 10, List.of());
        mockMvc.perform(post("/api/products")
                        .header("Authorization", "Bearer " + customerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(2)
    void vendorCanCreateProduct() throws Exception {
        ensureVendorLoggedIn();
        CreateProductRestRequest request = new CreateProductRestRequest("Laptop", "Gaming Laptop", new BigDecimal("1500.00"), 10, List.of("img1"));

        String response = mockMvc.perform(post("/api/products")
                        .header("Authorization", "Bearer " + vendorToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andReturn().getResponse().getContentAsString();

        sharedProductId = objectMapper.readTree(response).get("id").get("value").asText();
    }

    @Test
    @Order(3)
    void customerCanGetProduct() throws Exception {
        ensureCustomerLoggedIn();
        mockMvc.perform(get("/api/products/" + sharedProductId)
                        .header("Authorization", "Bearer " + customerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.stock").value(10));
    }

    @Test
    @Order(4)
    void customerCannotUpdateProduct() throws Exception {
        ensureCustomerLoggedIn();
        UpdateProductRestRequest request = new UpdateProductRestRequest(sharedProductId, "Laptop Pro", "Desc", new BigDecimal("2000.00"), 5, List.of());
        mockMvc.perform(put("/api/products")
                        .header("Authorization", "Bearer " + customerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(5)
    void adminCanUpdateProduct() throws Exception {
        ensureAdminLoggedIn();
        UpdateProductRestRequest request = new UpdateProductRestRequest(sharedProductId, "Laptop Pro", "Gaming Laptop Pro", new BigDecimal("2000.00"), 5, List.of("img1", "img2"));
        mockMvc.perform(put("/api/products")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop Pro"))
                .andExpect(jsonPath("$.stock").value(5));
    }

    @Test
    @Order(6)
    void customerCanListProducts() throws Exception {
        ensureCustomerLoggedIn();
        mockMvc.perform(get("/api/products")
                        .header("Authorization", "Bearer " + customerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").exists());
    }

    @Test
    @Order(7)
    void customerCannotDeleteProduct() throws Exception {
        ensureCustomerLoggedIn();
        mockMvc.perform(delete("/api/products/" + sharedProductId)
                        .header("Authorization", "Bearer " + customerToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(8)
    void vendorCanDeleteProduct() throws Exception {
        ensureVendorLoggedIn();
        mockMvc.perform(delete("/api/products/" + sharedProductId)
                        .header("Authorization", "Bearer " + vendorToken))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/products/" + sharedProductId)
                        .header("Authorization", "Bearer " + vendorToken))
                .andExpect(status().isNotFound());
    }
}
