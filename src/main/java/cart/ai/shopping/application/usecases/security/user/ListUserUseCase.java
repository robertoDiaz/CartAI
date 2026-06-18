package cart.ai.shopping.application.usecases.security.user;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.domain.model.security.User;
import cart.ai.shopping.domain.ports.security.repositories.UserRepositoryPort;
import cart.ai.shopping.domain.result.Result;
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
