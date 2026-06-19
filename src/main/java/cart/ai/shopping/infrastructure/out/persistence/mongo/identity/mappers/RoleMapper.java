/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.out.persistence.mongo.identity.mappers;

import cart.ai.shopping.domain.model.identity.Role;
import cart.ai.shopping.domain.model.identity.vos.Permission;
import cart.ai.shopping.domain.model.identity.vos.RoleId;
import cart.ai.shopping.infrastructure.out.persistence.mongo.identity.documents.RoleDocument;

import java.util.stream.Collectors;

/**
 * @author Roberto Díaz
 */
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
