package org.terracotta.offheapstore.storage.listener;

import java.nio.ByteBuffer;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArraySet;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/storage/listener/AbstractListenableStorageEngine.class */
public abstract class AbstractListenableStorageEngine<K, V> implements ListenableStorageEngine<K, V> {
    private final Set<RuntimeStorageEngineListener<? super K, ? super V>> runtimeListeners = new CopyOnWriteArraySet();
    private final Set<RecoveryStorageEngineListener<? super K, ? super V>> recoveryListeners = new CopyOnWriteArraySet();

    @Override // org.terracotta.offheapstore.storage.listener.ListenableStorageEngine
    public void registerListener(StorageEngineListener<? super K, ? super V> listener) {
        if (listener instanceof RecoveryStorageEngineListener) {
            this.recoveryListeners.add((RecoveryStorageEngineListener) listener);
        }
        if (listener instanceof RuntimeStorageEngineListener) {
            this.runtimeListeners.add((RuntimeStorageEngineListener) listener);
        }
    }

    protected final boolean hasListeners() {
        return !this.runtimeListeners.isEmpty();
    }

    protected final boolean hasRecoveryListeners() {
        return !this.recoveryListeners.isEmpty();
    }

    protected final void fireRecovered(Callable<K> key, Callable<V> value, ByteBuffer binaryKey, ByteBuffer binaryValue, int hash, int metadata, long encoding) {
        for (RecoveryStorageEngineListener<? super K, ? super V> listener : this.recoveryListeners) {
            listener.recovered(key, value, binaryKey.duplicate(), binaryValue.duplicate(), hash, metadata, encoding);
        }
    }

    protected final void fireWritten(K key, V value, ByteBuffer binaryKey, ByteBuffer binaryValue, int hash, int metadata, long encoding) {
        for (RuntimeStorageEngineListener<? super K, ? super V> listener : this.runtimeListeners) {
            listener.written(key, value, binaryKey.duplicate(), binaryValue.duplicate(), hash, metadata, encoding);
        }
    }

    protected final void fireFreed(long encoding, int hash, ByteBuffer binaryKey, boolean removed) {
        for (RuntimeStorageEngineListener<? super K, ? super V> listener : this.runtimeListeners) {
            listener.freed(encoding, hash, binaryKey.duplicate(), removed);
        }
    }

    protected final void fireCleared() {
        for (RuntimeStorageEngineListener<? super K, ? super V> listener : this.runtimeListeners) {
            listener.cleared();
        }
    }

    protected final void fireCopied(int hash, long oldEncoding, long newEncoding, int metadata) {
        for (RuntimeStorageEngineListener<? super K, ? super V> listener : this.runtimeListeners) {
            listener.copied(hash, oldEncoding, newEncoding, metadata);
        }
    }
}
