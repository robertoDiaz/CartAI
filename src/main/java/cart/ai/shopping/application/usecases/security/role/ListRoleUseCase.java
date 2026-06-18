package cart.ai.shopping.application.usecases.security.role;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.domain.model.security.Role;
import cart.ai.shopping.domain.ports.security.repositories.RoleRepositoryPort;
import cart.ai.shopping.domain.result.Result;
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
