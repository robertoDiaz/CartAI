package com.bikemmerce.commerce.infrastructure.out.persistence.mongo.security.documents;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document("users")
@Data
@Builder
public class UserDocument {

    @Id
    private final String id;

    private final String name;

    private final String email;

    private final String passwordHash;

    private final Set<String> roleIds;

}
