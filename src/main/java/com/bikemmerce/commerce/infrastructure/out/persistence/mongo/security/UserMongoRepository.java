package com.bikemmerce.commerce.infrastructure.out.persistence.mongo.security;

import com.bikemmerce.commerce.infrastructure.out.persistence.mongo.security.documents.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserMongoRepository extends MongoRepository<UserDocument, String> {

    Optional<UserDocument> findByEmail(String email);

}
