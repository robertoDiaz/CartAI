/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.out.persistence.mongo.storage.repositories;

import cart.ai.shopping.infrastructure.out.persistence.mongo.storage.documents.StoredFileDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Roberto Díaz
 */
@Repository
public interface StoredFileMongoRepository extends MongoRepository<StoredFileDocument, String> {


}
