/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.identity.user;

import cart.ai.shopping.application.usecases.identity.user.commands.AuthenticateUserCommand;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.model.identity.Role;
import cart.ai.shopping.domain.model.identity.User;
import cart.ai.shopping.domain.model.identity.vos.Email;
import cart.ai.shopping.domain.model.identity.vos.Permission;
import cart.ai.shopping.domain.model.identity.vos.RoleId;
import cart.ai.shopping.domain.model.identity.vos.UserId;
import cart.ai.shopping.domain.ports.identity.PasswordEncoderPort;
import cart.ai.shopping.domain.ports.identity.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static cart.ai.shopping.domain.common.result.ResultError.UNAUTHORIZED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for AuthenticateUserUseCase.
 *
 * @author Roberto Díaz
 */
@ExtendWith(MockitoExtension.class)
class AuthenticateUserUseCaseTest {

    private static final Role CUSTOMER_ROLE = new Role(new RoleId("1"), "CUSTOMER", Set.of(new Permission("ROLE_CUSTOMER")));
    private static final User EXISTING_USER = new User(
            new UserId("1001"), "John", new Email("john@test.com"), "hashed-pass", Set.of(CUSTOMER_ROLE)
    );
    @Mock
    private UserRepositoryPort userRepositoryPort;
    @Mock
    private PasswordEncoderPort passwordEncoderPort;
    private AuthenticateUserUseCase authenticateUserUseCase;

    @BeforeEach
    void setUp() {
        authenticateUserUseCase = new AuthenticateUserUseCase(userRepositoryPort, passwordEncoderPort);
    }

    @Test
    void shouldAuthenticateSuccessfully() {
        when(userRepositoryPort.findByEmail(new Email("john@test.com"))).thenReturn(EXISTING_USER);
        when(passwordEncoderPort.matches("raw-pass", "hashed-pass")).thenReturn(true);

        Result<User> result = authenticateUserUseCase.execute(new AuthenticateUserCommand("john@test.com", "raw-pass"));

        assertFalse(result.hasError());
        assertEquals(EXISTING_USER, result.getValue());
    }

    @Test
    void shouldReturnUnauthorizedWhenUserNotFound() {
        when(userRepositoryPort.findByEmail(new Email("unknown@test.com"))).thenReturn(null);

        Result<User> result = authenticateUserUseCase.execute(new AuthenticateUserCommand("unknown@test.com", "pass"));

        assertTrue(result.hasError());
        assertEquals(UNAUTHORIZED, result.getError());
    }

    @Test
    void shouldReturnUnauthorizedWhenPasswordDoesNotMatch() {
        when(userRepositoryPort.findByEmail(new Email("john@test.com"))).thenReturn(EXISTING_USER);
        when(passwordEncoderPort.matches("wrong-pass", "hashed-pass")).thenReturn(false);

        Result<User> result = authenticateUserUseCase.execute(new AuthenticateUserCommand("john@test.com", "wrong-pass"));

        assertTrue(result.hasError());
        assertEquals(UNAUTHORIZED, result.getError());
    }
}
