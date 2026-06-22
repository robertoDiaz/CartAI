/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.shop.order;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.model.shop.Order;
import cart.ai.shopping.domain.model.shop.vos.OrderId;
import cart.ai.shopping.domain.ports.shop.OrderRepositoryPort;
import lombok.RequiredArgsConstructor;

import static cart.ai.shopping.domain.common.result.ResultError.NOT_FOUND;

/**
 * @author Roberto Díaz
 */
@RequiredArgsConstructor
@UseCase
public class GetOrderUseCase {

    private final OrderRepositoryPort OrderRepositoryPort;

    public Result<Order> execute(OrderId orderId) {
        Order Order = OrderRepositoryPort.find(orderId);

        if (Order != null) {
            return Result.success(Order);
        }

        return Result.error(NOT_FOUND);
    }

}
