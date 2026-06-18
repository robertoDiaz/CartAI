package com.bikemmerce.commerce.application.usecases.security.user;

import com.bikemmerce.commerce.application.annotations.UseCase;
import com.bikemmerce.commerce.domain.model.security.User;
import com.bikemmerce.commerce.domain.model.security.value.objects.Email;
import com.bikemmerce.commerce.domain.ports.security.UserRepositoryPort;
import com.bikemmerce.commerce.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@UseCase
public class GetUserByEmailUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public Result<User> execute(Email email) {
        User user = userRepositoryPort.findByEmail(email);

        if (user == null) {
            return Result.error(HttpStatus.NOT_FOUND.value());
        }

        return Result.success(user);
    }
}
