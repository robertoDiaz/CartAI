/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.domain.ports.identity;

import cart.ai.shopping.domain.model.identity.User;
import cart.ai.shopping.domain.model.identity.vos.Email;
import cart.ai.shopping.domain.model.identity.vos.UserId;

import java.util.List;

/**
 * @author Roberto Díaz
 */
public interface UserRepositoryPort {

    void delete(UserId userId);

    User findByUserId(UserId userId);

    User findByEmail(Email email);

    List<User> findAll();

    User save(User product);
}
