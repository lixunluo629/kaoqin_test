package org.terracotta.offheapstore.storage.listener;

import java.nio.ByteBuffer;
import java.util.concurrent.Callable;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/storage/listener/RecoveryStorageEngineListener.class */
public interface RecoveryStorageEngineListener<K, V> extends StorageEngineListener<K, V> {
    void recovered(Callable<? extends K> callable, Callable<? extends V> callable2, ByteBuffer byteBuffer, ByteBuffer byteBuffer2, int i, int i2, long j);
}
