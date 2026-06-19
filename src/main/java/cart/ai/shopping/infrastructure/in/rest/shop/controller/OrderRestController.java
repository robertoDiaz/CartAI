/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.shop.controller;

import cart.ai.shopping.application.usecases.shop.order.CancelOrderUseCase;
import cart.ai.shopping.application.usecases.shop.order.CreateOrderUseCase;
import cart.ai.shopping.application.usecases.shop.order.GetOrderUseCase;
import cart.ai.shopping.domain.model.identity.value.objects.UserId;
import cart.ai.shopping.domain.model.shop.Order;
import cart.ai.shopping.domain.model.shop.value.objects.OrderId;
import cart.ai.shopping.domain.result.Result;
import cart.ai.shopping.infrastructure.in.rest.shop.mapper.OrderRestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Roberto Díaz
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderRestController {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;


    @PostMapping("/{id}")
    @PreAuthorize("hasAuthority('WRITE_ORDERS')")
    public ResponseEntity<?> createOrder(@PathVariable String id) {
        Result<Order> result = createOrderUseCase.execute(new UserId(id));

        if (result.hasError()) {
            return ResponseEntity.status(result.getErrorCode()).body("Error.");
        }

        return ResponseEntity.ok(OrderRestMapper.toResponse(result.getValue()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ_ORDERS')")
    public ResponseEntity<?> getOrderById(@PathVariable String id) {
        Result<Order> result = getOrderUseCase.execute(new OrderId(id));

        if (result.hasError()) {
            return ResponseEntity.status(result.getErrorCode()).body("Not found.");
        }

        return ResponseEntity.ok(OrderRestMapper.toResponse(result.getValue()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('WRITE_ORDERS')")
    public ResponseEntity<?> cancelOrder(@PathVariable String id) {
        Result<Order> result = cancelOrderUseCase.execute(new OrderId(id));

        if (result.hasError()) {
            return ResponseEntity.status(result.getErrorCode()).body("Not found.");
        }

        return ResponseEntity.ok(OrderRestMapper.toResponse(result.getValue()));
    }
}
