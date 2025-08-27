package org.apache.commons.io.function;

import java.io.IOException;
import java.util.Objects;

@FunctionalInterface
/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/function/IOTriConsumer.class */
public interface IOTriConsumer<T, U, V> {
    void accept(T t, U u, V v) throws IOException;

    static <T, U, V> IOTriConsumer<T, U, V> noop() {
        return Constants.IO_TRI_CONSUMER;
    }

    default IOTriConsumer<T, U, V> andThen(IOTriConsumer<? super T, ? super U, ? super V> after) {
        Objects.requireNonNull(after);
        return (obj, obj2, obj3) -> {
            accept(obj, obj2, obj3);
            after.accept(obj, obj2, obj3);
        };
    }
}
