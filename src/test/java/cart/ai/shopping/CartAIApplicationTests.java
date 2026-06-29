/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping;

import cart.ai.shopping.infrastructure.in.rest.BaseIT;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Smoke test — verifies the Spring application context loads successfully.
 *
 * @author Roberto Díaz
 */
class CartAIApplicationTests extends BaseIT {

    @Test
    void contextLoads() {
        assertThat(mockMvc).isNotNull();
        assertThat(objectMapper).isNotNull();
        assertThat(mongoTemplate).isNotNull();
    }
}
