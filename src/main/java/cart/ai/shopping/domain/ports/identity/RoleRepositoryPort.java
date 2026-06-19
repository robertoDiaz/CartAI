/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.domain.ports.identity;

import cart.ai.shopping.domain.model.identity.Role;
import cart.ai.shopping.domain.model.identity.vos.RoleId;
import cart.ai.shopping.domain.model.identity.vos.UserId;

import java.util.List;

/**
 * @author Roberto Díaz
 */
public interface RoleRepositoryPort {

    void delete(RoleId userId);

    Role findByRoleId(RoleId userId);

    Role findByName(String name);

    List<Role> findUserRoles(UserId userId);

    List<Role> findAll();

    Role save(Role product);
}
