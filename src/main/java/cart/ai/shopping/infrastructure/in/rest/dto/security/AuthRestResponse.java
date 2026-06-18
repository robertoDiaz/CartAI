package cart.ai.shopping.infrastructure.in.rest.dto.security;

import java.util.List;

public record AuthRestResponse(

        String token,

        String email,

        String name,

        List<String> roles

) {
}
