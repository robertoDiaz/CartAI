package com.bikemmerce.commerce.application.usecases.security.role;

import com.bikemmerce.commerce.application.annotations.UseCase;
import com.bikemmerce.commerce.application.usecases.security.commands.CreateRoleCommand;
import com.bikemmerce.commerce.domain.model.security.Role;
import com.bikemmerce.commerce.domain.model.security.value.objects.Permission;
import com.bikemmerce.commerce.domain.model.security.value.objects.RoleId;
import com.bikemmerce.commerce.domain.ports.common.IncrementIdGeneratorPort;
import com.bikemmerce.commerce.domain.ports.security.RoleRepositoryPort;
import com.bikemmerce.commerce.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@UseCase
public class CreateRoleUseCase {

    private final RoleRepositoryPort roleRepositoryPort;
    private final IncrementIdGeneratorPort incrementIdGeneratorPort;

    public Result<Role> execute(CreateRoleCommand command) {
        RoleId roleId = new RoleId(incrementIdGeneratorPort.generate(Role.class));

        if (roleRepositoryPort.findByRoleId(roleId) != null) {
            return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        Role role = new Role(
                roleId,
                command.name(),
                command.permissions().stream()
                        .map(Permission::new)
                        .collect(Collectors.toSet())
        );

        return Result.success(roleRepositoryPort.save(role));
    }
}
