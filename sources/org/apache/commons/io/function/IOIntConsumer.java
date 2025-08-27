package org.apache.commons.io.function;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

@FunctionalInterface
/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/function/IOIntConsumer.class */
public interface IOIntConsumer {
    public static final IOIntConsumer NOOP = i -> {
    };

    void accept(int i) throws IOException;

    default IOIntConsumer andThen(IOIntConsumer after) {
        Objects.requireNonNull(after);
        return i -> {
            accept(i);
            after.accept(i);
        };
    }

    default Consumer<Integer> asConsumer() {
        return i -> {
            Uncheck.accept(this, i.intValue());
        };
    }

    default IntConsumer asIntConsumer() {
        return i -> {
            Uncheck.accept(this, i);
        };
    }
}
