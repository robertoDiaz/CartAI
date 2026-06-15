package com.bikemmerce.commerce.adapters.out.mongo;

import com.bikemmerce.commerce.adapters.out.mongo.documents.dto.CustomerDocument;
import com.bikemmerce.commerce.domain.model.value.objects.Email;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerMongoRepository extends MongoRepository<CustomerDocument, String> {

    Optional<CustomerDocument> findByEmail(Email email);

}
