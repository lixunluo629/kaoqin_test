package org.terracotta.offheapstore.pinning;

import java.util.concurrent.ConcurrentMap;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/pinning/PinnableCache.class */
public interface PinnableCache<K, V> extends ConcurrentMap<K, V> {
    boolean isPinned(Object obj);

    void setPinning(K k, boolean z);

    V putPinned(K k, V v);

    V getAndPin(K k);
}
