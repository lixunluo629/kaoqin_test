package org.ehcache.sizeof.util;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/sizeof/util/WeakIdentityConcurrentMap.class */
public final class WeakIdentityConcurrentMap<K, V> {
    private final ConcurrentMap<WeakReference<K>, V> map;
    private final ReferenceQueue<K> queue;
    private final CleanUpTask<V> cleanUpTask;

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/sizeof/util/WeakIdentityConcurrentMap$CleanUpTask.class */
    public interface CleanUpTask<T> {
        void cleanUp(T t);
    }

    public WeakIdentityConcurrentMap() {
        this(null);
    }

    public WeakIdentityConcurrentMap(CleanUpTask<V> cleanUpTask) {
        this.map = new ConcurrentHashMap();
        this.queue = new ReferenceQueue<>();
        this.cleanUpTask = cleanUpTask;
    }

    public V put(K key, V value) {
        cleanUp();
        return this.map.put(new IdentityWeakReference(key, this.queue), value);
    }

    public V remove(K key) {
        cleanUp();
        return this.map.remove(new IdentityWeakReference(key, this.queue));
    }

    public String toString() {
        cleanUp();
        return this.map.toString();
    }

    public V putIfAbsent(K key, V value) {
        cleanUp();
        return this.map.putIfAbsent(new IdentityWeakReference(key, this.queue), value);
    }

    public V get(K key) {
        cleanUp();
        return this.map.get(new IdentityWeakReference(key));
    }

    public void cleanUp() {
        while (true) {
            Reference<? extends K> reference = this.queue.poll();
            if (reference != null) {
                V value = this.map.remove(reference);
                if (this.cleanUpTask != null && value != null) {
                    this.cleanUpTask.cleanUp(value);
                }
            } else {
                return;
            }
        }
    }

    public Set<K> keySet() {
        cleanUp();
        HashSet<K> ks = new HashSet<>();
        for (WeakReference<K> weakReference : this.map.keySet()) {
            K k = weakReference.get();
            if (k != null) {
                ks.add(k);
            }
        }
        return ks;
    }

    public boolean containsKey(K key) {
        cleanUp();
        return this.map.containsKey(new IdentityWeakReference(key));
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/sizeof/util/WeakIdentityConcurrentMap$IdentityWeakReference.class */
    private static final class IdentityWeakReference<T> extends WeakReference<T> {
        private final int hashCode;

        IdentityWeakReference(T reference) {
            this(reference, null);
        }

        IdentityWeakReference(T reference, ReferenceQueue<T> referenceQueue) {
            super(reference, referenceQueue);
            this.hashCode = reference == null ? 0 : System.identityHashCode(reference);
        }

        public String toString() {
            return String.valueOf(get());
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
}
