package cart.ai.shopping.infrastructure.in.rest.identity;

import cart.ai.shopping.infrastructure.in.rest.BaseIT;
import cart.ai.shopping.infrastructure.in.rest.identity.dtos.LoginRestRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Atomic integration tests for authentication endpoints.
 */
class AuthIT extends BaseIT {

    @Test
    void loginWithInvalidPasswordReturnsUnauthorized() throws Exception {
        LoginRestRequest req = new LoginRestRequest(CUSTOMER_EMAIL, "wrongpassword");
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginWithNonExistentUserReturnsUnauthorized() throws Exception {
        LoginRestRequest req = new LoginRestRequest("nobody@test.com", "password");
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void logoutBlacklistsToken() throws Exception {
        var auth = login(CUSTOMER_EMAIL, CUSTOMER_PASS);
        String token = auth.get("token").asText();

        // 1. Verify token works
        mockMvc.perform(get("/api/users/" + auth.get("userId").asText())
                        .header("Authorization", bearerOf(token)))
                .andExpect(status().isOk());

        // 2. Logout
        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", bearerOf(token)))
                .andExpect(status().isOk());

        // 3. Verify token no longer works
        mockMvc.perform(get("/api/users/" + auth.get("userId").asText())
                        .header("Authorization", bearerOf(token)))
                .andExpect(status().isForbidden());
    }
}
