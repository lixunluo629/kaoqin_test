package org.springframework.data.keyvalue.core;

import java.util.Iterator;
import org.springframework.data.util.CloseableIterator;
import org.springframework.util.Assert;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/ForwardingCloseableIterator.class */
public class ForwardingCloseableIterator<T> implements CloseableIterator<T> {
    private final Iterator<? extends T> delegate;
    private final Runnable closeHandler;

    public ForwardingCloseableIterator(Iterator<? extends T> delegate) {
        this(delegate, null);
    }

    public ForwardingCloseableIterator(Iterator<? extends T> delegate, Runnable closeHandler) {
        Assert.notNull(delegate, "Delegate iterator must not be null!");
        this.delegate = delegate;
        this.closeHandler = closeHandler;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.delegate.hasNext();
    }

    @Override // java.util.Iterator
    public T next() {
        return this.delegate.next();
    }

    @Override // org.springframework.data.util.CloseableIterator, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (this.closeHandler != null) {
            this.closeHandler.run();
        }
    }
}
