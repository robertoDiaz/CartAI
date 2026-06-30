/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.shop;

import cart.ai.shopping.infrastructure.in.rest.BaseIT;
import cart.ai.shopping.infrastructure.in.rest.shop.dtos.CreateProductRestRequest;
import cart.ai.shopping.infrastructure.in.rest.shop.dtos.UpdateProductRestRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Atomic integration tests for product management endpoints.
 * <p>
 * Each test creates the product it needs and asserts only its own action.
 * No shared state, no ordering, fully independent.
 *
 * @author Roberto Díaz
 */
class ProductIT extends BaseIT {

    @AfterEach
    void cleanup() {
        cleanCollections(COLLECTION_PRODUCTS);
    }

    // ── helpers ───────────────────────────────────────────────────────────────


    /**
     * Creates a product as vendor and returns its ID.
     */
    private String createProductAsVendor(String name) throws Exception {
        var auth = login(VENDOR_EMAIL, VENDOR_PASS);
        CreateProductRestRequest req = new CreateProductRestRequest(
                name, "Test description", new BigDecimal("99.99"), 10, List.of()
        );
        String response = mockMvc.perform(post("/api/products")
                        .header("Authorization", bearerOf(auth.get("token").asText()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(response).get("id").get("value").asText();
    }

    // =========================================================================
    // POST /api/products — create
    // =========================================================================

    @Test
    void vendorCanCreateProduct() throws Exception {
        var auth = login(VENDOR_EMAIL, VENDOR_PASS);
        CreateProductRestRequest req = new CreateProductRestRequest(
                "Gaming Laptop", "High-end laptop", new BigDecimal("1500.00"), 5, List.of("img1.jpg")
        );

        mockMvc.perform(post("/api/products")
                        .header("Authorization", bearerOf(auth.get("token").asText()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gaming Laptop"))
                .andExpect(jsonPath("$.stock").value(5));
    }

    @Test
    void customerCannotCreateProduct() throws Exception {
        var auth = login(CUSTOMER_EMAIL, CUSTOMER_PASS);
        CreateProductRestRequest req = new CreateProductRestRequest(
                "Illegal Product", "Should fail", new BigDecimal("1.00"), 1, List.of()
        );

        mockMvc.perform(post("/api/products")
                        .header("Authorization", bearerOf(auth.get("token").asText()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isForbidden());
    }

    // =========================================================================
    // GET /api/products/{id} — get by id
    // =========================================================================

    @Test
    void anyAuthenticatedUserCanGetProduct() throws Exception {
        String productId = createProductAsVendor("Headphones");

        var auth = login(CUSTOMER_EMAIL, CUSTOMER_PASS);
        mockMvc.perform(get("/api/products/" + productId)
                        .header("Authorization", bearerOf(auth.get("token").asText())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Headphones"));
    }

    @Test
    void getNonExistentProductReturns404() throws Exception {
        var auth = login(CUSTOMER_EMAIL, CUSTOMER_PASS);
        mockMvc.perform(get("/api/products/non-existent-id")
                        .header("Authorization", bearerOf(auth.get("token").asText())))
                .andExpect(status().isNotFound());
    }

    // =========================================================================
    // GET /api/products — list all
    // =========================================================================

    @Test
    void anyAuthenticatedUserCanListProducts() throws Exception {
        createProductAsVendor("Keyboard");

        var auth = login(CUSTOMER_EMAIL, CUSTOMER_PASS);
        mockMvc.perform(get("/api/products")
                        .header("Authorization", bearerOf(auth.get("token").asText())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").exists());
    }

    // =========================================================================
    // PUT /api/products — update
    // =========================================================================

    @Test
    void adminCanUpdateProduct() throws Exception {
        String productId = createProductAsVendor("Old Name");

        var auth = login(ADMIN_EMAIL, ADMIN_PASS);
        UpdateProductRestRequest req = new UpdateProductRestRequest(
                productId, "New Name", "Updated desc", new BigDecimal("200.00"), 3, List.of()
        );

        mockMvc.perform(put("/api/products")
                        .header("Authorization", bearerOf(auth.get("token").asText()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"))
                .andExpect(jsonPath("$.stock").value(3));
    }

    @Test
    void vendorCanUpdateProduct() throws Exception {
        String productId = createProductAsVendor("Draft Product");

        var auth = login(VENDOR_EMAIL, VENDOR_PASS);
        UpdateProductRestRequest req = new UpdateProductRestRequest(
                productId, "Published Product", "Ready for sale", new BigDecimal("299.99"), 20, List.of()
        );

        mockMvc.perform(put("/api/products")
                        .header("Authorization", bearerOf(auth.get("token").asText()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Published Product"));
    }

    @Test
    void customerCannotUpdateProduct() throws Exception {
        String productId = createProductAsVendor("Protected Product");

        var auth = login(CUSTOMER_EMAIL, CUSTOMER_PASS);
        UpdateProductRestRequest req = new UpdateProductRestRequest(
                productId, "Hacked", "Hacked", new BigDecimal("0.01"), 999, List.of()
        );

        mockMvc.perform(put("/api/products")
                        .header("Authorization", bearerOf(auth.get("token").asText()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isForbidden());
    }

    @Test
    void vendorCanDeleteProduct() throws Exception {
        String productId = createProductAsVendor("To Be Deleted");

        var auth = login(VENDOR_EMAIL, VENDOR_PASS);
        mockMvc.perform(delete("/api/products/" + productId)
                        .header("Authorization", bearerOf(auth.get("token").asText())))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/products/" + productId)
                        .header("Authorization", bearerOf(auth.get("token").asText())))
                .andExpect(status().isNotFound());
    }

    @Test
    void customerCannotDeleteProduct() throws Exception {
        String productId = createProductAsVendor("Safe Product");

        var auth = login(CUSTOMER_EMAIL, CUSTOMER_PASS);
        mockMvc.perform(delete("/api/products/" + productId)
                        .header("Authorization", bearerOf(auth.get("token").asText())))
                .andExpect(status().isForbidden());
    }
}
