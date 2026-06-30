/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.shop;

import cart.ai.shopping.infrastructure.in.rest.BaseIT;
import cart.ai.shopping.infrastructure.in.rest.shop.dtos.AddShoppingItemToCartRestRequest;
import cart.ai.shopping.infrastructure.in.rest.shop.dtos.CreateProductRestRequest;
import cart.ai.shopping.infrastructure.in.rest.shop.dtos.RemoveShoppingItemToCartRestRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Atomic integration tests for cart management endpoints.
 * <p>
 * Each test handles its own product creation, cart manipulation, and asserts its own state.
 *
 * @author Roberto Díaz
 */
class CartIT extends BaseIT {

    @AfterEach
    void cleanup() {
        cleanCollections(COLLECTION_PRODUCTS, COLLECTION_CART);
    }


    private String createProductAsVendor(String name) throws Exception {
        var auth = login(VENDOR_EMAIL, VENDOR_PASS);
        CreateProductRestRequest req = new CreateProductRestRequest(
                name, "Test desc", new BigDecimal("10.00"), 100, List.of()
        );
        String response = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/products")
                        .header("Authorization", bearerOf(auth.get("token").asText()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(response).get("id").get("value").asText();
    }

    @Test
    void customerCanAddItemToCart() throws Exception {
        String productId = createProductAsVendor("Coffee Beans");

        var auth = login(CUSTOMER_EMAIL, CUSTOMER_PASS);
        String token = auth.get("token").asText();
        String customerId = auth.get("userId").asText();

        AddShoppingItemToCartRestRequest req = new AddShoppingItemToCartRestRequest(customerId, productId, 1);

        mockMvc.perform(patch("/api/carts/add")
                        .header("Authorization", bearerOf(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shoppingItems", hasSize(1)))
                .andExpect(jsonPath("$.shoppingItems[0].productId.value").value(productId));
    }

    @Test
    void customerCanGetTheirCart() throws Exception {
        String productId = createProductAsVendor("Tea");

        var auth = login(CUSTOMER_EMAIL, CUSTOMER_PASS);
        String token = auth.get("token").asText();
        String customerId = auth.get("userId").asText();

        // Add to cart first
        mockMvc.perform(patch("/api/carts/add")
                .header("Authorization", bearerOf(token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new AddShoppingItemToCartRestRequest(customerId, productId, 1))));

        // Get cart
        mockMvc.perform(get("/api/carts/" + customerId)
                        .header("Authorization", bearerOf(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shoppingItems", hasSize(1)));
    }

    @Test
    void customerCanRemoveItemFromCart() throws Exception {
        String productId = createProductAsVendor("Sugar");

        var auth = login(CUSTOMER_EMAIL, CUSTOMER_PASS);
        String token = auth.get("token").asText();
        String customerId = auth.get("userId").asText();

        // Add
        mockMvc.perform(patch("/api/carts/add")
                .header("Authorization", bearerOf(token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new AddShoppingItemToCartRestRequest(customerId, productId, 1))));

        // Remove
        RemoveShoppingItemToCartRestRequest req = new RemoveShoppingItemToCartRestRequest(customerId, productId, 1);
        mockMvc.perform(patch("/api/carts/remove")
                        .header("Authorization", bearerOf(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shoppingItems", hasSize(0)));
    }

    @Test
    void customerCanClearTheirCart() throws Exception {
        String productId1 = createProductAsVendor("Item 1");
        String productId2 = createProductAsVendor("Item 2");

        var auth = login(CUSTOMER_EMAIL, CUSTOMER_PASS);
        String token = auth.get("token").asText();
        String customerId = auth.get("userId").asText();

        // Add 2 items
        mockMvc.perform(patch("/api/carts/add").header("Authorization", bearerOf(token))
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(new AddShoppingItemToCartRestRequest(customerId, productId1, 1))));
        mockMvc.perform(patch("/api/carts/add").header("Authorization", bearerOf(token))
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(new AddShoppingItemToCartRestRequest(customerId, productId2, 1))));

        // Clear
        mockMvc.perform(patch("/api/carts/clear/" + customerId)
                        .header("Authorization", bearerOf(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shoppingItems", hasSize(0)));
    }
}
