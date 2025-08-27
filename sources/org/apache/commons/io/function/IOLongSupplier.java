package org.apache.commons.io.function;

import java.io.IOException;
import java.util.function.LongSupplier;

@FunctionalInterface
/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/function/IOLongSupplier.class */
public interface IOLongSupplier {
    long getAsLong() throws IOException;

    default LongSupplier asSupplier() {
        return () -> {
            return Uncheck.getAsLong(this);
        };
    }
}
