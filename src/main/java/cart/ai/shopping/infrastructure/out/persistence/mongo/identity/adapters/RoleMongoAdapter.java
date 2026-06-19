/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.out.persistence.mongo.identity.adapters;

import cart.ai.shopping.domain.model.identity.Role;
import cart.ai.shopping.domain.model.identity.vos.RoleId;
import cart.ai.shopping.domain.model.identity.vos.UserId;
import cart.ai.shopping.domain.ports.identity.RoleRepositoryPort;
import cart.ai.shopping.infrastructure.out.persistence.mongo.identity.mappers.RoleMapper;
import cart.ai.shopping.infrastructure.out.persistence.mongo.identity.repositories.RoleMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Roberto Díaz
 */
@Component
@RequiredArgsConstructor
public class RoleMongoAdapter implements RoleRepositoryPort {

    private final RoleMongoRepository roleMongoRepository;

    @Override
    public void delete(RoleId roleId) {
        roleMongoRepository.deleteById(roleId.value());
    }

    @Override
    public Role findByRoleId(RoleId roleId) {
        return roleMongoRepository.findById(roleId.value())
                .map(RoleMapper::toDomain)
                .orElse(null);
    }

    @Override
    public Role findByName(String name) {
        return roleMongoRepository.findByName(name)
                .map(RoleMapper::toDomain)
                .orElse(null);
    }

    @Override
    public List<Role> findUserRoles(UserId userId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Role> findAll() {
        return roleMongoRepository.findAll().stream()
                .map(RoleMapper::toDomain)
                .toList();
    }

    @Override
    public Role save(Role role) {
        return RoleMapper.toDomain(roleMongoRepository.save(RoleMapper.toDocument(role)));
    }
}
