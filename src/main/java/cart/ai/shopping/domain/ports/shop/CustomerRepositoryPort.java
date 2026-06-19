/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.domain.ports.shop;

import cart.ai.shopping.domain.model.identity.vos.Email;
import cart.ai.shopping.domain.model.identity.vos.UserId;
import cart.ai.shopping.domain.model.shop.Customer;

import java.util.List;

/**
 * @author Roberto Díaz
 */
public interface CustomerRepositoryPort {

    void delete(UserId userId);

    Customer findByCustomerId(UserId userId);

    Customer findByEmail(Email email);

    List<Customer> findAll();

    Customer save(Customer product);

}
