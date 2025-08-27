package org.ehcache.impl.serialization;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import org.ehcache.impl.internal.concurrent.ConcurrentHashMap;
import org.ehcache.spi.persistence.StateHolder;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/serialization/TransientStateHolder.class */
public class TransientStateHolder<K, V> implements StateHolder<K, V>, Serializable {
    private final ConcurrentMap<K, V> map = new ConcurrentHashMap();

    @Override // org.ehcache.spi.persistence.StateHolder
    public V putIfAbsent(K key, V value) {
        return this.map.putIfAbsent(key, value);
    }

    @Override // org.ehcache.spi.persistence.StateHolder
    public V get(K key) {
        return this.map.get(key);
    }

    @Override // org.ehcache.spi.persistence.StateHolder
    public Set<Map.Entry<K, V>> entrySet() {
        return this.map.entrySet();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TransientStateHolder<?, ?> that = (TransientStateHolder) o;
        return this.map.equals(that.map);
    }

    public int hashCode() {
        return this.map.hashCode();
    }
}
