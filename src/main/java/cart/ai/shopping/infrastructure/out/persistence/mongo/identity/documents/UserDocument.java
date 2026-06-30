/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.out.persistence.mongo.identity.documents;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

/**
 * @author Roberto Díaz
 */
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

    private final String avatarFileId;
    
    private final String phone;
    
    private final String taxId;
    
    private final String preferredLanguage;

}
