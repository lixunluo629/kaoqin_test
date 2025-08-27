package org.apache.commons.io.function;

import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/function/IOIterator.class */
public interface IOIterator<E> {
    boolean hasNext() throws IOException;

    E next() throws IOException;

    Iterator<E> unwrap();

    static <E> IOIterator<E> adapt(Iterable<E> iterable) {
        return IOIteratorAdapter.adapt((Iterator) iterable.iterator());
    }

    static <E> IOIterator<E> adapt(Iterator<E> iterator) {
        return IOIteratorAdapter.adapt((Iterator) iterator);
    }

    default Iterator<E> asIterator() {
        return new UncheckedIOIterator(this);
    }

    default void forEachRemaining(IOConsumer<? super E> action) throws IOException {
        Objects.requireNonNull(action);
        while (hasNext()) {
            action.accept(next());
        }
    }

    default void remove() throws IOException {
        unwrap().remove();
    }
}
