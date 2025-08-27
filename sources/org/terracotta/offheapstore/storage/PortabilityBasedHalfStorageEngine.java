package org.terracotta.offheapstore.storage;

import java.nio.ByteBuffer;
import org.terracotta.offheapstore.storage.portability.Portability;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/storage/PortabilityBasedHalfStorageEngine.class */
public abstract class PortabilityBasedHalfStorageEngine<T> implements HalfStorageEngine<T> {
    private final Portability<? super T> portability;
    private CachedEncode<T> lastObject;

    @Override // org.terracotta.offheapstore.storage.HalfStorageEngine
    public abstract void free(int i);

    protected abstract ByteBuffer readBuffer(int i);

    protected abstract Integer writeBuffer(ByteBuffer byteBuffer, int i);

    public PortabilityBasedHalfStorageEngine(Portability<? super T> portability) {
        this.portability = portability;
    }

    @Override // org.terracotta.offheapstore.storage.HalfStorageEngine
    public Integer write(T object, int hash) {
        if (this.lastObject != null && this.lastObject.get() == object) {
            return writeBuffer(this.lastObject.getEncoded(), hash);
        }
        ByteBuffer buffer = this.portability.encode(object);
        Integer result = writeBuffer(buffer, hash);
        if (result == null) {
            this.lastObject = new CachedEncode<>(object, buffer.duplicate());
        }
        return result;
    }

    @Override // org.terracotta.offheapstore.storage.HalfStorageEngine
    public T read(int encoding) {
        return this.portability.decode(readBuffer(encoding));
    }

    @Override // org.terracotta.offheapstore.storage.HalfStorageEngine
    public boolean equals(Object value, int encoding) {
        return this.portability.equals(value, readBuffer(encoding));
    }

    @Override // org.terracotta.offheapstore.storage.HalfStorageEngine
    public void invalidateCache() {
        this.lastObject = null;
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/storage/PortabilityBasedHalfStorageEngine$CachedEncode.class */
    static class CachedEncode<T> {
        private final T object;
        private final ByteBuffer buffer;

        public CachedEncode(T object, ByteBuffer buffer) {
            this.object = object;
            this.buffer = buffer;
        }

        final T get() {
            return this.object;
        }

        final ByteBuffer getEncoded() {
            return this.buffer.duplicate();
        }
    }
}
