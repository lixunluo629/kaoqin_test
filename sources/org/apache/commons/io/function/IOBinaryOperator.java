package org.apache.commons.io.function;

import java.util.Objects;
import java.util.function.BinaryOperator;

@FunctionalInterface
/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/function/IOBinaryOperator.class */
public interface IOBinaryOperator<T> extends IOBiFunction<T, T, T> {
    static <T> IOBinaryOperator<T> maxBy(IOComparator<? super T> comparator) {
        Objects.requireNonNull(comparator);
        return (a, b) -> {
            return comparator.compare(a, b) >= 0 ? a : b;
        };
    }

    static <T> IOBinaryOperator<T> minBy(IOComparator<? super T> comparator) {
        Objects.requireNonNull(comparator);
        return (a, b) -> {
            return comparator.compare(a, b) <= 0 ? a : b;
        };
    }

    default BinaryOperator<T> asBinaryOperator() {
        return (t, u) -> {
            return Uncheck.apply(this, t, u);
        };
    }
}
