package com.bikemmerce.commerce.adapters.out.mongo;

import com.bikemmerce.commerce.adapters.out.mongo.documents.dto.ProductDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductMongoRepository extends MongoRepository<ProductDocument, String> {}
