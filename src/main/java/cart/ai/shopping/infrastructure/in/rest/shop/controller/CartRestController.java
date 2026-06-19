/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.shop.controller;

import cart.ai.shopping.application.usecases.shop.cart.AddShoppingItemToCartUseCase;
import cart.ai.shopping.application.usecases.shop.cart.ClearCartUseCase;
import cart.ai.shopping.application.usecases.shop.cart.GetCartUseCase;
import cart.ai.shopping.application.usecases.shop.cart.RemoveShoppingItemFromCartUseCase;
import cart.ai.shopping.domain.model.identity.value.objects.UserId;
import cart.ai.shopping.domain.model.shop.Cart;
import cart.ai.shopping.domain.model.shop.value.objects.ProductId;
import cart.ai.shopping.domain.result.Result;
import cart.ai.shopping.infrastructure.in.rest.shop.dto.AddShoppingItemToCartRestRequest;
import cart.ai.shopping.infrastructure.in.rest.shop.dto.RemoveShoppingItemToCartRestRequest;
import cart.ai.shopping.infrastructure.in.rest.shop.mapper.CartRestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Roberto Díaz
 */
@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartRestController {

    private final AddShoppingItemToCartUseCase addShoppingItemToCartUseCase;
    private final ClearCartUseCase clearCartUseCase;
    private final GetCartUseCase getCartUseCase;
    private final RemoveShoppingItemFromCartUseCase removeShoppingItemFromCartUseCase;

    @PatchMapping("/add")
    @PreAuthorize("hasAuthority('WRITE_CARTS')")
    public ResponseEntity<?> addShoppingItemToCart(@RequestBody @Valid AddShoppingItemToCartRestRequest request) {
        Result<Cart> result = addShoppingItemToCartUseCase.execute(
                new UserId(request.customerId()), new ProductId(request.productId()));

        if (result.hasError()) {
            return ResponseEntity.status(result.getErrorCode()).body("Error.");
        }

        return ResponseEntity.ok(CartRestMapper.toResponse(result.getValue()));
    }

    @PatchMapping("/remove")
    @PreAuthorize("hasAuthority('WRITE_CARTS')")
    public ResponseEntity<?> removeItemFromCart(@RequestBody @Valid RemoveShoppingItemToCartRestRequest request) {
        Result<Cart> result = removeShoppingItemFromCartUseCase.execute(
                new UserId(request.customerId()), new ProductId(request.productId()));

        if (result.hasError()) {
            return ResponseEntity.status(result.getErrorCode()).body("Error.");
        }

        return ResponseEntity.ok(CartRestMapper.toResponse(result.getValue()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ_CARTS')")
    public ResponseEntity<?> getCartById(@PathVariable String id) {
        Result<Cart> result = getCartUseCase.execute(new UserId(id));

        if (result.hasError()) {
            return ResponseEntity.status(result.getErrorCode()).body("Not found.");
        }

        return ResponseEntity.ok(CartRestMapper.toResponse(result.getValue()));
    }

    @PatchMapping("/clear/{id}")
    @PreAuthorize("hasAuthority('WRITE_CARTS')")
    public ResponseEntity<?> clearCart(@PathVariable String id) {
        Result<Cart> result = clearCartUseCase.execute(new UserId(id));

        if (result.hasError()) {
            return ResponseEntity.status(result.getErrorCode()).body("Not found.");
        }

        return ResponseEntity.ok(CartRestMapper.toResponse(result.getValue()));
    }


}
