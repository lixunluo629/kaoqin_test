package org.apache.commons.io.function;

import java.util.Iterator;
import java.util.Objects;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/function/UncheckedIOIterator.class */
final class UncheckedIOIterator<E> implements Iterator<E> {
    private final IOIterator<E> delegate;

    UncheckedIOIterator(IOIterator<E> delegate) {
        this.delegate = (IOIterator) Objects.requireNonNull(delegate, "delegate");
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        IOIterator<E> iOIterator = this.delegate;
        Objects.requireNonNull(iOIterator);
        return ((Boolean) Uncheck.get(iOIterator::hasNext)).booleanValue();
    }

    @Override // java.util.Iterator
    public E next() {
        IOIterator<E> iOIterator = this.delegate;
        Objects.requireNonNull(iOIterator);
        return (E) Uncheck.get(iOIterator::next);
    }

    @Override // java.util.Iterator
    public void remove() {
        IOIterator<E> iOIterator = this.delegate;
        Objects.requireNonNull(iOIterator);
        Uncheck.run(iOIterator::remove);
    }
}
