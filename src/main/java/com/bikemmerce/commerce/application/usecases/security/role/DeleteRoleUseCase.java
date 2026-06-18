package com.bikemmerce.commerce.application.usecases.security.role;

import com.bikemmerce.commerce.application.annotations.UseCase;
import com.bikemmerce.commerce.domain.model.security.Role;
import com.bikemmerce.commerce.domain.model.security.value.objects.RoleId;
import com.bikemmerce.commerce.domain.ports.security.RoleRepositoryPort;
import com.bikemmerce.commerce.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@UseCase
public class DeleteRoleUseCase {

    private final RoleRepositoryPort roleRepositoryPort;

    public Result<Role> execute(RoleId roleId) {
        Role role = roleRepositoryPort.findByRoleId(roleId);

        if (role == null) {
            return Result.error(HttpStatus.NOT_FOUND.value());
        }

        roleRepositoryPort.delete(roleId);

        return Result.success(role);
    }
}
