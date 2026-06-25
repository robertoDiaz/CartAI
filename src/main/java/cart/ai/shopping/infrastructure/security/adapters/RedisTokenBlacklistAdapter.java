/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.security.adapters;

import cart.ai.shopping.infrastructure.security.services.TokenBlacklistService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis adapter for the token blacklist.
 * Suitable for distributed production environments.
 *
 * @author Roberto Díaz
 */
@Component
@ConditionalOnProperty(name = "app.security.blacklist.provider", havingValue = "redis")
public class RedisTokenBlacklistAdapter implements TokenBlacklistService {

    private static final String BLACKLIST_KEY_PREFIX = "jwt:blacklist:";
    private final StringRedisTemplate redisTemplate;

    public RedisTokenBlacklistAdapter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void blacklistToken(String token, long expirationTimeMs) {
        if (expirationTimeMs > 0) {
            redisTemplate.opsForValue().set(
                    BLACKLIST_KEY_PREFIX + token,
                    "true",
                    expirationTimeMs,
                    TimeUnit.MILLISECONDS
            );
        }
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        return redisTemplate.hasKey(BLACKLIST_KEY_PREFIX + token);
    }
}
