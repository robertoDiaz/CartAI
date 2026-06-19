/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.identity.user;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.model.identity.User;
import cart.ai.shopping.domain.ports.identity.UserRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author Roberto Díaz
 */
@RequiredArgsConstructor
@UseCase
public class ListUserUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public Result<List<User>> execute() {
        return Result.success(userRepositoryPort.findAll());
    }
}
