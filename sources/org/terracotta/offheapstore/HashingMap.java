package org.terracotta.offheapstore;

import java.util.Map;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/HashingMap.class */
public interface HashingMap<K, V> extends Map<K, V> {
    Map<K, V> removeAllWithHash(int i);
}
