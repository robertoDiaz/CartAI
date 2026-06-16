package com.bikemmerce.commerce.infrastructure.in.rest;

import com.bikemmerce.commerce.application.usecases.order.CancelOrderUseCase;
import com.bikemmerce.commerce.application.usecases.order.CreateOrderUseCase;
import com.bikemmerce.commerce.application.usecases.order.GetOrderUseCase;
import com.bikemmerce.commerce.domain.model.Order;
import com.bikemmerce.commerce.domain.model.value.objects.CustomerId;
import com.bikemmerce.commerce.domain.model.value.objects.OrderId;
import com.bikemmerce.commerce.domain.result.Result;
import com.bikemmerce.commerce.infrastructure.in.rest.mapper.OrderRestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderRestController {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;


    @PostMapping("/{id}")
    public ResponseEntity<?> createOrder(@PathVariable String id) {
        Result<Order> result = createOrderUseCase.execute(new CustomerId(id));

        if (result.hasError()) {
            return ResponseEntity.status(result.getErrorCode()).body("Error.");
        }

        return ResponseEntity.ok(OrderRestMapper.toResponse(result.getValue()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable String id) {
        Result<Order> result = getOrderUseCase.execute(new OrderId(id));

        if (result.hasError()) {
            return ResponseEntity.status(result.getErrorCode()).body("Not found.");
        }

        return ResponseEntity.ok(OrderRestMapper.toResponse(result.getValue()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable String id) {
        Result<Order> result = cancelOrderUseCase.execute(new OrderId(id));

        if (result.hasError()) {
            return ResponseEntity.status(result.getErrorCode()).body("Not found.");
        }

        return ResponseEntity.ok(OrderRestMapper.toResponse(result.getValue()));
    }
}
