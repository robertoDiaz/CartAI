package com.bikemmerce.commerce.adapters.out.mongo;

import com.bikemmerce.commerce.adapters.out.mongo.documents.dto.CartDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartMongoRepository extends MongoRepository<CartDocument, String> {}
