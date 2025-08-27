package org.terracotta.context;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/context/WeakIdentityHashMap.class */
public class WeakIdentityHashMap<K, V> {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) WeakIdentityHashMap.class);
    private final ReferenceQueue<K> referenceQueue = new ReferenceQueue<>();
    private final ConcurrentHashMap<Reference<K>, V> backing = new ConcurrentHashMap<>();

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/context/WeakIdentityHashMap$Cleanable.class */
    public interface Cleanable {
        void clean();
    }

    public V get(K key) {
        clean();
        return this.backing.get(createReference(key, null));
    }

    public V putIfAbsent(K key, V value) {
        clean();
        return this.backing.putIfAbsent(createReference(key, this.referenceQueue), value);
    }

    public V remove(K key) {
        V v = this.backing.remove(createReference(key, null));
        clean();
        return v;
    }

    private void clean() {
        while (true) {
            Reference<? extends K> ref = this.referenceQueue.poll();
            if (ref != null) {
                V dead = this.backing.remove(ref);
                if (dead instanceof Cleanable) {
                    try {
                        ((Cleanable) dead).clean();
                    } catch (Throwable t) {
                        LOGGER.warn("Cleaning failed with : {}", t);
                    }
                }
            } else {
                return;
            }
        }
    }

    protected Reference<K> createReference(K key, ReferenceQueue<? super K> queue) {
        return new IdentityWeakReference(key, queue);
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/context/WeakIdentityHashMap$IdentityWeakReference.class */
    static class IdentityWeakReference<T> extends WeakReference<T> {
        private final int hashCode;

        public IdentityWeakReference(T t) {
            this(t, null);
        }

        public IdentityWeakReference(T t, ReferenceQueue<? super T> rq) {
            super(t, rq);
            this.hashCode = System.identityHashCode(t);
        }

        public int hashCode() {
            return this.hashCode;
        }

        public boolean equals(Object o) {
            Object obj;
            if (this == o) {
                return true;
            }
            return (o instanceof IdentityWeakReference) && (obj = get()) != null && obj == ((IdentityWeakReference) o).get();
        }
    }
}
