/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.security.adapters;

import cart.ai.shopping.infrastructure.security.services.TokenBlacklistService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory adapter for the token blacklist.
 * Useful for development and single-instance environments.
 *
 * @author Roberto Díaz
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "app.security.blacklist.provider", havingValue = "inmemory", matchIfMissing = true)
public class InMemoryTokenBlacklistAdapter implements TokenBlacklistService {

    private final Map<String, Long> blacklist = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        log.warn("InMemoryTokenBlacklistAdapter is active. WARNING: This implementation is NOT suitable for production environments with multiple instances.");
    }

    @Override
    public void blacklistToken(String token, long expirationTimeMs) {
        if (expirationTimeMs > 0) {
            blacklist.put(token, System.currentTimeMillis() + expirationTimeMs);
        }
        // Basic self-cleanup
        cleanupExpiredTokens();
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        Long expiry = blacklist.get(token);
        if (expiry == null) {
            return false;
        }
        if (expiry < System.currentTimeMillis()) {
            blacklist.remove(token);
            return false;
        }
        return true;
    }

    private void cleanupExpiredTokens() {
        long now = System.currentTimeMillis();
        blacklist.entrySet().removeIf(entry -> entry.getValue() < now);
    }
}
