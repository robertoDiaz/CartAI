package cart.ai.shopping.application.usecases.security.user;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.application.usecases.security.commands.AuthenticateUserCommand;
import cart.ai.shopping.domain.model.security.User;
import cart.ai.shopping.domain.model.security.value.objects.Email;
import cart.ai.shopping.domain.ports.security.repositories.UserRepositoryPort;
import cart.ai.shopping.domain.ports.security.services.PasswordEncoderPort;
import cart.ai.shopping.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@UseCase
public class AuthenticateUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;

    public Result<User> execute(AuthenticateUserCommand command) {
        User user = userRepositoryPort.findByEmail(new Email(command.email()));

        if (user == null || !passwordEncoderPort.matches(command.password(), user.passwordHash())) {
            return Result.error(HttpStatus.UNAUTHORIZED.value());
        }

        return Result.success(user);
    }
}
