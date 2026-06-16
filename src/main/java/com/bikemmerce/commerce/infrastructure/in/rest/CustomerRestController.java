package com.bikemmerce.commerce.infrastructure.in.rest;

import com.bikemmerce.commerce.application.usecases.customer.CreateCustomerUseCase;
import com.bikemmerce.commerce.application.usecases.customer.GetCustomerUseCase;
import com.bikemmerce.commerce.application.usecases.customer.UpdateCustomerUseCase;
import com.bikemmerce.commerce.domain.model.Customer;
import com.bikemmerce.commerce.domain.result.Result;
import com.bikemmerce.commerce.infrastructure.in.rest.dto.customer.UpdateCustomerRestRequest;
import com.bikemmerce.commerce.infrastructure.in.rest.dto.product.CreateCustomerRestRequest;
import com.bikemmerce.commerce.infrastructure.in.rest.mapper.CustomerRestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerRestController {

    private final CreateCustomerUseCase createCustomerUseCase;
    private final GetCustomerUseCase getCustomerUseCase;
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
        Result<Customer> result = getCustomerUseCase.execute(id, null);

        if (result.hasError()) {
            return ResponseEntity.status(result.getErrorCode()).body("Not found.");
        }

        return ResponseEntity.ok(CustomerRestMapper.toResponse(result.getValue()));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getCustomerByEmail(@PathVariable String email) {
        Result<Customer> result = getCustomerUseCase.execute(null, email);

        if (result.hasError()) {
            return ResponseEntity.status(result.getErrorCode()).body("Not found.");
        }

        return ResponseEntity.ok(CustomerRestMapper.toResponse(result.getValue()));
    }

}
