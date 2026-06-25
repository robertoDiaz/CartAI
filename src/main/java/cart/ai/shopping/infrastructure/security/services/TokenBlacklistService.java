/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.security.services;

/**
 * Port representing the token blacklisting mechanism.
 * Can be implemented for in-memory or Redis caching.
 *
 * @author Roberto Díaz
 */
public interface TokenBlacklistService {
    /**
     * Blacklists a token for a given expiration duration in milliseconds.
     *
     * @param token            the JWT token string
     * @param expirationTimeMs remaining lifetime of the token in milliseconds
     */
    void blacklistToken(String token, long expirationTimeMs);

    /**
     * Checks if a token is blacklisted.
     *
     * @param token the JWT token string
     * @return true if the token is blacklisted, false otherwise
     */
    boolean isTokenBlacklisted(String token);
}
