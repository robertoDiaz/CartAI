/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.shop.product;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.application.usecases.shop.commands.UpdateProductCommand;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.model.shop.Product;
import cart.ai.shopping.domain.model.shop.vos.ProductId;
import cart.ai.shopping.domain.ports.shop.ProductRepositoryPort;
import cart.ai.shopping.domain.ports.storage.StoragePort;
import cart.ai.shopping.domain.ports.storage.TempStoragePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

import static cart.ai.shopping.domain.common.result.ResultError.INTERNAL_ERROR;
import static cart.ai.shopping.domain.common.result.ResultError.NOT_FOUND;

/**
 * @author Roberto Díaz
 */
@RequiredArgsConstructor
@UseCase
@Slf4j
public class UpdateProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private final StoragePort storagePort;
    private final TempStoragePort tempStoragePort;

    public Result<Product> execute(UpdateProductCommand command) {
        ProductId productId = new ProductId(command.id());
        Product existingProduct = productRepositoryPort.find(productId);
        
        if (existingProduct == null) {
            return Result.error(NOT_FOUND);
        }

        List<String> newFileIds = command.imageFileIds() != null ? command.imageFileIds() : Collections.emptyList();
        List<String> oldFileIds = existingProduct.getImageFileIds() != null ? existingProduct.getImageFileIds() : Collections.emptyList();

        List<String> toPromote = newFileIds.stream().filter(id -> !oldFileIds.contains(id)).toList();
        List<String> toDelete = oldFileIds.stream().filter(id -> !newFileIds.contains(id)).toList();

        for (String fileId : toPromote) {
            try {
                storagePort.promoteFile(fileId, tempStoragePort.getBucketName());
            } catch (Exception e) {
                log.warn("Could not promote temp product image {}: {}", fileId, e.getMessage());
            }
        }

        for (String fileId : toDelete) {
            try {
                storagePort.deleteFile(fileId);
            } catch (Exception e) {
                log.warn("Could not delete product image {}: {}", fileId, e.getMessage());
            }
        }

        Product product = new Product(productId, command.name(), command.description(), command.price(), command.stock(), command.imageFileIds());
        return Result.success(productRepositoryPort.save(product));
    }
}
