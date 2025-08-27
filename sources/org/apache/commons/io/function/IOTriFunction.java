package org.apache.commons.io.function;

import java.io.IOException;
import java.util.Objects;

@FunctionalInterface
/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/function/IOTriFunction.class */
public interface IOTriFunction<T, U, V, R> {
    R apply(T t, U u, V v) throws IOException;

    default <W> IOTriFunction<T, U, V, W> andThen(IOFunction<? super R, ? extends W> after) {
        Objects.requireNonNull(after);
        return (obj, obj2, obj3) -> {
            return after.apply(apply(obj, obj2, obj3));
        };
    }
}
