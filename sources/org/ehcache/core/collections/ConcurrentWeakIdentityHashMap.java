package org.ehcache.core.collections;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/collections/ConcurrentWeakIdentityHashMap.class */
public class ConcurrentWeakIdentityHashMap<K, V> implements ConcurrentMap<K, V> {
    private final ConcurrentMap<WeakReference<K>, V> map = new ConcurrentHashMap();
    private final ReferenceQueue<K> queue = new ReferenceQueue<>();

    @Override // java.util.concurrent.ConcurrentMap, java.util.Map
    public V putIfAbsent(K key, V value) {
        purgeKeys();
        return this.map.putIfAbsent(newKey(key), value);
    }

    @Override // java.util.concurrent.ConcurrentMap, java.util.Map
    public boolean remove(Object key, Object value) {
        purgeKeys();
        return this.map.remove(new WeakReference(key, null), value);
    }

    @Override // java.util.concurrent.ConcurrentMap, java.util.Map
    public boolean replace(K key, V oldValue, V newValue) {
        purgeKeys();
        return this.map.replace(newKey(key), oldValue, newValue);
    }

    @Override // java.util.concurrent.ConcurrentMap, java.util.Map
    public V replace(K key, V value) {
        purgeKeys();
        return this.map.replace(newKey(key), value);
    }

    @Override // java.util.Map
    public int size() {
        purgeKeys();
        return this.map.size();
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        purgeKeys();
        return this.map.isEmpty();
    }

    @Override // java.util.Map
    public boolean containsKey(Object key) {
        purgeKeys();
        return this.map.containsKey(new WeakReference(key, null));
    }

    @Override // java.util.Map
    public boolean containsValue(Object value) {
        purgeKeys();
        return this.map.containsValue(value);
    }

    @Override // java.util.Map
    public V get(Object key) {
        purgeKeys();
        return this.map.get(new WeakReference(key, null));
    }

    @Override // java.util.Map
    public V put(K key, V value) {
        purgeKeys();
        return this.map.put(newKey(key), value);
    }

    @Override // java.util.Map
    public V remove(Object key) {
        purgeKeys();
        return this.map.remove(new WeakReference(key, null));
    }

    @Override // java.util.Map
    public void putAll(Map<? extends K, ? extends V> m) {
        purgeKeys();
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            this.map.put(newKey(entry.getKey()), entry.getValue());
        }
    }

    @Override // java.util.Map
    public void clear() {
        purgeKeys();
        this.map.clear();
    }

    @Override // java.util.Map
    public Set<K> keySet() {
        return new AbstractSet<K>() { // from class: org.ehcache.core.collections.ConcurrentWeakIdentityHashMap.1
            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public Iterator<K> iterator() {
                ConcurrentWeakIdentityHashMap.this.purgeKeys();
                return new WeakSafeIterator<K, WeakReference<K>>(ConcurrentWeakIdentityHashMap.this.map.keySet().iterator()) { // from class: org.ehcache.core.collections.ConcurrentWeakIdentityHashMap.1.1
                    /* JADX INFO: Access modifiers changed from: protected */
                    @Override // org.ehcache.core.collections.ConcurrentWeakIdentityHashMap.WeakSafeIterator
                    public K extract(WeakReference<K> weakReference) {
                        return (K) weakReference.get();
                    }
                };
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean contains(Object o) {
                return ConcurrentWeakIdentityHashMap.this.containsKey(o);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return ConcurrentWeakIdentityHashMap.this.map.size();
            }
        };
    }

    @Override // java.util.Map
    public Collection<V> values() {
        purgeKeys();
        return this.map.values();
    }

    @Override // java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        return new AbstractSet<Map.Entry<K, V>>() { // from class: org.ehcache.core.collections.ConcurrentWeakIdentityHashMap.2
            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public Iterator<Map.Entry<K, V>> iterator() {
                ConcurrentWeakIdentityHashMap.this.purgeKeys();
                return new WeakSafeIterator<Map.Entry<K, V>, Map.Entry<WeakReference<K>, V>>(ConcurrentWeakIdentityHashMap.this.map.entrySet().iterator()) { // from class: org.ehcache.core.collections.ConcurrentWeakIdentityHashMap.2.1
                    /* JADX INFO: Access modifiers changed from: protected */
                    @Override // org.ehcache.core.collections.ConcurrentWeakIdentityHashMap.WeakSafeIterator
                    public Map.Entry<K, V> extract(Map.Entry<WeakReference<K>, V> u) {
                        Object obj = u.getKey().get();
                        if (obj == null) {
                            return null;
                        }
                        return new AbstractMap.SimpleEntry(obj, u.getValue());
                    }
                };
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return ConcurrentWeakIdentityHashMap.this.map.size();
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void purgeKeys() {
        while (true) {
            Reference<? extends K> reference = this.queue.poll();
            if (reference != null) {
                this.map.remove(reference);
            } else {
                return;
            }
        }
    }

    private WeakReference<K> newKey(K key) {
        return new WeakReference<>(key, this.queue);
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/collections/ConcurrentWeakIdentityHashMap$WeakReference.class */
    private static class WeakReference<T> extends java.lang.ref.WeakReference<T> {
        private final int hashCode;

        private WeakReference(T referent, ReferenceQueue<? super T> q) {
            super(referent, q);
            this.hashCode = referent.hashCode();
        }

        public boolean equals(Object obj) {
            return obj != null && obj.getClass() == getClass() && (this == obj || get() == ((WeakReference) obj).get());
        }

        public int hashCode() {
            return this.hashCode;
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/collections/ConcurrentWeakIdentityHashMap$WeakSafeIterator.class */
    private static abstract class WeakSafeIterator<T, U> implements Iterator<T> {
        private final Iterator<U> weakIterator;
        protected T strongNext;

        protected abstract T extract(U u);

        public WeakSafeIterator(Iterator<U> weakIterator) {
            this.weakIterator = weakIterator;
            advance();
        }

        private void advance() {
            while (this.weakIterator.hasNext()) {
                U nextU = this.weakIterator.next();
                T tExtract = extract(nextU);
                this.strongNext = tExtract;
                if (tExtract != null) {
                    return;
                }
            }
            this.strongNext = null;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.strongNext != null;
        }

        @Override // java.util.Iterator
        public final T next() {
            T next = this.strongNext;
            advance();
            return next;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
