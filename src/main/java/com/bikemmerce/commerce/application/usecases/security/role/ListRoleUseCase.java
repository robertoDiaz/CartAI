package com.bikemmerce.commerce.application.usecases.security.role;

import com.bikemmerce.commerce.application.annotations.UseCase;
import com.bikemmerce.commerce.domain.model.security.Role;
import com.bikemmerce.commerce.domain.ports.security.RoleRepositoryPort;
import com.bikemmerce.commerce.domain.result.Result;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@UseCase
public class ListRoleUseCase {

    private final RoleRepositoryPort roleRepositoryPort;

    public Result<List<Role>> execute() {
        return Result.success(roleRepositoryPort.findAll());
    }
}
