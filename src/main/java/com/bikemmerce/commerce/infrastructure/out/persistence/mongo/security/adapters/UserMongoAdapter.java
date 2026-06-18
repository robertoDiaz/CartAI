package com.bikemmerce.commerce.infrastructure.out.persistence.mongo.security.adapters;

import com.bikemmerce.commerce.domain.model.security.Role;
import com.bikemmerce.commerce.domain.model.security.User;
import com.bikemmerce.commerce.domain.model.security.value.objects.Email;
import com.bikemmerce.commerce.domain.model.security.value.objects.RoleId;
import com.bikemmerce.commerce.domain.model.security.value.objects.UserId;
import com.bikemmerce.commerce.domain.ports.security.UserRepositoryPort;
import com.bikemmerce.commerce.infrastructure.out.persistence.mongo.security.UserMongoRepository;
import com.bikemmerce.commerce.infrastructure.out.persistence.mongo.security.documents.UserDocument;
import com.bikemmerce.commerce.infrastructure.out.persistence.mongo.security.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
