package cart.ai.shopping.infrastructure.out.persistence.mongo.security.mapper;

import cart.ai.shopping.domain.model.security.Role;
import cart.ai.shopping.domain.model.security.User;
import cart.ai.shopping.domain.model.security.value.objects.Email;
import cart.ai.shopping.domain.model.security.value.objects.UserId;
import cart.ai.shopping.infrastructure.out.persistence.mongo.security.documents.UserDocument;

import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserDocument toDocument(User user) {
        return UserDocument.builder()
                .id(user.userId().value())
                .name(user.name())
                .email(user.email().value())
                .passwordHash(user.passwordHash())
                .roleIds(user.roles().stream()
                        .map(role -> role.id().value())
                        .collect(Collectors.toSet()))
                .build();
    }

    public static User toDomain(UserDocument document, Set<Role> roles) {
        return new User(
                new UserId(document.getId()),
                document.getName(),
                new Email(document.getEmail()),
                document.getPasswordHash(),
                roles
        );
    }
}
