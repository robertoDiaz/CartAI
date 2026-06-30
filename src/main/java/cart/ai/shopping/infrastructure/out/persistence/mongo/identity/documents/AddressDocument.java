/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.out.persistence.mongo.identity.documents;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Roberto Díaz
 */
@Document("addresses")
@Data
@Builder
public class AddressDocument {

    @Id
    private final String id;

    private final String ownerId;

    private final String ownerType;

    private final String alias;

    private final String firstName;

    private final String lastName;

    private final String company;

    private final String street;

    private final String city;

    private final String zipCode;

    private final String phone;

    private final String state;

    private final String country;

    private final String notes;

    private final boolean isDefault;
}
