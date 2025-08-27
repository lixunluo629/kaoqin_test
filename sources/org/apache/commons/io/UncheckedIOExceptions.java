package org.apache.commons.io;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/UncheckedIOExceptions.class */
final class UncheckedIOExceptions {
    public static UncheckedIOException create(Object message) {
        String string = Objects.toString(message);
        return new UncheckedIOException(string, new IOException(string));
    }

    public static UncheckedIOException wrap(IOException e, Object message) {
        return new UncheckedIOException(Objects.toString(message), e);
    }

    private UncheckedIOExceptions() {
    }
}
