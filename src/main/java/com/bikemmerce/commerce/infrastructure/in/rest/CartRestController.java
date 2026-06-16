package com.bikemmerce.commerce.infrastructure.in.rest;

import com.bikemmerce.commerce.application.usecases.cart.AddShoppingItemToCartUseCase;
import com.bikemmerce.commerce.application.usecases.cart.ClearCartUseCase;
import com.bikemmerce.commerce.application.usecases.cart.GetCartUseCase;
import com.bikemmerce.commerce.application.usecases.cart.RemoveShoppingItemFromCartUseCase;
import com.bikemmerce.commerce.domain.model.Cart;
import com.bikemmerce.commerce.domain.model.value.objects.CustomerId;
import com.bikemmerce.commerce.domain.model.value.objects.ProductId;
import com.bikemmerce.commerce.domain.result.Result;
import com.bikemmerce.commerce.infrastructure.in.rest.dto.cart.AddShoppingItemToCartRestRequest;
import com.bikemmerce.commerce.infrastructure.in.rest.dto.cart.RemoveShoppingItemToCartRestRequest;
import com.bikemmerce.commerce.infrastructure.in.rest.mapper.CartRestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartRestController {

    private final AddShoppingItemToCartUseCase addShoppingItemToCartUseCase;
    private final ClearCartUseCase clearCartUseCase;
    private final GetCartUseCase getCartUseCase;
    private final RemoveShoppingItemFromCartUseCase removeShoppingItemFromCartUseCase;

    @PatchMapping("/add")
    public ResponseEntity<?> addShoppingItemToCart(@RequestBody @Valid AddShoppingItemToCartRestRequest request) {
        Result<Cart> result = addShoppingItemToCartUseCase.execute(
                new CustomerId(request.customerId()), new ProductId(request.productId()));

        if (result.hasError()) {
            return ResponseEntity.status(result.getErrorCode()).body("Error.");
        }

        return ResponseEntity.ok(CartRestMapper.toResponse(result.getValue()));
    }

    @PatchMapping("/remove")
    public ResponseEntity<?> removeItemFromCart(@RequestBody @Valid RemoveShoppingItemToCartRestRequest request) {
        Result<Cart> result = removeShoppingItemFromCartUseCase.execute(
                new CustomerId(request.customerId()), new ProductId(request.productId()));

        if (result.hasError()) {
            return ResponseEntity.status(result.getErrorCode()).body("Error.");
        }

        return ResponseEntity.ok(CartRestMapper.toResponse(result.getValue()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCartById(@PathVariable String id) {
        Result<Cart> result = getCartUseCase.execute(new CustomerId(id));

        if (result.hasError()) {
            return ResponseEntity.status(result.getErrorCode()).body("Not found.");
        }

        return ResponseEntity.ok(CartRestMapper.toResponse(result.getValue()));
    }

    @PatchMapping("/clear/{id}")
    public ResponseEntity<?> clearCart(@PathVariable String id) {
        Result<Cart> result = clearCartUseCase.execute(new CustomerId(id));

        if (result.hasError()) {
            return ResponseEntity.status(result.getErrorCode()).body("Not found.");
        }

        return ResponseEntity.ok(CartRestMapper.toResponse(result.getValue()));
    }


}
