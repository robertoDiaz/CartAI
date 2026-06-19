/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.security.cotroller;

import cart.ai.shopping.application.usecases.identity.commands.CreateRoleCommand;
import cart.ai.shopping.application.usecases.identity.role.CreateRoleUseCase;
import cart.ai.shopping.application.usecases.identity.user.AuthenticateUserUseCase;
import cart.ai.shopping.application.usecases.identity.user.CreateUserUseCase;
import cart.ai.shopping.domain.model.identity.Role;
import cart.ai.shopping.domain.model.identity.User;
import cart.ai.shopping.domain.model.identity.value.objects.Permission;
import cart.ai.shopping.domain.ports.identity.repositories.RoleRepositoryPort;
import cart.ai.shopping.domain.result.Result;
import cart.ai.shopping.infrastructure.in.rest.security.dto.LoginRestRequest;
import cart.ai.shopping.infrastructure.in.rest.security.dto.RegisterRestRequest;
import cart.ai.shopping.infrastructure.in.rest.security.mapper.AuthRestMapper;
import cart.ai.shopping.infrastructure.security.services.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Roberto Díaz
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final CreateUserUseCase createUserUseCase;
    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final CreateRoleUseCase createRoleUseCase;
    private final RoleRepositoryPort roleRepositoryPort;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRestRequest request) {
        Role customerRole = getOrCreateCustomerRole();
        Result<User> result = createUserUseCase.execute(AuthRestMapper.toCreateUserCommand(request, Set.of(customerRole)));

        if (result.hasError()) {
            return ResponseEntity.status(result.getErrorCode()).body("Registration failed.");
        }

        return ResponseEntity.ok(AuthRestMapper.toResponse(
                result.getValue(),
                generateTokenForUser(result.getValue())
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRestRequest request) {
        Result<User> result = authenticateUserUseCase.execute(AuthRestMapper.toAuthenticateUserCommand(request));

        if (result.hasError()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
        }

        return ResponseEntity.ok(AuthRestMapper.toResponse(
                result.getValue(),
                generateTokenForUser(result.getValue())
        ));
    }

    private Role getOrCreateCustomerRole() {
        Role role = roleRepositoryPort.findByName("CUSTOMER");

        if (role == null) {
            Result<Role> result = createRoleUseCase.execute(new CreateRoleCommand(
                    "CUSTOMER",
                    Set.of("WRITE_ORDERS", "READ_ORDERS", "WRITE_CARTS", "READ_CARTS")
            ));
            if (result.hasError()) {
                throw new IllegalStateException("Could not create CUSTOMER role");
            }
            return result.getValue();
        }

        return role;
    }

    private String generateTokenForUser(User user) {
        Map<String, Object> extraClaims = Map.of(
                "roles", user.roles().stream().map(Role::name).toList(),
                "permissions", user.roles().stream()
                        .flatMap(role -> role.permissions().stream())
                        .map(Permission::value)
                        .collect(Collectors.toSet())
        );

        return jwtService.generateToken(user.email().value(), extraClaims);
    }
}
