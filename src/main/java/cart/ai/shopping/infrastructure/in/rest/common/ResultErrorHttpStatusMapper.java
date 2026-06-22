/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.common;

import cart.ai.shopping.domain.common.result.ResultError;
import org.springframework.http.HttpStatus;

/**
 * @author Roberto Díaz
 */
public final class ResultErrorHttpStatusMapper {

    private ResultErrorHttpStatusMapper() {
    }

    public static HttpStatus toHttpStatus(ResultError error) {
        return switch (error) {
            case BAD_REQUEST -> HttpStatus.BAD_REQUEST;
            case UNAUTHORIZED -> HttpStatus.UNAUTHORIZED;
            case FORBIDDEN -> HttpStatus.FORBIDDEN;
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case CONFLICT -> HttpStatus.CONFLICT;
            case INTERNAL_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
