package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@GwtCompatible
/* loaded from: guava-18.0.jar:com/google/common/util/concurrent/AtomicLongMap.class */
public final class AtomicLongMap<K> {
    private final ConcurrentHashMap<K, AtomicLong> map;
    private transient Map<K, Long> asMap;

    private AtomicLongMap(ConcurrentHashMap<K, AtomicLong> map) {
        this.map = (ConcurrentHashMap) Preconditions.checkNotNull(map);
    }

    public static <K> AtomicLongMap<K> create() {
        return new AtomicLongMap<>(new ConcurrentHashMap());
    }

    public static <K> AtomicLongMap<K> create(Map<? extends K, ? extends Long> m) {
        AtomicLongMap<K> result = create();
        result.putAll(m);
        return result;
    }

    public long get(K key) {
        AtomicLong atomic = this.map.get(key);
        if (atomic == null) {
            return 0L;
        }
        return atomic.get();
    }

    public long incrementAndGet(K key) {
        return addAndGet(key, 1L);
    }

    public long decrementAndGet(K key) {
        return addAndGet(key, -1L);
    }

    public long addAndGet(K key, long delta) {
        AtomicLong atomic;
        long oldValue;
        long newValue;
        do {
            atomic = this.map.get(key);
            if (atomic == null) {
                atomic = this.map.putIfAbsent(key, new AtomicLong(delta));
                if (atomic == null) {
                    return delta;
                }
            }
            do {
                oldValue = atomic.get();
                if (oldValue != 0) {
                    newValue = oldValue + delta;
                }
            } while (!atomic.compareAndSet(oldValue, newValue));
            return newValue;
        } while (!this.map.replace(key, atomic, new AtomicLong(delta)));
        return delta;
    }

    public long getAndIncrement(K key) {
        return getAndAdd(key, 1L);
    }

    public long getAndDecrement(K key) {
        return getAndAdd(key, -1L);
    }

    public long getAndAdd(K key, long delta) {
        AtomicLong atomic;
        long oldValue;
        long newValue;
        do {
            atomic = this.map.get(key);
            if (atomic == null) {
                atomic = this.map.putIfAbsent(key, new AtomicLong(delta));
                if (atomic == null) {
                    return 0L;
                }
            }
            do {
                oldValue = atomic.get();
                if (oldValue != 0) {
                    newValue = oldValue + delta;
                }
            } while (!atomic.compareAndSet(oldValue, newValue));
            return oldValue;
        } while (!this.map.replace(key, atomic, new AtomicLong(delta)));
        return 0L;
    }

    public long put(K key, long newValue) {
        AtomicLong atomic;
        long oldValue;
        do {
            atomic = this.map.get(key);
            if (atomic == null) {
                atomic = this.map.putIfAbsent(key, new AtomicLong(newValue));
                if (atomic == null) {
                    return 0L;
                }
            }
            do {
                oldValue = atomic.get();
                if (oldValue == 0) {
                }
            } while (!atomic.compareAndSet(oldValue, newValue));
            return oldValue;
        } while (!this.map.replace(key, atomic, new AtomicLong(newValue)));
        return 0L;
    }

    public void putAll(Map<? extends K, ? extends Long> m) {
        for (Map.Entry<? extends K, ? extends Long> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue().longValue());
        }
    }

    public long remove(K key) {
        long oldValue;
        AtomicLong atomic = this.map.get(key);
        if (atomic == null) {
            return 0L;
        }
        do {
            oldValue = atomic.get();
            if (oldValue == 0) {
                break;
            }
        } while (!atomic.compareAndSet(oldValue, 0L));
        this.map.remove(key, atomic);
        return oldValue;
    }

    public void removeAllZeros() {
        for (K key : this.map.keySet()) {
            AtomicLong atomic = this.map.get(key);
            if (atomic != null && atomic.get() == 0) {
                this.map.remove(key, atomic);
            }
        }
    }

    public long sum() {
        long sum = 0;
        for (AtomicLong value : this.map.values()) {
            sum += value.get();
        }
        return sum;
    }

    public Map<K, Long> asMap() {
        Map<K, Long> result = this.asMap;
        if (result != null) {
            return result;
        }
        Map<K, Long> mapCreateAsMap = createAsMap();
        this.asMap = mapCreateAsMap;
        return mapCreateAsMap;
    }

    private Map<K, Long> createAsMap() {
        return Collections.unmodifiableMap(Maps.transformValues(this.map, new Function<AtomicLong, Long>() { // from class: com.google.common.util.concurrent.AtomicLongMap.1
            @Override // com.google.common.base.Function
            public Long apply(AtomicLong atomic) {
                return Long.valueOf(atomic.get());
            }
        }));
    }

    public boolean containsKey(Object key) {
        return this.map.containsKey(key);
    }

    public int size() {
        return this.map.size();
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    public void clear() {
        this.map.clear();
    }

    public String toString() {
        return this.map.toString();
    }

    long putIfAbsent(K key, long newValue) {
        AtomicLong atomic;
        do {
            atomic = this.map.get(key);
            if (atomic == null) {
                atomic = this.map.putIfAbsent(key, new AtomicLong(newValue));
                if (atomic == null) {
                    return 0L;
                }
            }
            long oldValue = atomic.get();
            if (oldValue != 0) {
                return oldValue;
            }
        } while (!this.map.replace(key, atomic, new AtomicLong(newValue)));
        return 0L;
    }

    boolean replace(K key, long expectedOldValue, long newValue) {
        if (expectedOldValue == 0) {
            return putIfAbsent(key, newValue) == 0;
        }
        AtomicLong atomic = this.map.get(key);
        if (atomic == null) {
            return false;
        }
        return atomic.compareAndSet(expectedOldValue, newValue);
    }

    boolean remove(K key, long value) {
        AtomicLong atomic = this.map.get(key);
        if (atomic == null) {
            return false;
        }
        long oldValue = atomic.get();
        if (oldValue != value) {
            return false;
        }
        if (oldValue == 0 || atomic.compareAndSet(oldValue, 0L)) {
            this.map.remove(key, atomic);
            return true;
        }
        return false;
    }
}
