/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.identity.controllers;

import cart.ai.shopping.application.usecases.identity.role.CreateRoleUseCase;
import cart.ai.shopping.application.usecases.identity.role.DeleteRoleUseCase;
import cart.ai.shopping.application.usecases.identity.role.GetRoleUseCase;
import cart.ai.shopping.application.usecases.identity.role.ListRoleUseCase;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.model.identity.Role;
import cart.ai.shopping.domain.model.identity.vos.RoleId;
import cart.ai.shopping.infrastructure.in.rest.common.ResultErrorHttpStatusMapper;
import cart.ai.shopping.infrastructure.in.rest.identity.dtos.CreateRoleRestRequest;
import cart.ai.shopping.infrastructure.in.rest.identity.dtos.RoleRestResponse;
import cart.ai.shopping.infrastructure.in.rest.identity.mappers.RoleRestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Roberto Díaz
 */
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleRestController {

    private final CreateRoleUseCase createRoleUseCase;
    private final DeleteRoleUseCase deleteRoleUseCase;
    private final GetRoleUseCase getRoleUseCase;
    private final ListRoleUseCase listRoleUseCase;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('WRITE_ROLES')")
    public ResponseEntity<?> createRole(@RequestBody @Valid CreateRoleRestRequest request) {
        Result<Role> result = createRoleUseCase.execute(RoleRestMapper.toCreateRoleCommand(request));

        if (result.hasError()) {
            return ResponseEntity.status(ResultErrorHttpStatusMapper.toHttpStatus(result.getError())).body("Could not create role.");
        }

        return ResponseEntity.ok(RoleRestMapper.toResponse(result.getValue()));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('READ_ROLES')")
    public ResponseEntity<?> getRoles() {
        Result<List<Role>> result = listRoleUseCase.execute();
        List<RoleRestResponse> responses = result.getValue().stream()
                .map(RoleRestMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('READ_ROLES')")
    public ResponseEntity<?> getRoleById(@PathVariable String id) {
        Result<Role> result = getRoleUseCase.execute(new RoleId(id));

        if (result.hasError()) {
            return ResponseEntity.status(ResultErrorHttpStatusMapper.toHttpStatus(result.getError())).body("Role not found.");
        }

        return ResponseEntity.ok(RoleRestMapper.toResponse(result.getValue()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('WRITE_ROLES')")
    public ResponseEntity<?> deleteRole(@PathVariable String id) {
        Result<Role> result = deleteRoleUseCase.execute(new RoleId(id));

        if (result.hasError()) {
            return ResponseEntity.status(ResultErrorHttpStatusMapper.toHttpStatus(result.getError())).body("Could not delete role.");
        }

        return ResponseEntity.ok(RoleRestMapper.toResponse(result.getValue()));
    }
}
