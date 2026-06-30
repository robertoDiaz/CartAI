/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.identity.user;

import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.model.identity.Role;
import cart.ai.shopping.domain.model.identity.User;
import cart.ai.shopping.domain.model.identity.vos.Email;
import cart.ai.shopping.domain.model.identity.vos.Permission;
import cart.ai.shopping.domain.model.identity.vos.RoleId;
import cart.ai.shopping.domain.model.identity.vos.UserId;
import cart.ai.shopping.domain.ports.identity.UserRepositoryPort;
import cart.ai.shopping.domain.ports.storage.StoragePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static cart.ai.shopping.domain.common.result.ResultError.NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for DeleteUserUseCase.
 *
 * @author Roberto Díaz
 */
@ExtendWith(MockitoExtension.class)
class DeleteUserUseCaseTest {

    private static final Role CUSTOMER_ROLE = new Role(new RoleId("1"), "CUSTOMER", Set.of(new Permission("ROLE_CUSTOMER")));
    private static final UserId USER_ID = new UserId("1001");
    @Mock
    private UserRepositoryPort userRepositoryPort;
    @Mock
    private StoragePort storagePort;
    private DeleteUserUseCase deleteUserUseCase;

    @BeforeEach
    void setUp() {
        deleteUserUseCase = new DeleteUserUseCase(userRepositoryPort, storagePort);
    }

    @Test
    void shouldReturnNotFoundWhenUserDoesNotExist() {
        when(userRepositoryPort.findByUserId(USER_ID)).thenReturn(null);

        Result<User> result = deleteUserUseCase.execute(USER_ID);

        assertTrue(result.hasError());
        assertEquals(NOT_FOUND, result.getError());
        verify(userRepositoryPort, never()).delete(any());
        verify(storagePort, never()).deleteFile(any());
    }

    @Test
    void shouldDeleteUserAndCleanupAvatar() {
        User userWithAvatar = new User(USER_ID, "John", new Email("john@test.com"), "hash", Set.of(CUSTOMER_ROLE), "avatar.jpg");
        when(userRepositoryPort.findByUserId(USER_ID)).thenReturn(userWithAvatar);

        Result<User> result = deleteUserUseCase.execute(USER_ID);

        assertFalse(result.hasError());
        assertEquals(userWithAvatar, result.getValue());
        verify(userRepositoryPort).delete(USER_ID);
        verify(storagePort).deleteFile("avatar.jpg");
    }

    @Test
    void shouldDeleteUserWithoutCallingStorageWhenNoAvatar() {
        User userWithoutAvatar = new User(USER_ID, "John", new Email("john@test.com"), "hash", Set.of(CUSTOMER_ROLE));
        when(userRepositoryPort.findByUserId(USER_ID)).thenReturn(userWithoutAvatar);

        Result<User> result = deleteUserUseCase.execute(USER_ID);

        assertFalse(result.hasError());
        verify(userRepositoryPort).delete(USER_ID);
        verify(storagePort, never()).deleteFile(any());
    }
}
