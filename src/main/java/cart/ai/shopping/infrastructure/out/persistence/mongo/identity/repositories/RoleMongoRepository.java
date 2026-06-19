/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.out.persistence.mongo.identity.repositories;

import cart.ai.shopping.infrastructure.out.persistence.mongo.identity.documents.RoleDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Roberto Díaz
 */
@Repository
public interface RoleMongoRepository extends MongoRepository<RoleDocument, String> {

    Optional<RoleDocument> findByName(String name);

}
