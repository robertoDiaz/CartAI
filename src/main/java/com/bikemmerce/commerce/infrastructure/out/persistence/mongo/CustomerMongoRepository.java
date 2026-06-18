package com.bikemmerce.commerce.infrastructure.out.persistence.mongo;

import com.bikemmerce.commerce.domain.model.value.objects.Email;
import com.bikemmerce.commerce.infrastructure.out.persistence.mongo.documents.CustomerDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerMongoRepository extends MongoRepository<CustomerDocument, String> {

    Optional<CustomerDocument> findByEmail(Email email);

}
