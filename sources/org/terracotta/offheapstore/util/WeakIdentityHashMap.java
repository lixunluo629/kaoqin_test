package org.terracotta.offheapstore.util;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/util/WeakIdentityHashMap.class */
public class WeakIdentityHashMap<K, V> {
    private final Map<WeakReference<K>, V> map;
    private final ReferenceQueue<K> queue;
    private final ReaperTask<V> reaperTask;

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/util/WeakIdentityHashMap$ReaperTask.class */
    public interface ReaperTask<T> {
        void reap(T t);
    }

    public WeakIdentityHashMap() {
        this(null);
    }

    public WeakIdentityHashMap(ReaperTask<V> reaperTask) {
        this.map = new HashMap();
        this.queue = new ReferenceQueue<>();
        this.reaperTask = reaperTask;
    }

    public V put(K key, V value) {
        reap();
        WeakReference<K> keyRef = new IdentityWeakReference<>(key, this.queue);
        return this.map.put(keyRef, value);
    }

    public V get(K key) {
        reap();
        WeakReference<K> keyRef = new IdentityWeakReference<>(key);
        return this.map.get(keyRef);
    }

    public V remove(K key) {
        reap();
        return this.map.remove(new IdentityWeakReference(key));
    }

    public Iterator<Map.Entry<K, V>> entries() {
        return new Iterator<Map.Entry<K, V>>() { // from class: org.terracotta.offheapstore.util.WeakIdentityHashMap.1
            private final Iterator<Map.Entry<WeakReference<K>, V>> delegate;

            {
                this.delegate = WeakIdentityHashMap.this.map.entrySet().iterator();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.delegate.hasNext();
            }

            @Override // java.util.Iterator
            public Map.Entry<K, V> next() {
                Map.Entry<WeakReference<K>, V> e = this.delegate.next();
                K key = e.getKey().get();
                V value = e.getValue();
                if (key == null) {
                    return null;
                }
                return new SimpleEntry(key, value);
            }

            @Override // java.util.Iterator
            public void remove() {
                this.delegate.remove();
            }
        };
    }

    public Iterator<V> values() {
        return this.map.values().iterator();
    }

    public void reap() {
        while (true) {
            Reference<? extends K> ref = this.queue.poll();
            if (ref != null) {
                V removed = this.map.remove(ref);
                if (this.reaperTask != null && removed != null) {
                    this.reaperTask.reap(removed);
                }
            } else {
                return;
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/util/WeakIdentityHashMap$IdentityWeakReference.class */
    static class IdentityWeakReference<T> extends WeakReference<T> {
        final int hashCode;

        IdentityWeakReference(T o) {
            this(o, null);
        }

        IdentityWeakReference(T o, ReferenceQueue<T> q) {
            super(o, q);
            this.hashCode = o == null ? 0 : System.identityHashCode(o);
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof IdentityWeakReference)) {
                return false;
            }
            IdentityWeakReference<?> wr = (IdentityWeakReference) o;
            Object got = get();
            return got != null && got == wr.get();
        }

        public int hashCode() {
            return this.hashCode;
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/util/WeakIdentityHashMap$SimpleEntry.class */
    static class SimpleEntry<K, V> implements Map.Entry<K, V> {
        private final K key;
        private final V value;

        SimpleEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override // java.util.Map.Entry
        public K getKey() {
            return this.key;
        }

        @Override // java.util.Map.Entry
        public V getValue() {
            return this.value;
        }

        @Override // java.util.Map.Entry
        public V setValue(V v) {
            throw new UnsupportedOperationException();
        }
    }
}
