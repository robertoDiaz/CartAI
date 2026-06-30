/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.identity.user;

import cart.ai.shopping.application.usecases.identity.user.commands.CreateUserCommand;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.model.identity.Role;
import cart.ai.shopping.domain.model.identity.User;
import cart.ai.shopping.domain.model.identity.vos.Email;
import cart.ai.shopping.domain.model.identity.vos.Permission;
import cart.ai.shopping.domain.model.identity.vos.RoleId;
import cart.ai.shopping.domain.model.identity.vos.UserId;
import cart.ai.shopping.domain.ports.common.IncrementIdGeneratorPort;
import cart.ai.shopping.domain.ports.identity.PasswordEncoderPort;
import cart.ai.shopping.domain.ports.identity.UserAddedEventPublisherPort;
import cart.ai.shopping.domain.ports.identity.UserRepositoryPort;
import cart.ai.shopping.domain.ports.storage.StoragePort;
import cart.ai.shopping.domain.ports.storage.TempStoragePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static cart.ai.shopping.domain.common.result.ResultError.CONFLICT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CreateUserUseCase.
 * All dependencies are mocked — no Spring context, no database.
 *
 * @author Roberto Díaz
 */
@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    private static final Role CUSTOMER_ROLE = new Role(new RoleId("1"), "CUSTOMER", Set.of(new Permission("ROLE_CUSTOMER")));
    private static final String GENERATED_ID = "1001";
    @Mock
    private UserRepositoryPort userRepositoryPort;
    @Mock
    private UserAddedEventPublisherPort userAddedEventPublisherPort;
    @Mock
    private IncrementIdGeneratorPort incrementIdGeneratorPort;
    @Mock
    private PasswordEncoderPort passwordEncoderPort;
    @Mock
    private StoragePort storagePort;
    @Mock
    private TempStoragePort tempStoragePort;
    private CreateUserUseCase createUserUseCase;

    @BeforeEach
    void setUp() {
        createUserUseCase = new CreateUserUseCase(
                userRepositoryPort, userAddedEventPublisherPort,
                incrementIdGeneratorPort, passwordEncoderPort,
                storagePort, tempStoragePort
        );
    }

    @Test
    void shouldCreateUserSuccessfully() {
        when(incrementIdGeneratorPort.generate(User.class)).thenReturn(GENERATED_ID);
        when(userRepositoryPort.findByEmail(any())).thenReturn(null);
        when(passwordEncoderPort.encode(any())).thenReturn("hashed-password");
        when(userRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        CreateUserCommand command = new CreateUserCommand("John", "john@test.com", "pass", Set.of(CUSTOMER_ROLE), null);

        Result<User> result = createUserUseCase.execute(command);

        assertFalse(result.hasError());
        assertEquals("John", result.getValue().name());
        assertEquals("john@test.com", result.getValue().email().value());
        assertNull(result.getValue().avatarFileId());
        verify(userRepositoryPort).save(any());
        verify(userAddedEventPublisherPort).added(any());
    }

    @Test
    void shouldReturnConflictWhenEmailAlreadyExists() {
        User existingUser = new User(new UserId("999"), "Existing", new Email("john@test.com"), "hash", Set.of(CUSTOMER_ROLE));
        when(userRepositoryPort.findByEmail(new Email("john@test.com"))).thenReturn(existingUser);

        CreateUserCommand command = new CreateUserCommand("John", "john@test.com", "pass", Set.of(CUSTOMER_ROLE), null);

        Result<User> result = createUserUseCase.execute(command);

        assertTrue(result.hasError());
        assertEquals(CONFLICT, result.getError());
        verify(userRepositoryPort, never()).save(any());
        verify(userAddedEventPublisherPort, never()).added(any());
    }

    @Test
    void shouldPromoteTempAvatarWhenAvatarProvided() {
        when(incrementIdGeneratorPort.generate(User.class)).thenReturn(GENERATED_ID);
        when(userRepositoryPort.findByEmail(any())).thenReturn(null);
        when(passwordEncoderPort.encode(any())).thenReturn("hashed-password");
        when(userRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(tempStoragePort.getBucketName()).thenReturn("test-temp-bucket");

        CreateUserCommand command = new CreateUserCommand("John", "john@test.com", "pass", Set.of(CUSTOMER_ROLE), "avatar-file.jpg");

        Result<User> result = createUserUseCase.execute(command);

        assertFalse(result.hasError());
        assertEquals("avatar-file.jpg", result.getValue().avatarFileId());
        verify(storagePort).promoteFile("avatar-file.jpg", "test-temp-bucket");
    }

    @Test
    void shouldCreateUserEvenWhenAvatarPromotionFails() {
        when(incrementIdGeneratorPort.generate(User.class)).thenReturn(GENERATED_ID);
        when(userRepositoryPort.findByEmail(any())).thenReturn(null);
        when(passwordEncoderPort.encode(any())).thenReturn("hashed-password");
        when(userRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(tempStoragePort.getBucketName()).thenReturn("test-temp-bucket");
        doThrow(new RuntimeException("MinIO unavailable")).when(storagePort).promoteFile(any(), any());

        CreateUserCommand command = new CreateUserCommand("John", "john@test.com", "pass", Set.of(CUSTOMER_ROLE), "avatar-file.jpg");

        Result<User> result = createUserUseCase.execute(command);

        // User is still created even if avatar promotion fails — avatar is just lost
        assertFalse(result.hasError());
        assertNull(result.getValue().avatarFileId());
        verify(userRepositoryPort).save(any());
    }
}
