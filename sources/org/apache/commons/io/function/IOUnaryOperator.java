package org.apache.commons.io.function;

import java.util.function.UnaryOperator;

@FunctionalInterface
/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/function/IOUnaryOperator.class */
public interface IOUnaryOperator<T> extends IOFunction<T, T> {
    static <T> IOUnaryOperator<T> identity() {
        return t -> {
            return t;
        };
    }

    default UnaryOperator<T> asUnaryOperator() {
        return t -> {
            return Uncheck.apply(this, t);
        };
    }
}
