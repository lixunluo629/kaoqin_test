package org.apache.commons.io.function;

import java.io.IOException;
import java.util.Objects;
import java.util.function.BiConsumer;

@FunctionalInterface
/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/function/IOBiConsumer.class */
public interface IOBiConsumer<T, U> {
    void accept(T t, U u) throws IOException;

    static <T, U> IOBiConsumer<T, U> noop() {
        return Constants.IO_BI_CONSUMER;
    }

    default IOBiConsumer<T, U> andThen(IOBiConsumer<? super T, ? super U> after) {
        Objects.requireNonNull(after);
        return (obj, obj2) -> {
            accept(obj, obj2);
            after.accept(obj, obj2);
        };
    }

    default BiConsumer<T, U> asBiConsumer() {
        return (t, u) -> {
            Uncheck.accept(this, t, u);
        };
    }
}
