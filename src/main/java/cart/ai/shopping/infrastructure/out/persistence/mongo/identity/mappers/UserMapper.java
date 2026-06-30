/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.out.persistence.mongo.identity.mappers;

import cart.ai.shopping.domain.model.identity.Role;
import cart.ai.shopping.domain.model.identity.User;
import cart.ai.shopping.domain.model.identity.vos.Email;
import cart.ai.shopping.domain.model.identity.vos.UserId;
import cart.ai.shopping.infrastructure.out.persistence.mongo.identity.documents.UserDocument;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Roberto Díaz
 */
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
                .avatarFileId(user.avatarFileId())
                .phone(user.phone())
                .taxId(user.taxId())
                .preferredLanguage(user.preferredLanguage())
                .build();
    }

    public static User toDomain(UserDocument document, Set<Role> roles) {
        return new User(
                new UserId(document.getId()),
                document.getName(),
                new Email(document.getEmail()),
                document.getPasswordHash(),
                roles,
                document.getAvatarFileId(),
                document.getPhone(),
                document.getTaxId(),
                document.getPreferredLanguage()
        );
    }
}
