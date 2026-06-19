/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.identity.dtos;

import java.util.List;

/**
 * @author Roberto Díaz
 */
public record AuthRestResponse(

        String token,

        String email,

        String name,

        List<String> roles

) {
}
