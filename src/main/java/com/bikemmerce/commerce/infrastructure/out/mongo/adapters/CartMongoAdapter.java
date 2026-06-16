package com.bikemmerce.commerce.infrastructure.out.mongo.adapters;

import com.bikemmerce.commerce.domain.model.Cart;
import com.bikemmerce.commerce.domain.model.value.objects.CustomerId;
import com.bikemmerce.commerce.domain.ports.CartRepositoryPort;
import com.bikemmerce.commerce.infrastructure.out.mongo.CartMongoRepository;
import com.bikemmerce.commerce.infrastructure.out.mongo.mapper.CartMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CartMongoAdapter implements CartRepositoryPort {

    private final CartMongoRepository cartMongoRepository;

    @Override
    public void delete(CustomerId customerId) {
        cartMongoRepository.deleteById(customerId.value());
    }

    @Override
    public Cart find(CustomerId customerId) {
        return cartMongoRepository.findById(customerId.value()).map(CartMapper::toDomain).orElse(null);
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
