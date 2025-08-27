package org.apache.commons.io.function;

import java.io.IOException;
import java.util.Comparator;

@FunctionalInterface
/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/function/IOComparator.class */
public interface IOComparator<T> {
    int compare(T t, T t2) throws IOException;

    default Comparator<T> asComparator() {
        return (t, u) -> {
            return Uncheck.compare(this, t, u);
        };
    }
}
