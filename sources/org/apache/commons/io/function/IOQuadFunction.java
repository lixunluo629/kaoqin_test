package org.apache.commons.io.function;

import java.io.IOException;
import java.util.Objects;

@FunctionalInterface
/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/function/IOQuadFunction.class */
public interface IOQuadFunction<T, U, V, W, R> {
    R apply(T t, U u, V v, W w) throws IOException;

    default <X> IOQuadFunction<T, U, V, W, X> andThen(IOFunction<? super R, ? extends X> after) {
        Objects.requireNonNull(after);
        return (obj, obj2, obj3, obj4) -> {
            return after.apply(apply(obj, obj2, obj3, obj4));
        };
    }
}
