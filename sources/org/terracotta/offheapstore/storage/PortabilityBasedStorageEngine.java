package org.terracotta.offheapstore.storage;

import java.nio.ByteBuffer;
import org.terracotta.offheapstore.storage.listener.AbstractListenableStorageEngine;
import org.terracotta.offheapstore.storage.portability.Portability;
import org.terracotta.offheapstore.storage.portability.WriteBackPortability;
import org.terracotta.offheapstore.storage.portability.WriteContext;
import org.terracotta.offheapstore.util.ByteBufferUtils;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/storage/PortabilityBasedStorageEngine.class */
public abstract class PortabilityBasedStorageEngine<K, V> extends AbstractListenableStorageEngine<K, V> implements StorageEngine<K, V>, BinaryStorageEngine {
    protected final Portability<? super K> keyPortability;
    protected final Portability<? super V> valuePortability;
    private CachedEncode<K, V> lastMapping;

    protected abstract void free(long j);

    protected abstract void clearInternal();

    protected abstract ByteBuffer readKeyBuffer(long j);

    protected abstract WriteContext getKeyWriteContext(long j);

    protected abstract ByteBuffer readValueBuffer(long j);

    protected abstract WriteContext getValueWriteContext(long j);

    protected abstract Long writeMappingBuffers(ByteBuffer byteBuffer, ByteBuffer byteBuffer2, int i);

    public PortabilityBasedStorageEngine(Portability<? super K> keyPortability, Portability<? super V> valuePortability) {
        this.keyPortability = keyPortability;
        this.valuePortability = valuePortability;
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public final Long writeMapping(K key, V value, int hash, int metadata) {
        Long result;
        if (this.lastMapping != null && this.lastMapping.getKey() == key && this.lastMapping.getValue() == value) {
            result = writeMappingBuffers(this.lastMapping.getEncodedKey(), this.lastMapping.getEncodedValue(), hash);
        } else {
            ByteBuffer keyBuffer = this.keyPortability.encode(key);
            ByteBuffer valueBuffer = this.valuePortability.encode(value);
            result = writeMappingBuffers(keyBuffer.duplicate(), valueBuffer.duplicate(), hash);
            this.lastMapping = new CachedEncode<>(key, value, keyBuffer, valueBuffer, result);
        }
        if (result != null) {
            fireWritten(key, value, this.lastMapping.getEncodedKey(), this.lastMapping.getEncodedValue(), hash, metadata, result.longValue());
        }
        return result;
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public void attachedMapping(long encoding, int hash, int metadata) {
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public final void freeMapping(long encoding, int hash, boolean removal) {
        if (hasListeners()) {
            ByteBuffer binaryKey = readBinaryKey(encoding);
            free(encoding);
            fireFreed(encoding, hash, binaryKey, removal);
            return;
        }
        free(encoding);
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public final void clear() {
        clearInternal();
        fireCleared();
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public V readValue(long j) {
        if (this.valuePortability instanceof WriteBackPortability) {
            return (V) ((WriteBackPortability) this.valuePortability).decode(readValueBuffer(j), getValueWriteContext(j));
        }
        return this.valuePortability.decode(readValueBuffer(j));
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public boolean equalsValue(Object value, long encoding) {
        return this.valuePortability.equals(value, readValueBuffer(encoding));
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public K readKey(long j, int i) {
        if (this.keyPortability instanceof WriteBackPortability) {
            return (K) ((WriteBackPortability) this.keyPortability).decode(readKeyBuffer(j), getKeyWriteContext(j));
        }
        return this.keyPortability.decode(readKeyBuffer(j));
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public boolean equalsKey(Object key, long encoding) {
        return this.keyPortability.equals(key, readKeyBuffer(encoding));
    }

    @Override // org.terracotta.offheapstore.storage.BinaryStorageEngine
    public ByteBuffer readBinaryKey(long encoding) {
        Long cachedEncoding;
        CachedEncode<K, V> cache = this.lastMapping;
        if (cache != null && (cachedEncoding = cache.getEncoding()) != null && cachedEncoding.longValue() == encoding) {
            return cache.getEncodedKey();
        }
        ByteBuffer attached = readKeyBuffer(encoding);
        ByteBuffer detached = ByteBuffer.allocate(attached.remaining());
        detached.put(attached).flip();
        return detached;
    }

    @Override // org.terracotta.offheapstore.storage.BinaryStorageEngine
    public ByteBuffer readBinaryValue(long encoding) {
        Long cachedEncoding;
        CachedEncode<K, V> cache = this.lastMapping;
        if (cache != null && (cachedEncoding = cache.getEncoding()) != null && cachedEncoding.longValue() == encoding) {
            return cache.getEncodedValue();
        }
        ByteBuffer attached = readValueBuffer(encoding);
        ByteBuffer detached = ByteBuffer.allocate(attached.remaining());
        detached.put(attached).flip();
        return detached;
    }

    @Override // org.terracotta.offheapstore.storage.BinaryStorageEngine
    public boolean equalsBinaryKey(ByteBuffer binaryKey, long encoding) {
        return binaryKey.equals(readBinaryKey(encoding)) || equalsKey(this.keyPortability.decode(binaryKey.duplicate()), encoding);
    }

    @Override // org.terracotta.offheapstore.storage.BinaryStorageEngine
    public Long writeBinaryMapping(ByteBuffer[] binaryKey, ByteBuffer[] binaryValue, int pojoHash, int metadata) {
        return writeMappingBuffersGathering(binaryKey, binaryValue, pojoHash);
    }

    @Override // org.terracotta.offheapstore.storage.BinaryStorageEngine
    public Long writeBinaryMapping(ByteBuffer binaryKey, ByteBuffer binaryValue, int pojoHash, int metadata) {
        return writeMappingBuffers(binaryKey, binaryValue, pojoHash);
    }

    protected Long writeMappingBuffersGathering(ByteBuffer[] keyBuffers, ByteBuffer[] valueBuffers, int hash) {
        return writeMappingBuffers(ByteBufferUtils.aggregate(keyBuffers), ByteBufferUtils.aggregate(valueBuffers), hash);
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public void invalidateCache() {
        this.lastMapping = null;
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/storage/PortabilityBasedStorageEngine$CachedEncode.class */
    static class CachedEncode<K, V> {
        private final K key;
        private final V value;
        private final ByteBuffer keyBuffer;
        private final ByteBuffer valueBuffer;
        private final Long encoding;

        public CachedEncode(K key, V value, ByteBuffer keyBuffer, ByteBuffer valueBuffer, Long encoding) {
            this.key = key;
            this.value = value;
            this.keyBuffer = keyBuffer;
            this.valueBuffer = valueBuffer;
            this.encoding = encoding;
        }

        final K getKey() {
            return this.key;
        }

        final V getValue() {
            return this.value;
        }

        final ByteBuffer getEncodedKey() {
            return this.keyBuffer.duplicate();
        }

        final ByteBuffer getEncodedValue() {
            return this.valueBuffer.duplicate();
        }

        final Long getEncoding() {
            return this.encoding;
        }
    }
}
