/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.identity.controllers;

import cart.ai.shopping.application.usecases.identity.user.AuthenticateUserUseCase;
import cart.ai.shopping.application.usecases.identity.user.CreateUserUseCase;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.model.identity.Role;
import cart.ai.shopping.domain.model.identity.User;
import cart.ai.shopping.domain.ports.identity.RoleRepositoryPort;
import cart.ai.shopping.infrastructure.in.rest.common.ResultErrorHttpStatusMapper;
import cart.ai.shopping.infrastructure.in.rest.identity.dtos.LoginRestRequest;
import cart.ai.shopping.infrastructure.in.rest.identity.dtos.RegisterRestRequest;
import cart.ai.shopping.infrastructure.in.rest.identity.mappers.AuthRestMapper;
import cart.ai.shopping.infrastructure.security.services.JwtService;
import cart.ai.shopping.infrastructure.security.services.TokenBlacklistService;
import cart.ai.shopping.infrastructure.security.services.UserAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * @author Roberto Díaz
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final CreateUserUseCase createUserUseCase;
    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final RoleRepositoryPort roleRepositoryPort;
    private final UserAuthService userAuthService;
    private final TokenBlacklistService tokenBlacklistService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRestRequest request) {
        Role customerRole = roleRepositoryPort.findByName("CUSTOMER");

        if (customerRole == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Critical error: CUSTOMER role not found in system.");
        }

        Result<User> result = createUserUseCase.execute(AuthRestMapper.toCreateUserCommand(request, Set.of(customerRole)));

        if (result.hasError()) {
            return ResponseEntity.status(ResultErrorHttpStatusMapper.toHttpStatus(result.getError())).body("Registration failed.");
        }

        return ResponseEntity.ok(AuthRestMapper.toResponse(
                result.getValue(),
                userAuthService.generateToken(result.getValue())
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
                userAuthService.generateToken(result.getValue())
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            long remainingTimeMs = jwtService.getRemainingExpirationTimeMs(token);
            if (remainingTimeMs > 0) {
                tokenBlacklistService.blacklistToken(token, remainingTimeMs);
            }
        }
        return ResponseEntity.ok("Successfully logged out.");
    }
}
