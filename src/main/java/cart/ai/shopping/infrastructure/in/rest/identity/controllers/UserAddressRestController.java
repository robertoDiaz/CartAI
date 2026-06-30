/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.identity.controllers;

import cart.ai.shopping.application.usecases.identity.address.AddAddressUseCase;
import cart.ai.shopping.application.usecases.identity.address.DeleteAddressUseCase;
import cart.ai.shopping.application.usecases.identity.address.GetUserAddressesUseCase;
import cart.ai.shopping.application.usecases.identity.address.UpdateAddressUseCase;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.common.result.ResultError;
import cart.ai.shopping.domain.model.identity.Address;
import cart.ai.shopping.infrastructure.in.rest.identity.dtos.address.AddressRestRequest;
import cart.ai.shopping.infrastructure.in.rest.identity.dtos.address.AddressRestResponse;
import cart.ai.shopping.infrastructure.in.rest.identity.mappers.address.AddressRestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Roberto Díaz
 */
@RestController
@RequestMapping("/api/users/{userId}/addresses")
@RequiredArgsConstructor
public class UserAddressRestController {

    private final AddAddressUseCase addAddressUseCase;
    private final UpdateAddressUseCase updateAddressUseCase;
    private final DeleteAddressUseCase deleteAddressUseCase;
    private final GetUserAddressesUseCase getUserAddressesUseCase;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or principal.id == #userId")
    public ResponseEntity<List<AddressRestResponse>> getAddresses(@PathVariable String userId) {
        List<Address> addresses = getUserAddressesUseCase.execute(userId);
        return ResponseEntity.ok(addresses.stream()
                .map(AddressRestMapper::toResponse)
                .collect(Collectors.toList()));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or principal.id == #userId")
    public ResponseEntity<AddressRestResponse> addAddress(@PathVariable String userId, @Valid @RequestBody AddressRestRequest request) {
        Result<Address> result = addAddressUseCase.execute(AddressRestMapper.toAddCommand(userId, "USER", request));
        if (!result.hasError()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(AddressRestMapper.toResponse(result.getValue()));
        }
        return mapError(result.getError());
    }

    @PutMapping("/{addressId}")
    @PreAuthorize("hasRole('ADMIN') or principal.id == #userId")
    public ResponseEntity<AddressRestResponse> updateAddress(@PathVariable String userId, @PathVariable String addressId, @Valid @RequestBody AddressRestRequest request) {
        Result<Address> result = updateAddressUseCase.execute(AddressRestMapper.toUpdateCommand(addressId, userId, "USER", request));
        if (!result.hasError()) {
            return ResponseEntity.ok(AddressRestMapper.toResponse(result.getValue()));
        }
        return mapError(result.getError());
    }

    @DeleteMapping("/{addressId}")
    @PreAuthorize("hasRole('ADMIN') or principal.id == #userId")
    public ResponseEntity<Void> deleteAddress(@PathVariable String userId, @PathVariable String addressId) {
        Result<Void> result = deleteAddressUseCase.execute(addressId, userId, "USER");
        if (!result.hasError()) {
            return ResponseEntity.noContent().build();
        }
        return mapError(result.getError());
    }

    private <T> ResponseEntity<T> mapError(ResultError error) {
        return switch (error) {
            case NOT_FOUND -> ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            case UNAUTHORIZED, FORBIDDEN -> ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            case CONFLICT -> ResponseEntity.status(HttpStatus.CONFLICT).build();
            default -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        };
    }
}
