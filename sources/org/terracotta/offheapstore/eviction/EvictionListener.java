package org.terracotta.offheapstore.eviction;

import java.util.Map;
import java.util.concurrent.Callable;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/eviction/EvictionListener.class */
public interface EvictionListener<K, V> {
    void evicting(Callable<Map.Entry<K, V>> callable);
}
