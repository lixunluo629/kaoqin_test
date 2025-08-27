package org.terracotta.offheapstore.storage.listener;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/storage/listener/ListenableStorageEngine.class */
public interface ListenableStorageEngine<K, V> {
    void registerListener(StorageEngineListener<? super K, ? super V> storageEngineListener);
}
