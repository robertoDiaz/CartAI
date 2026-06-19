/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.out.persistence.mongo.shop.adapters;

import cart.ai.shopping.domain.model.identity.vos.UserId;
import cart.ai.shopping.domain.model.shop.Cart;
import cart.ai.shopping.domain.ports.shop.CartRepositoryPort;
import cart.ai.shopping.infrastructure.out.persistence.mongo.shop.mappers.CartMapper;
import cart.ai.shopping.infrastructure.out.persistence.mongo.shop.repositories.CartMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Roberto Díaz
 */
@Component
@RequiredArgsConstructor
public class CartMongoAdapter implements CartRepositoryPort {

    private final CartMongoRepository cartMongoRepository;

    @Override
    public void delete(UserId userId) {
        cartMongoRepository.deleteById(userId.value());
    }

    @Override
    public Cart find(UserId userId) {
        return cartMongoRepository.findById(userId.value()).map(CartMapper::toDomain).orElse(null);
    }

    @Override
    public List<Cart> findAll() {
        return cartMongoRepository.findAll().stream().map(CartMapper::toDomain).toList();
    }

    @Override
    public Cart save(Cart cart) {
        return CartMapper.toDomain(cartMongoRepository.save(CartMapper.toDocument(cart)));
    }
}
