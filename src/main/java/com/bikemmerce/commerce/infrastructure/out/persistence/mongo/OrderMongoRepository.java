package com.bikemmerce.commerce.infrastructure.out.persistence.mongo;

import com.bikemmerce.commerce.infrastructure.out.persistence.mongo.documents.OrderDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMongoRepository extends MongoRepository<OrderDocument, String> {
}
