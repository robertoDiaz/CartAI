package com.bikemmerce.commerce.infrastructure.out.mongo;

import com.bikemmerce.commerce.infrastructure.out.mongo.documents.dto.OrderDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMongoRepository extends MongoRepository<OrderDocument, String> {
}
