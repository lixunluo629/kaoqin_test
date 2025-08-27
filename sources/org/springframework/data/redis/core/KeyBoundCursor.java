package org.springframework.data.redis.core;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/KeyBoundCursor.class */
public abstract class KeyBoundCursor<T> extends ScanCursor<T> {
    private byte[] key;

    protected abstract ScanIteration<T> doScan(byte[] bArr, long j, ScanOptions scanOptions);

    public KeyBoundCursor(byte[] key, long cursorId, ScanOptions options) {
        super(cursorId, options != null ? options : ScanOptions.NONE);
        this.key = key;
    }

    @Override // org.springframework.data.redis.core.ScanCursor
    protected ScanIteration<T> doScan(long cursorId, ScanOptions options) {
        return doScan(this.key, cursorId, options);
    }

    public byte[] getKey() {
        return this.key;
    }
}
