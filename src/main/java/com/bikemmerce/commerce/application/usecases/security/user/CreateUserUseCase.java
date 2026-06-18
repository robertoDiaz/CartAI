package com.bikemmerce.commerce.application.usecases.security.user;

import com.bikemmerce.commerce.application.annotations.UseCase;
import com.bikemmerce.commerce.application.usecases.security.commands.CreateUserCommand;
import com.bikemmerce.commerce.domain.model.security.User;
import com.bikemmerce.commerce.domain.model.security.value.objects.Email;
import com.bikemmerce.commerce.domain.model.security.value.objects.UserAddedEvent;
import com.bikemmerce.commerce.domain.model.security.value.objects.UserId;
import com.bikemmerce.commerce.domain.ports.common.IncrementIdGeneratorPort;
import com.bikemmerce.commerce.domain.ports.security.PasswordEncoderPort;
import com.bikemmerce.commerce.domain.ports.security.UserRepositoryPort;
import com.bikemmerce.commerce.domain.ports.security.events.UserAddedEventPublisherPort;
import com.bikemmerce.commerce.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@UseCase
public class CreateUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final UserAddedEventPublisherPort userAddedEventPublisherPort;
    private final IncrementIdGeneratorPort incrementIdGeneratorPort;
    private final PasswordEncoderPort passwordEncoderPort;

    public Result<User> execute(CreateUserCommand command) {
        Email email = new Email(command.email());

        if (userRepositoryPort.findByEmail(email) != null) {
            return Result.error(HttpStatus.CONFLICT.value());
        }

        UserId userId = new UserId(incrementIdGeneratorPort.generate(User.class));
        String passwordHash = passwordEncoderPort.encode(command.password());

        User user = userRepositoryPort.save(new User(
                userId, 
                command.name(), 
                email, 
                passwordHash, 
                command.roles()
        ));

        userAddedEventPublisherPort.added(new UserAddedEvent(
                user.userId(),
                user.name(),
                user.email(),
                user.roles()
        ));

        return Result.success(user);
    }
}
