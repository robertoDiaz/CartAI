package com.bikemmerce.commerce.application.usecases.security.user;

import com.bikemmerce.commerce.application.annotations.UseCase;
import com.bikemmerce.commerce.domain.model.security.User;
import com.bikemmerce.commerce.domain.ports.security.UserRepositoryPort;
import com.bikemmerce.commerce.domain.result.Result;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@UseCase
public class ListUserUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public Result<List<User>> execute() {
        return Result.success(userRepositoryPort.findAll());
    }
}
