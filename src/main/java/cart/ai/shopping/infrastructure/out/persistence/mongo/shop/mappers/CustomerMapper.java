/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.out.persistence.mongo.shop.mappers;

import cart.ai.shopping.domain.model.identity.vos.Email;
import cart.ai.shopping.domain.model.identity.vos.UserId;
import cart.ai.shopping.domain.model.shop.Customer;
import cart.ai.shopping.infrastructure.out.persistence.mongo.shop.documents.CustomerDocument;

/**
 * @author Roberto Díaz
 */
public class CustomerMapper {

    public static CustomerDocument toDocument(Customer customer) {
        return CustomerDocument.builder()
                .id(customer.userId().value())
                .name(customer.name())
                .email(customer.email().value())
                .build();
    }

    public static Customer toDomain(CustomerDocument customerDocument) {
        return new Customer(
                new UserId(customerDocument.getId()),
                customerDocument.getName(),
                new Email(customerDocument.getEmail())
        );
    }
}
