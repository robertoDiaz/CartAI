/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.identity.role;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.application.usecases.identity.role.commands.CreateRoleCommand;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.model.identity.Role;
import cart.ai.shopping.domain.model.identity.vos.Permission;
import cart.ai.shopping.domain.model.identity.vos.RoleId;
import cart.ai.shopping.domain.ports.common.IncrementIdGeneratorPort;
import cart.ai.shopping.domain.ports.identity.RoleRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;

import static cart.ai.shopping.domain.common.result.ResultError.INTERNAL_ERROR;

/**
 * @author Roberto Díaz
 */
@RequiredArgsConstructor
@UseCase
public class CreateRoleUseCase {

    private final RoleRepositoryPort roleRepositoryPort;
    private final IncrementIdGeneratorPort incrementIdGeneratorPort;

    public Result<Role> execute(CreateRoleCommand command) {
        RoleId roleId = new RoleId(incrementIdGeneratorPort.generate(Role.class));

        if (roleRepositoryPort.findByRoleId(roleId) != null) {
            return Result.error(INTERNAL_ERROR);
        }

        Role role = new Role(
                roleId,
                command.name(),
                command.permissions().stream()
                        .map(Permission::new)
                        .collect(Collectors.toSet())
        );

        return Result.success(roleRepositoryPort.save(role));
    }
}
