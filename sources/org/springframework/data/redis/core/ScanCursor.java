package org.springframework.data.redis.core;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.util.CollectionUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/ScanCursor.class */
public abstract class ScanCursor<T> implements Cursor<T> {
    private CursorState state;
    private long cursorId;
    private Iterator<T> delegate;
    private final ScanOptions scanOptions;
    private long position;

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/ScanCursor$CursorState.class */
    enum CursorState {
        READY,
        OPEN,
        FINISHED,
        CLOSED
    }

    protected abstract ScanIteration<T> doScan(long j, ScanOptions scanOptions);

    public ScanCursor() {
        this(ScanOptions.NONE);
    }

    public ScanCursor(ScanOptions options) {
        this(0L, options);
    }

    public ScanCursor(long cursorId) {
        this(cursorId, ScanOptions.NONE);
    }

    public ScanCursor(long cursorId, ScanOptions options) {
        this.scanOptions = options != null ? options : ScanOptions.NONE;
        this.cursorId = cursorId;
        this.state = CursorState.READY;
        this.delegate = Collections.emptyList().iterator();
    }

    private void scan(long cursorId) {
        ScanIteration<T> result = doScan(cursorId, this.scanOptions);
        processScanResult(result);
    }

    @Override // org.springframework.data.redis.core.Cursor
    public final ScanCursor<T> open() {
        if (!isReady()) {
            throw new InvalidDataAccessApiUsageException("Cursor already " + this.state + ". Cannot (re)open it.");
        }
        this.state = CursorState.OPEN;
        doOpen(this.cursorId);
        return this;
    }

    protected void doOpen(long cursorId) {
        scan(cursorId);
    }

    private void processScanResult(ScanIteration<T> result) {
        if (result == null) {
            resetDelegate();
            this.state = CursorState.FINISHED;
            return;
        }
        this.cursorId = Long.valueOf(result.getCursorId()).longValue();
        if (this.cursorId == 0) {
            this.state = CursorState.FINISHED;
        }
        if (!CollectionUtils.isEmpty((Collection<?>) result.getItems())) {
            this.delegate = result.iterator();
        } else {
            resetDelegate();
        }
    }

    private void resetDelegate() {
        this.delegate = Collections.emptyList().iterator();
    }

    @Override // org.springframework.data.redis.core.Cursor
    public long getCursorId() {
        return this.cursorId;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        assertCursorIsOpen();
        while (!this.delegate.hasNext() && !CursorState.FINISHED.equals(this.state)) {
            scan(this.cursorId);
        }
        if (this.delegate.hasNext() || this.cursorId > 0) {
            return true;
        }
        return false;
    }

    private void assertCursorIsOpen() {
        if (isReady() || isClosed()) {
            throw new InvalidDataAccessApiUsageException("Cannot access closed cursor. Did you forget to call open()?");
        }
    }

    @Override // java.util.Iterator
    public T next() {
        assertCursorIsOpen();
        if (!hasNext()) {
            throw new NoSuchElementException("No more elements available for cursor " + this.cursorId + ".");
        }
        T next = moveNext(this.delegate);
        this.position++;
        return next;
    }

    protected T moveNext(Iterator<T> source) {
        return source.next();
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException("Remove is not supported");
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public final void close() throws IOException {
        try {
            doClose();
        } finally {
            this.state = CursorState.CLOSED;
        }
    }

    protected void doClose() {
    }

    @Override // org.springframework.data.redis.core.Cursor
    public boolean isClosed() {
        return this.state == CursorState.CLOSED;
    }

    protected final boolean isReady() {
        return this.state == CursorState.READY;
    }

    protected final boolean isOpen() {
        return this.state == CursorState.OPEN;
    }

    @Override // org.springframework.data.redis.core.Cursor
    public long getPosition() {
        return this.position;
    }
}
