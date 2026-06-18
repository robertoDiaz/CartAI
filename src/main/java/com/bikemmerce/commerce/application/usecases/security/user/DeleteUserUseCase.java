package com.bikemmerce.commerce.application.usecases.security.user;

import com.bikemmerce.commerce.application.annotations.UseCase;
import com.bikemmerce.commerce.domain.model.security.User;
import com.bikemmerce.commerce.domain.model.security.value.objects.UserId;
import com.bikemmerce.commerce.domain.ports.security.UserRepositoryPort;
import com.bikemmerce.commerce.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@UseCase
public class DeleteUserUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public Result<User> execute(UserId userId) {
        User user = userRepositoryPort.findByUserId(userId);

        if (user == null) {
            return Result.error(HttpStatus.NOT_FOUND.value());
        }

        userRepositoryPort.delete(userId);

        return Result.success(user);
    }
}
