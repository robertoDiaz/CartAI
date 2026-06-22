/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.identity.controllers;

import cart.ai.shopping.application.usecases.identity.user.DeleteUserUseCase;
import cart.ai.shopping.application.usecases.identity.user.GetUserUseCase;
import cart.ai.shopping.application.usecases.identity.user.ListUserUseCase;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.model.identity.User;
import cart.ai.shopping.domain.model.identity.vos.UserId;
import cart.ai.shopping.infrastructure.in.rest.common.ResultErrorHttpStatusMapper;
import cart.ai.shopping.infrastructure.in.rest.identity.dtos.UserRestResponse;
import cart.ai.shopping.infrastructure.in.rest.identity.mappers.UserRestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('READ_USERS')")
    public ResponseEntity<?> getUsers() {
        Result<List<User>> result = listUserUseCase.execute();
        List<UserRestResponse> responses = result.getValue().stream()
                .map(UserRestMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
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
}
