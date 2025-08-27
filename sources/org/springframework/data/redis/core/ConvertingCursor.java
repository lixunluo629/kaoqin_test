package org.springframework.data.redis.core;

import java.io.IOException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.Assert;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/ConvertingCursor.class */
public class ConvertingCursor<S, T> implements Cursor<T> {
    private Cursor<S> delegate;
    private Converter<S, T> converter;

    public ConvertingCursor(Cursor<S> cursor, Converter<S, T> converter) {
        Assert.notNull(cursor, "Cursor delegate must not be 'null'.");
        Assert.notNull(cursor, "Converter must not be 'null'.");
        this.delegate = cursor;
        this.converter = converter;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.delegate.hasNext();
    }

    @Override // java.util.Iterator
    public T next() {
        return (T) this.converter.convert(this.delegate.next());
    }

    @Override // java.util.Iterator
    public void remove() {
        this.delegate.remove();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.delegate.close();
    }

    @Override // org.springframework.data.redis.core.Cursor
    public long getCursorId() {
        return this.delegate.getCursorId();
    }

    @Override // org.springframework.data.redis.core.Cursor
    public boolean isClosed() {
        return this.delegate.isClosed();
    }

    @Override // org.springframework.data.redis.core.Cursor
    public Cursor<T> open() {
        this.delegate = this.delegate.open();
        return this;
    }

    @Override // org.springframework.data.redis.core.Cursor
    public long getPosition() {
        return this.delegate.getPosition();
    }
}
