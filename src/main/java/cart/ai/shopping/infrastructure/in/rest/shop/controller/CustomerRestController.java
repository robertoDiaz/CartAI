/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.shop.controller;

import cart.ai.shopping.application.usecases.shop.customer.CreateCustomerUseCase;
import cart.ai.shopping.application.usecases.shop.customer.GetCustomerByEmailUseCase;
import cart.ai.shopping.application.usecases.shop.customer.GetCustomerUseCase;
import cart.ai.shopping.application.usecases.shop.customer.UpdateCustomerUseCase;
import cart.ai.shopping.domain.model.identity.value.objects.Email;
import cart.ai.shopping.domain.model.identity.value.objects.UserId;
import cart.ai.shopping.domain.model.shop.Customer;
import cart.ai.shopping.domain.result.Result;
import cart.ai.shopping.infrastructure.in.rest.shop.dto.CreateCustomerRestRequest;
import cart.ai.shopping.infrastructure.in.rest.shop.dto.UpdateCustomerRestRequest;
import cart.ai.shopping.infrastructure.in.rest.shop.mapper.CustomerRestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Roberto Díaz
 */
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerRestController {

    private final CreateCustomerUseCase createCustomerUseCase;
    private final GetCustomerUseCase getCustomerUseCase;
    private final GetCustomerByEmailUseCase getCustomerByEmailUseCase;
    private final UpdateCustomerUseCase updateCustomerUseCase;

    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody @Valid CreateCustomerRestRequest request) {
        Result<Customer> result = createCustomerUseCase.execute(CustomerRestMapper.toCreateCustomerCommand(request));

        if (result.hasError()) {
            return ResponseEntity.status(result.getErrorCode()).body("Error.");
        }

        return ResponseEntity.ok(CustomerRestMapper.toResponse(result.getValue()));
    }

    @PutMapping
    public ResponseEntity<?> updateCustomer(@RequestBody @Valid UpdateCustomerRestRequest request) {
        Result<Customer> result = updateCustomerUseCase.execute(CustomerRestMapper.toUpdateCustomerCommand(request));

        if (result.hasError()) {
            return ResponseEntity.status(result.getErrorCode()).body("Error.");
        }

        return ResponseEntity.ok(CustomerRestMapper.toResponse(result.getValue()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable String id) {
        Result<Customer> result = getCustomerUseCase.execute(new UserId(id));

        if (result.hasError()) {
            return ResponseEntity.status(result.getErrorCode()).body("Not found.");
        }

        return ResponseEntity.ok(CustomerRestMapper.toResponse(result.getValue()));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getCustomerByEmail(@PathVariable String email) {
        Result<Customer> result = getCustomerByEmailUseCase.execute(new Email(email));

        if (result.hasError()) {
            return ResponseEntity.status(result.getErrorCode()).body("Not found.");
        }

        return ResponseEntity.ok(CustomerRestMapper.toResponse(result.getValue()));
    }

}
