/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.out.persistence.mongo.identity.adapters;

import cart.ai.shopping.domain.model.identity.Role;
import cart.ai.shopping.domain.model.identity.User;
import cart.ai.shopping.domain.model.identity.vos.Email;
import cart.ai.shopping.domain.model.identity.vos.RoleId;
import cart.ai.shopping.domain.model.identity.vos.UserId;
import cart.ai.shopping.domain.ports.identity.UserRepositoryPort;
import cart.ai.shopping.infrastructure.out.persistence.mongo.identity.documents.UserDocument;
import cart.ai.shopping.infrastructure.out.persistence.mongo.identity.mappers.UserMapper;
import cart.ai.shopping.infrastructure.out.persistence.mongo.identity.repositories.UserMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Roberto Díaz
 */
@Component
@RequiredArgsConstructor
public class UserMongoAdapter implements UserRepositoryPort {

    private final UserMongoRepository userMongoRepository;
    private final RoleMongoAdapter roleMongoAdapter;

    @Override
    public void delete(UserId userId) {
        userMongoRepository.deleteById(userId.value());
    }

    @Override
    public User findByUserId(UserId userId) {
        return userMongoRepository.findById(userId.value())
                .map(this::toDomain)
                .orElse(null);
    }

    @Override
    public User findByEmail(Email email) {
        return userMongoRepository.findByEmail(email.value())
                .map(this::toDomain)
                .orElse(null);
    }

    @Override
    public List<User> findAll() {
        return userMongoRepository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public User save(User user) {
        return toDomain(userMongoRepository.save(UserMapper.toDocument(user)));
    }

    private User toDomain(UserDocument doc) {
        Set<Role> roles = doc.getRoleIds().stream()
                .map(roleId -> roleMongoAdapter.findByRoleId(new RoleId(roleId)))
                .collect(Collectors.toSet());

        return UserMapper.toDomain(doc, roles);
    }

}
