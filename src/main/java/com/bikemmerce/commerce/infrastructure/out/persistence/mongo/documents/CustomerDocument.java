package com.bikemmerce.commerce.infrastructure.out.persistence.mongo.documents;

import com.bikemmerce.commerce.domain.model.value.objects.Email;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("customers")
@Data
public class CustomerDocument {

    @Id
    private final String id;

    private final String name;
    private final Email email;

}
