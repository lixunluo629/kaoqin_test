package org.apache.commons.io.function;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.apache.commons.io.IOExceptionList;
import org.apache.commons.io.IOIndexedException;

@FunctionalInterface
/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/function/IOConsumer.class */
public interface IOConsumer<T> {
    public static final IOConsumer<?> NOOP_IO_CONSUMER = t -> {
    };

    void accept(T t) throws IOException;

    static <T> void forAll(IOConsumer<T> action, Iterable<T> iterable) throws IOExceptionList {
        IOStreams.forAll(IOStreams.of(iterable), action);
    }

    static <T> void forAll(IOConsumer<T> action, Stream<T> stream) throws IOExceptionList {
        IOStreams.forAll(stream, action, (v1, v2) -> {
            return new IOIndexedException(v1, v2);
        });
    }

    @SafeVarargs
    static <T> void forAll(IOConsumer<T> action, T... array) throws IOExceptionList {
        IOStreams.forAll(IOStreams.of(array), action);
    }

    static <T> void forEach(Iterable<T> iterable, IOConsumer<T> action) throws IOException {
        IOStreams.forEach(IOStreams.of(iterable), action);
    }

    static <T> void forEach(Stream<T> stream, IOConsumer<T> action) throws IOException {
        IOStreams.forEach(stream, action);
    }

    static <T> void forEach(T[] array, IOConsumer<T> action) throws IOException {
        IOStreams.forEach(IOStreams.of(array), action);
    }

    static <T> IOConsumer<T> noop() {
        return (IOConsumer<T>) NOOP_IO_CONSUMER;
    }

    default IOConsumer<T> andThen(IOConsumer<? super T> after) {
        Objects.requireNonNull(after, "after");
        return obj -> {
            accept(obj);
            after.accept(obj);
        };
    }

    default Consumer<T> asConsumer() {
        return t -> {
            Uncheck.accept(this, t);
        };
    }
}
