/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.identity.controllers;

import cart.ai.shopping.application.usecases.identity.user.*;
import cart.ai.shopping.application.usecases.identity.user.commands.CreateUserCommand;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.model.identity.Role;
import cart.ai.shopping.domain.model.identity.User;
import cart.ai.shopping.domain.model.identity.vos.UserId;
import cart.ai.shopping.domain.ports.identity.RoleRepositoryPort;
import cart.ai.shopping.infrastructure.in.rest.common.ResultErrorHttpStatusMapper;
import cart.ai.shopping.infrastructure.in.rest.identity.dtos.CreateUserRestRequest;
import cart.ai.shopping.infrastructure.in.rest.identity.dtos.UpdateUserRestRequest;
import cart.ai.shopping.infrastructure.in.rest.identity.dtos.UserRestResponse;
import cart.ai.shopping.infrastructure.in.rest.identity.mappers.UserRestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Roberto Díaz
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {

    private final GetUserUseCase getUserUseCase;
    private final ListUserUseCase listUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final CreateUserUseCase createUserUseCase;
    private final RoleRepositoryPort roleRepositoryPort;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('READ_USERS')")
    public ResponseEntity<?> getUsers() {
        Result<List<User>> result = listUserUseCase.execute();
        List<UserRestResponse> responses = result.getValue().stream()
                .map(UserRestMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('WRITE_USERS')")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRestRequest request) {
        Set<Role> roles = request.roles().stream()
                .map(roleRepositoryPort::findByName)
                .collect(Collectors.toSet());

        if (roles.contains(null)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("One or more roles do not exist.");
        }

        Result<User> result = createUserUseCase.execute(
                new CreateUserCommand(request.name(), request.email(), request.password(), roles, null)
        );

        if (result.hasError()) {
            return ResponseEntity.status(ResultErrorHttpStatusMapper.toHttpStatus(result.getError())).body("User creation failed.");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(UserRestMapper.toResponse(result.getValue()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('READ_USERS') or principal == #id")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        Result<User> result = getUserUseCase.execute(new UserId(id));

        if (result.hasError()) {
            return ResponseEntity.status(ResultErrorHttpStatusMapper.toHttpStatus(result.getError())).body("User not found.");
        }

        return ResponseEntity.ok(UserRestMapper.toResponse(result.getValue()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('WRITE_USERS')")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        Result<User> result = deleteUserUseCase.execute(new UserId(id));

        if (result.hasError()) {
            return ResponseEntity.status(ResultErrorHttpStatusMapper.toHttpStatus(result.getError())).body("Could not delete user.");
        }

        return ResponseEntity.ok(UserRestMapper.toResponse(result.getValue()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('WRITE_USERS') or principal == #id")
    public ResponseEntity<?> updateUser(@PathVariable String id, @Valid @RequestBody UpdateUserRestRequest request) {
        if (!id.equals(request.id())) {
            return ResponseEntity.badRequest().body("ID mismatch");
        }

        Set<Role> roles = request.roles().stream()
                .map(roleRepositoryPort::findByName)
                .collect(Collectors.toSet());

        if (roles.contains(null)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("One or more roles do not exist.");
        }

        Result<User> result = updateUserUseCase.execute(UserRestMapper.toUpdateUserCommand(request, roles));

        if (result.hasError()) {
            return ResponseEntity.status(ResultErrorHttpStatusMapper.toHttpStatus(result.getError())).body("Could not update user.");
        }

        return ResponseEntity.ok(UserRestMapper.toResponse(result.getValue()));
    }

}
