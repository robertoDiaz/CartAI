package cart.ai.shopping.infrastructure.in.rest.identity;

import cart.ai.shopping.infrastructure.in.rest.BaseIT;
import cart.ai.shopping.infrastructure.in.rest.identity.dtos.CreateRoleRestRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Atomic integration tests for role management.
 */
class RoleIT extends BaseIT {

    private static final String CUSTOM_ROLE = "SUPERVISOR";

    @Test
    void adminCanCreateAndGetRole() throws Exception {
        var auth = login(ADMIN_EMAIL, ADMIN_PASS);
        String token = auth.get("token").asText();

        CreateRoleRestRequest req = new CreateRoleRestRequest(CUSTOM_ROLE, java.util.Set.of("READ_REPORTS"));

        // Create
        String response = mockMvc.perform(post("/api/roles")
                        .header("Authorization", bearerOf(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(CUSTOM_ROLE))
                .andReturn().getResponse().getContentAsString();

        String roleId = objectMapper.readTree(response).get("id").asText();

        // Get
        mockMvc.perform(get("/api/roles/" + roleId)
                        .header("Authorization", bearerOf(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(CUSTOM_ROLE));
                
        // Cleanup explicitly
        mockMvc.perform(delete("/api/roles/" + roleId)
                        .header("Authorization", bearerOf(token)))
                .andExpect(status().isOk());
    }

    @Test
    void customerCannotCreateRole() throws Exception {
        var auth = login(CUSTOMER_EMAIL, CUSTOMER_PASS);
        String token = auth.get("token").asText();

        CreateRoleRestRequest req = new CreateRoleRestRequest("HACKER", java.util.Set.of("ALL_PERMISSIONS"));

        mockMvc.perform(post("/api/roles")
                        .header("Authorization", bearerOf(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isForbidden());
    }
}
