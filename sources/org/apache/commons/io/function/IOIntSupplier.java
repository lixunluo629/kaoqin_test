package org.apache.commons.io.function;

import java.io.IOException;
import java.util.function.IntSupplier;

@FunctionalInterface
/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/function/IOIntSupplier.class */
public interface IOIntSupplier {
    int getAsInt() throws IOException;

    default IntSupplier asIntSupplier() {
        return () -> {
            return Uncheck.getAsInt(this);
        };
    }
}
