/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.domain.common.result;

import lombok.Getter;

/**
 * @author Roberto Díaz
 */
public class Result<T> {

    @Getter
    private final Integer errorCode;
    private final T value;

    private Result(T value, Integer errorCode) {
        this.value = value;
        this.errorCode = errorCode;
    }

    public static <T> Result<T> success(T value) {
        return new Result<>(value, null);
    }

    public static <T> Result<T> error(Integer errorCode) {
        return new Result<>(null, errorCode);
    }

    public boolean hasError() {
        return (errorCode != null);
    }

    public T getValue() {
        if (hasError()) throw new RuntimeException("Error present");
        return value;
    }
}
