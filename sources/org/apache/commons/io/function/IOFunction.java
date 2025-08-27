package org.apache.commons.io.function;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/function/IOFunction.class */
public interface IOFunction<T, R> {
    R apply(T t) throws IOException;

    static <T> IOFunction<T, T> identity() {
        return Constants.IO_FUNCTION_ID;
    }

    default IOConsumer<T> andThen(Consumer<? super R> after) {
        Objects.requireNonNull(after, "after");
        return obj -> {
            after.accept(apply(obj));
        };
    }

    default <V> IOFunction<T, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after, "after");
        return obj -> {
            return after.apply(apply(obj));
        };
    }

    default IOConsumer<T> andThen(IOConsumer<? super R> after) {
        Objects.requireNonNull(after, "after");
        return obj -> {
            after.accept(apply(obj));
        };
    }

    default <V> IOFunction<T, V> andThen(IOFunction<? super R, ? extends V> after) {
        Objects.requireNonNull(after, "after");
        return obj -> {
            return after.apply(apply(obj));
        };
    }

    default Function<T, R> asFunction() {
        return t -> {
            return Uncheck.apply(this, t);
        };
    }

    default <V> IOFunction<V, R> compose(Function<? super V, ? extends T> before) {
        Objects.requireNonNull(before, "before");
        return v -> {
            return apply(before.apply(v));
        };
    }

    default <V> IOFunction<V, R> compose(IOFunction<? super V, ? extends T> before) {
        Objects.requireNonNull(before, "before");
        return v -> {
            return apply(before.apply(v));
        };
    }

    default IOSupplier<R> compose(IOSupplier<? extends T> before) {
        Objects.requireNonNull(before, "before");
        return () -> {
            return apply(before.get());
        };
    }

    default IOSupplier<R> compose(Supplier<? extends T> before) {
        Objects.requireNonNull(before, "before");
        return () -> {
            return apply(before.get());
        };
    }
}
