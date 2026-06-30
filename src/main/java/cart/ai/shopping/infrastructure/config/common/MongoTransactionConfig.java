/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.config.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;

/**
 * Enables MongoDB multi-document transactions.
 * Requires MongoDB to be running as a replica set.
 *
 * @author Roberto Díaz
 */
@Configuration
@org.springframework.boot.autoconfigure.condition.ConditionalOnProperty(name = "cartai.mongo.transaction.enabled", havingValue = "true", matchIfMissing = true)
public class MongoTransactionConfig {

    @Bean
    public MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }
}
