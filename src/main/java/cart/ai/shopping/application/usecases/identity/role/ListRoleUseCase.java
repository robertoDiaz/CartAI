/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.identity.role;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.model.identity.Role;
import cart.ai.shopping.domain.ports.identity.RoleRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author Roberto Díaz
 */
@RequiredArgsConstructor
@UseCase
public class ListRoleUseCase {

    private final RoleRepositoryPort roleRepositoryPort;

    public Result<List<Role>> execute() {
        return Result.success(roleRepositoryPort.findAll());
    }
}
