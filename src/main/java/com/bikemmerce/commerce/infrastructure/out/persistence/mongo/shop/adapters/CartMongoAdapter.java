package com.bikemmerce.commerce.infrastructure.out.persistence.mongo.shop.adapters;

import com.bikemmerce.commerce.domain.model.security.value.objects.UserId;
import com.bikemmerce.commerce.domain.model.shop.Cart;
import com.bikemmerce.commerce.domain.ports.shop.CartRepositoryPort;
import com.bikemmerce.commerce.infrastructure.out.persistence.mongo.shop.CartMongoRepository;
import com.bikemmerce.commerce.infrastructure.out.persistence.mongo.shop.mapper.CartMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

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
