package org.terracotta.offheapstore.storage.listener;

import java.nio.ByteBuffer;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/storage/listener/RuntimeStorageEngineListener.class */
public interface RuntimeStorageEngineListener<K, V> extends StorageEngineListener<K, V> {
    void written(K k, V v, ByteBuffer byteBuffer, ByteBuffer byteBuffer2, int i, int i2, long j);

    void freed(long j, int i, ByteBuffer byteBuffer, boolean z);

    void cleared();

    void copied(int i, long j, long j2, int i2);
}
