package com.bikemmerce.commerce.infrastructure.out.persistence.mongo.security;

import com.bikemmerce.commerce.infrastructure.out.persistence.mongo.security.documents.RoleDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleMongoRepository extends MongoRepository<RoleDocument, String> {

    Optional<RoleDocument> findByName(String name);

}
