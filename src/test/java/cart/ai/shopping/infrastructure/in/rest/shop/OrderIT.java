/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.shop;

import cart.ai.shopping.infrastructure.in.rest.BaseIT;
import cart.ai.shopping.infrastructure.in.rest.shop.dtos.AddShoppingItemToCartRestRequest;
import cart.ai.shopping.infrastructure.in.rest.shop.dtos.CreateProductRestRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Atomic integration tests for order management endpoints.
 * <p>
 * Evaluates the endpoints exposed by {@code OrderRestController}.
 * Each test prepares its own product and cart independently before acting.
 *
 * @author Roberto Díaz
 */
class OrderIT extends BaseIT {

    @AfterEach
    void cleanup() {
        cleanCollections(COLLECTION_PRODUCTS, COLLECTION_CART, COLLECTION_ORDER);
    }


    private String createProductAsVendor(String name) throws Exception {
        var auth = login(VENDOR_EMAIL, VENDOR_PASS);
        CreateProductRestRequest req = new CreateProductRestRequest(
                name, "Test desc", new BigDecimal("10.00"), 100, List.of()
        );
        String response = mockMvc.perform(post("/api/products")
                        .header("Authorization", bearerOf(auth.get("token").asText()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(response).get("id").asText();
    }

    private void addProductToCart(String customerId, String productId, String token) throws Exception {
        mockMvc.perform(patch("/api/carts/add")
                .header("Authorization", bearerOf(token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new AddShoppingItemToCartRestRequest(customerId, productId, 1))));
    }

    // =========================================================================
    // POST /api/orders/{customerId}
    // =========================================================================

    @Test
    void customerCanCreateOrderFromCart() throws Exception {
        var auth = login(CUSTOMER_EMAIL, CUSTOMER_PASS);
        String token = auth.get("token").asText();
        String customerId = auth.get("userId").asText();

        // 1. Prepare Cart
        String productId = createProductAsVendor("Headphones");
        addProductToCart(customerId, productId, token);

        // 2. Create Order
        mockMvc.perform(post("/api/orders/" + customerId)
                        .header("Authorization", bearerOf(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").isNotEmpty())
                .andExpect(jsonPath("$.status").value("created"))
                .andExpect(jsonPath("$.shoppingItems").isNotEmpty());
    }

    @Test
    void createOrderFailsIfCartIsEmpty() throws Exception {
        var auth = login(CUSTOMER_EMAIL, CUSTOMER_PASS);
        String token = auth.get("token").asText();
        String customerId = auth.get("userId").asText();

        // 1. Ensure cart is empty
        mockMvc.perform(patch("/api/carts/clear/" + customerId)
                .header("Authorization", bearerOf(token)));

        // 2. Attempt Create Order
        mockMvc.perform(post("/api/orders/" + customerId)
                        .header("Authorization", bearerOf(token)))
                .andExpect(status().isBadRequest()); // ResultErrorHttpStatusMapper should map it appropriately
    }

    // =========================================================================
    // GET /api/orders/{id}
    // =========================================================================

    @Test
    void customerCanGetTheirOrder() throws Exception {
        var auth = login(CUSTOMER_EMAIL, CUSTOMER_PASS);
        String token = auth.get("token").asText();
        String customerId = auth.get("userId").asText();

        // 1. Prepare Cart and Order
        String productId = createProductAsVendor("Mouse");
        addProductToCart(customerId, productId, token);

        String response = mockMvc.perform(post("/api/orders/" + customerId)
                        .header("Authorization", bearerOf(token)))
                .andReturn().getResponse().getContentAsString();

        String orderId = objectMapper.readTree(response).get("orderId").asText();

        // 2. Get Order
        mockMvc.perform(get("/api/orders/" + orderId)
                        .header("Authorization", bearerOf(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(orderId));
    }

    // =========================================================================
    // DELETE /api/orders/{id}
    // =========================================================================

    @Test
    void customerCanCancelTheirOrder() throws Exception {
        var auth = login(CUSTOMER_EMAIL, CUSTOMER_PASS);
        String token = auth.get("token").asText();
        String customerId = auth.get("userId").asText();

        // 1. Prepare Cart and Order
        String productId = createProductAsVendor("Keyboard");
        addProductToCart(customerId, productId, token);

        String response = mockMvc.perform(post("/api/orders/" + customerId)
                        .header("Authorization", bearerOf(token)))
                .andReturn().getResponse().getContentAsString();

        String orderId = objectMapper.readTree(response).get("orderId").asText();

        // 2. Cancel Order
        mockMvc.perform(delete("/api/orders/" + orderId)
                        .header("Authorization", bearerOf(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("canceled"));
    }
}
