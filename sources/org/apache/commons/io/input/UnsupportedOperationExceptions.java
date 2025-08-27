package org.apache.commons.io.input;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/UnsupportedOperationExceptions.class */
final class UnsupportedOperationExceptions {
    private static final String MARK_RESET = "mark/reset";

    UnsupportedOperationExceptions() {
    }

    static UnsupportedOperationException mark() {
        return method(MARK_RESET);
    }

    static UnsupportedOperationException method(String method) {
        return new UnsupportedOperationException(method + " not supported");
    }

    static UnsupportedOperationException reset() {
        return method(MARK_RESET);
    }
}
