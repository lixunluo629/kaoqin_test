package io.netty.util.internal;

import java.util.Iterator;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/internal/ReadOnlyIterator.class */
public final class ReadOnlyIterator<T> implements Iterator<T> {
    private final Iterator<? extends T> iterator;

    public ReadOnlyIterator(Iterator<? extends T> iterator) {
        this.iterator = (Iterator) ObjectUtil.checkNotNull(iterator, "iterator");
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.iterator.hasNext();
    }

    @Override // java.util.Iterator
    public T next() {
        return this.iterator.next();
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException("read-only");
    }
}
