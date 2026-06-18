package cart.ai.shopping.infrastructure.out.persistence.mongo.security.mapper;

import cart.ai.shopping.domain.model.security.Role;
import cart.ai.shopping.domain.model.security.value.objects.Permission;
import cart.ai.shopping.domain.model.security.value.objects.RoleId;
import cart.ai.shopping.infrastructure.out.persistence.mongo.security.documents.RoleDocument;

import java.util.stream.Collectors;

public class RoleMapper {

    public static RoleDocument toDocument(Role role) {
        return RoleDocument.builder()
                .id(role.id().value())
                .name(role.name())
                .permissions(role.permissions().stream()
                        .map(Permission::value)
                        .collect(Collectors.toSet()))
                .build();
    }

    public static Role toDomain(RoleDocument document) {
        return new Role(
                new RoleId(document.getId()),
                document.getName(),
                document.getPermissions().stream()
                        .map(Permission::new)
                        .collect(Collectors.toSet())
        );
    }
}
