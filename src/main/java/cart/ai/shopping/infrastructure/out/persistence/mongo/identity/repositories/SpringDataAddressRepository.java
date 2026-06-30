/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.out.persistence.mongo.identity.repositories;

import cart.ai.shopping.infrastructure.out.persistence.mongo.identity.documents.AddressDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Roberto Díaz
 */
@Repository
public interface SpringDataAddressRepository extends MongoRepository<AddressDocument, String> {
    List<AddressDocument> findByOwnerIdAndOwnerType(String ownerId, String ownerType);
}
