package org.apache.commons.io.function;

import java.io.IOException;
import java.util.Objects;
import java.util.function.BiFunction;

@FunctionalInterface
/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/function/IOBiFunction.class */
public interface IOBiFunction<T, U, R> {
    R apply(T t, U u) throws IOException;

    default <V> IOBiFunction<T, U, V> andThen(IOFunction<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (obj, obj2) -> {
            return after.apply(apply(obj, obj2));
        };
    }

    default BiFunction<T, U, R> asBiFunction() {
        return (t, u) -> {
            return Uncheck.apply(this, t, u);
        };
    }
}
