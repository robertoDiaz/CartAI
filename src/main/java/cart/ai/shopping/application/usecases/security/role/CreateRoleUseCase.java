package cart.ai.shopping.application.usecases.security.role;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.application.usecases.security.commands.CreateRoleCommand;
import cart.ai.shopping.domain.model.security.Role;
import cart.ai.shopping.domain.model.security.value.objects.Permission;
import cart.ai.shopping.domain.model.security.value.objects.RoleId;
import cart.ai.shopping.domain.ports.common.IncrementIdGeneratorPort;
import cart.ai.shopping.domain.ports.security.repositories.RoleRepositoryPort;
import cart.ai.shopping.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@UseCase
public class CreateRoleUseCase {

    private final RoleRepositoryPort roleRepositoryPort;
    private final IncrementIdGeneratorPort incrementIdGeneratorPort;

    public Result<Role> execute(CreateRoleCommand command) {
        RoleId roleId = new RoleId(incrementIdGeneratorPort.generate(Role.class));

        if (roleRepositoryPort.findByRoleId(roleId) != null) {
            return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        Role role = new Role(
                roleId,
                command.name(),
                command.permissions().stream()
                        .map(Permission::new)
                        .collect(Collectors.toSet())
        );

        return Result.success(roleRepositoryPort.save(role));
    }
}
