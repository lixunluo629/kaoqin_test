package org.apache.commons.io;

import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/StreamIterator.class */
public final class StreamIterator<E> implements Iterator<E>, AutoCloseable {
    private final Iterator<E> iterator;
    private final Stream<E> stream;
    private boolean closed;

    public static <T> StreamIterator<T> iterator(Stream<T> stream) {
        return new StreamIterator<>(stream);
    }

    private StreamIterator(Stream<E> stream) {
        this.stream = (Stream) Objects.requireNonNull(stream, "stream");
        this.iterator = stream.iterator();
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        this.closed = true;
        this.stream.close();
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        if (this.closed) {
            return false;
        }
        boolean hasNext = this.iterator.hasNext();
        if (!hasNext) {
            close();
        }
        return hasNext;
    }

    @Override // java.util.Iterator
    public E next() {
        E next = this.iterator.next();
        if (next == null) {
            close();
        }
        return next;
    }
}
