package org.ehcache.impl.internal.store.basic;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.ehcache.Cache;
import org.ehcache.core.CacheConfigurationChangeListener;
import org.ehcache.core.spi.function.BiFunction;
import org.ehcache.core.spi.function.Function;
import org.ehcache.core.spi.function.NullaryFunction;
import org.ehcache.core.spi.store.Store;
import org.ehcache.core.spi.store.StoreAccessException;
import org.ehcache.core.spi.store.events.StoreEventFilter;
import org.ehcache.core.spi.store.events.StoreEventListener;
import org.ehcache.core.spi.store.events.StoreEventSource;
import org.ehcache.core.spi.store.tiering.AuthoritativeTier;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/basic/NopStore.class */
public class NopStore<K, V> implements AuthoritativeTier<K, V> {
    @Override // org.ehcache.core.spi.store.ConfigurationChangeSupport
    public List<CacheConfigurationChangeListener> getConfigurationChangeListeners() {
        return Collections.emptyList();
    }

    @Override // org.ehcache.core.spi.store.tiering.AuthoritativeTier
    public Store.ValueHolder<V> getAndFault(K key) throws StoreAccessException {
        return null;
    }

    @Override // org.ehcache.core.spi.store.tiering.AuthoritativeTier
    public Store.ValueHolder<V> computeIfAbsentAndFault(K key, Function<? super K, ? extends V> mappingFunction) throws StoreAccessException {
        return null;
    }

    @Override // org.ehcache.core.spi.store.tiering.AuthoritativeTier
    public boolean flush(K key, Store.ValueHolder<V> valueHolder) {
        return false;
    }

    @Override // org.ehcache.core.spi.store.tiering.AuthoritativeTier
    public void setInvalidationValve(AuthoritativeTier.InvalidationValve valve) {
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.ValueHolder<V> get(K key) throws StoreAccessException {
        return null;
    }

    @Override // org.ehcache.core.spi.store.Store
    public boolean containsKey(K key) throws StoreAccessException {
        return false;
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.PutStatus put(K key, V value) throws StoreAccessException {
        return Store.PutStatus.PUT;
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.ValueHolder<V> putIfAbsent(K key, V value) throws StoreAccessException {
        return EmptyValueHolder.empty();
    }

    @Override // org.ehcache.core.spi.store.Store
    public boolean remove(K key) throws StoreAccessException {
        return false;
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.RemoveStatus remove(K key, V value) throws StoreAccessException {
        return Store.RemoveStatus.KEY_MISSING;
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.ValueHolder<V> replace(K key, V value) throws StoreAccessException {
        return null;
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.ReplaceStatus replace(K key, V oldValue, V newValue) throws StoreAccessException {
        return Store.ReplaceStatus.MISS_NOT_PRESENT;
    }

    @Override // org.ehcache.core.spi.store.Store
    public void clear() throws StoreAccessException {
    }

    @Override // org.ehcache.core.spi.store.Store
    public StoreEventSource<K, V> getStoreEventSource() {
        return new StoreEventSource<K, V>() { // from class: org.ehcache.impl.internal.store.basic.NopStore.1
            @Override // org.ehcache.core.spi.store.events.StoreEventSource
            public void addEventListener(StoreEventListener<K, V> eventListener) {
            }

            @Override // org.ehcache.core.spi.store.events.StoreEventSource
            public void removeEventListener(StoreEventListener<K, V> eventListener) {
            }

            @Override // org.ehcache.core.spi.store.events.StoreEventSource
            public void addEventFilter(StoreEventFilter<K, V> eventFilter) {
            }

            @Override // org.ehcache.core.spi.store.events.StoreEventSource
            public void setEventOrdering(boolean ordering) {
            }

            @Override // org.ehcache.core.spi.store.events.StoreEventSource
            public boolean isEventOrdering() {
                return false;
            }
        };
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.Iterator<Cache.Entry<K, Store.ValueHolder<V>>> iterator() {
        return new Store.Iterator<Cache.Entry<K, Store.ValueHolder<V>>>() { // from class: org.ehcache.impl.internal.store.basic.NopStore.2
            @Override // org.ehcache.core.spi.store.Store.Iterator
            public boolean hasNext() {
                return false;
            }

            @Override // org.ehcache.core.spi.store.Store.Iterator
            public Cache.Entry<K, Store.ValueHolder<V>> next() throws StoreAccessException {
                return null;
            }
        };
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.ValueHolder<V> compute(K key, BiFunction<? super K, ? super V, ? extends V> mappingFunction) throws StoreAccessException {
        return EmptyValueHolder.empty();
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.ValueHolder<V> compute(K key, BiFunction<? super K, ? super V, ? extends V> mappingFunction, NullaryFunction<Boolean> replaceEqual) throws StoreAccessException {
        return null;
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.ValueHolder<V> computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) throws StoreAccessException {
        return null;
    }

    @Override // org.ehcache.core.spi.store.Store
    public Map<K, Store.ValueHolder<V>> bulkCompute(Set<? extends K> keys, Function<Iterable<? extends Map.Entry<? extends K, ? extends V>>, Iterable<? extends Map.Entry<? extends K, ? extends V>>> remappingFunction) throws StoreAccessException {
        return bulkCompute(keys, remappingFunction, null);
    }

    @Override // org.ehcache.core.spi.store.Store
    public Map<K, Store.ValueHolder<V>> bulkCompute(Set<? extends K> keys, Function<Iterable<? extends Map.Entry<? extends K, ? extends V>>, Iterable<? extends Map.Entry<? extends K, ? extends V>>> remappingFunction, NullaryFunction<Boolean> replaceEqual) throws StoreAccessException {
        Map<K, Store.ValueHolder<V>> map = new HashMap<>(keys.size());
        for (K key : keys) {
            map.put(key, EmptyValueHolder.empty());
        }
        return map;
    }

    @Override // org.ehcache.core.spi.store.Store
    public Map<K, Store.ValueHolder<V>> bulkComputeIfAbsent(Set<? extends K> keys, Function<Iterable<? extends K>, Iterable<? extends Map.Entry<? extends K, ? extends V>>> mappingFunction) throws StoreAccessException {
        Map<K, Store.ValueHolder<V>> map = new HashMap<>(keys.size());
        for (K key : keys) {
            map.put(key, EmptyValueHolder.empty());
        }
        return map;
    }
}
