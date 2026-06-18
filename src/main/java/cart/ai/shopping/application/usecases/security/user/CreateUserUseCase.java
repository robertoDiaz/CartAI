package cart.ai.shopping.application.usecases.security.user;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.application.usecases.security.commands.CreateUserCommand;
import cart.ai.shopping.domain.model.security.User;
import cart.ai.shopping.domain.model.security.value.objects.Email;
import cart.ai.shopping.domain.model.security.value.objects.UserAddedEvent;
import cart.ai.shopping.domain.model.security.value.objects.UserId;
import cart.ai.shopping.domain.ports.common.IncrementIdGeneratorPort;
import cart.ai.shopping.domain.ports.security.events.UserAddedEventPublisherPort;
import cart.ai.shopping.domain.ports.security.repositories.UserRepositoryPort;
import cart.ai.shopping.domain.ports.security.services.PasswordEncoderPort;
import cart.ai.shopping.domain.result.Result;
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
