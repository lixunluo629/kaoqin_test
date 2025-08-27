package org.ehcache.jsr107;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.cache.Cache;
import javax.cache.integration.CacheLoader;
import javax.cache.integration.CacheWriter;
import org.ehcache.jsr107.internal.Jsr107CacheLoaderWriter;
import org.ehcache.spi.loaderwriter.BulkCacheWritingException;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/Eh107CacheLoaderWriter.class */
class Eh107CacheLoaderWriter<K, V> implements Jsr107CacheLoaderWriter<K, V>, Closeable {
    private final CacheLoader<K, V> cacheLoader;
    private final boolean readThrough;
    private final CacheWriter<K, V> cacheWriter;

    Eh107CacheLoaderWriter(CacheLoader<K, V> cacheLoader, boolean readThrough, CacheWriter<K, V> cacheWriter, boolean writeThrough) {
        this.cacheLoader = cacheLoader;
        this.readThrough = cacheLoader != null && readThrough;
        if (writeThrough) {
            this.cacheWriter = cacheWriter;
        } else {
            this.cacheWriter = null;
        }
    }

    @Override // org.ehcache.spi.loaderwriter.CacheLoaderWriter
    public V load(K k) throws Exception {
        if (this.readThrough) {
            return (V) this.cacheLoader.load(k);
        }
        return null;
    }

    @Override // org.ehcache.spi.loaderwriter.CacheLoaderWriter
    public Map<K, V> loadAll(Iterable<? extends K> keys) throws Exception {
        if (this.readThrough) {
            return loadAllAlways(keys);
        }
        return Collections.emptyMap();
    }

    @Override // org.ehcache.jsr107.internal.Jsr107CacheLoaderWriter
    public Map<K, V> loadAllAlways(Iterable<? extends K> keys) throws Exception {
        if (this.cacheLoader == null) {
            return Collections.emptyMap();
        }
        return this.cacheLoader.loadAll(keys);
    }

    @Override // org.ehcache.spi.loaderwriter.CacheLoaderWriter
    public void write(K key, V value) throws Exception {
        if (this.cacheWriter != null) {
            this.cacheWriter.write(cacheEntryFor(key, value));
        }
    }

    @Override // org.ehcache.spi.loaderwriter.CacheLoaderWriter
    public void delete(K key) throws Exception {
        if (this.cacheWriter != null) {
            this.cacheWriter.delete(key);
        }
    }

    @Override // org.ehcache.spi.loaderwriter.CacheLoaderWriter
    public void deleteAll(Iterable<? extends K> keys) throws Exception {
        if (this.cacheWriter != null) {
            Set<K> allKeys = new HashSet<>();
            for (K key : keys) {
                allKeys.add(key);
            }
            try {
                this.cacheWriter.deleteAll(allKeys);
            } catch (Exception e) {
                Map<?, Exception> failures = failures(allKeys, e);
                Set<?> successes = successes(keys, failures.keySet());
                throw new BulkCacheWritingException(failures, successes);
            }
        }
    }

    private Set<?> successes(Iterable<? extends K> keys, Set<?> failures) {
        HashSet hashSet = new HashSet();
        for (K key : keys) {
            if (!failures.contains(key)) {
                hashSet.add(key);
            }
        }
        return hashSet;
    }

    private Map<?, Exception> failures(Set<K> keys, Exception e) {
        HashMap map = new HashMap(keys.size());
        for (K key : keys) {
            map.put(key, e);
        }
        return map;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.ehcache.spi.loaderwriter.CacheLoaderWriter
    public void writeAll(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) throws Exception {
        if (this.cacheWriter != null) {
            Collection<Cache.Entry<? extends K, ? extends V>> toWrite = new ArrayList<>();
            for (Map.Entry<? extends K, ? extends V> entry : entries) {
                toWrite.add(cacheEntryFor(entry.getKey(), entry.getValue()));
            }
            try {
                this.cacheWriter.writeAll(toWrite);
            } catch (Exception e) {
                HashMap map = new HashMap();
                for (Cache.Entry<? extends K, ? extends V> entry2 : toWrite) {
                    map.put(entry2.getKey(), e);
                }
                Set<K> successes = new HashSet<>();
                for (Map.Entry<? extends K, ? extends V> entry3 : entries) {
                    K key = entry3.getKey();
                    if (!map.containsKey(key)) {
                        successes.add(key);
                    }
                }
                throw new BulkCacheWritingException(map, successes);
            }
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        try {
            if (this.cacheLoader instanceof Closeable) {
                this.cacheLoader.close();
            }
        } finally {
            if (this.cacheWriter instanceof Closeable) {
                this.cacheWriter.close();
            }
        }
    }

    private static <K, V> Cache.Entry<K, V> cacheEntryFor(K key, V value) {
        return new Entry(key, value);
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/Eh107CacheLoaderWriter$Entry.class */
    static class Entry<K, V> implements Cache.Entry<K, V> {
        private final K key;
        private final V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return this.key;
        }

        public V getValue() {
            return this.value;
        }

        public <T> T unwrap(Class<T> clazz) {
            throw new IllegalArgumentException();
        }

        public int hashCode() {
            return (this.key == null ? 0 : this.key.hashCode()) ^ (this.value == null ? 0 : this.value.hashCode());
        }

        public boolean equals(Object obj) {
            if (obj instanceof Entry) {
                Entry<?, ?> other = (Entry) obj;
                Object key1 = getKey();
                Object key2 = other.getKey();
                if (key1 == key2 || (key1 != null && key1.equals(key2))) {
                    Object value1 = getValue();
                    Object value2 = other.getValue();
                    if (value1 == value2) {
                        return true;
                    }
                    if (value1 != null && value1.equals(value2)) {
                        return true;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
    }
}
