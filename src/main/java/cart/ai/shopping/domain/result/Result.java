package cart.ai.shopping.domain.result;

import lombok.Getter;

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
