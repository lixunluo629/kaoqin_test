package org.apache.commons.io.function;

import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/function/IOIteratorAdapter.class */
final class IOIteratorAdapter<E> implements IOIterator<E> {
    private final Iterator<E> delegate;

    static <E> IOIteratorAdapter<E> adapt(Iterator<E> delegate) {
        return new IOIteratorAdapter<>(delegate);
    }

    IOIteratorAdapter(Iterator<E> delegate) {
        this.delegate = (Iterator) Objects.requireNonNull(delegate, "delegate");
    }

    @Override // org.apache.commons.io.function.IOIterator
    public boolean hasNext() throws IOException {
        return this.delegate.hasNext();
    }

    @Override // org.apache.commons.io.function.IOIterator
    public E next() throws IOException {
        return this.delegate.next();
    }

    @Override // org.apache.commons.io.function.IOIterator
    public Iterator<E> unwrap() {
        return this.delegate;
    }
}
