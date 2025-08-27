package org.apache.commons.io.function;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Supplier;

@FunctionalInterface
/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/function/IOSupplier.class */
public interface IOSupplier<T> {
    T get() throws IOException;

    default Supplier<T> asSupplier() {
        return this::getUnchecked;
    }

    default T getUnchecked() throws UncheckedIOException {
        return (T) Uncheck.get(this);
    }
}
