/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.identity.user;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.application.usecases.identity.commands.AuthenticateUserCommand;
import cart.ai.shopping.domain.model.identity.User;
import cart.ai.shopping.domain.model.identity.value.objects.Email;
import cart.ai.shopping.domain.ports.identity.repositories.UserRepositoryPort;
import cart.ai.shopping.domain.ports.security.PasswordEncoderPort;
import cart.ai.shopping.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author Roberto Díaz
 */
@RequiredArgsConstructor
@UseCase
public class AuthenticateUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;

    public Result<User> execute(AuthenticateUserCommand command) {
        User user = userRepositoryPort.findByEmail(new Email(command.email()));

        if (user == null || !passwordEncoderPort.matches(command.password(), user.passwordHash())) {
            return Result.error(HttpStatus.UNAUTHORIZED.value());
        }

        return Result.success(user);
    }
}
