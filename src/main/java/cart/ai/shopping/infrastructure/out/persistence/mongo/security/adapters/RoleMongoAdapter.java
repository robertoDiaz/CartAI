package cart.ai.shopping.infrastructure.out.persistence.mongo.security.adapters;

import cart.ai.shopping.domain.model.security.Role;
import cart.ai.shopping.domain.model.security.value.objects.RoleId;
import cart.ai.shopping.domain.model.security.value.objects.UserId;
import cart.ai.shopping.domain.ports.security.repositories.RoleRepositoryPort;
import cart.ai.shopping.infrastructure.out.persistence.mongo.security.RoleMongoRepository;
import cart.ai.shopping.infrastructure.out.persistence.mongo.security.mapper.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

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
