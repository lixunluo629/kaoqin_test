package org.ehcache.core.spi.store;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.ehcache.Cache;
import org.ehcache.ValueSupplier;
import org.ehcache.config.EvictionAdvisor;
import org.ehcache.config.ResourcePools;
import org.ehcache.config.ResourceType;
import org.ehcache.core.spi.function.BiFunction;
import org.ehcache.core.spi.function.Function;
import org.ehcache.core.spi.function.NullaryFunction;
import org.ehcache.core.spi.store.events.StoreEventSource;
import org.ehcache.expiry.Expiry;
import org.ehcache.spi.serialization.Serializer;
import org.ehcache.spi.service.PluralService;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/store/Store.class */
public interface Store<K, V> extends ConfigurationChangeSupport {

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/store/Store$Configuration.class */
    public interface Configuration<K, V> {
        Class<K> getKeyType();

        Class<V> getValueType();

        EvictionAdvisor<? super K, ? super V> getEvictionAdvisor();

        ClassLoader getClassLoader();

        Expiry<? super K, ? super V> getExpiry();

        ResourcePools getResourcePools();

        Serializer<K> getKeySerializer();

        Serializer<V> getValueSerializer();

        int getDispatcherConcurrency();
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/store/Store$Iterator.class */
    public interface Iterator<T> {
        boolean hasNext();

        T next() throws StoreAccessException;
    }

    @PluralService
    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/store/Store$Provider.class */
    public interface Provider extends Service {
        <K, V> Store<K, V> createStore(Configuration<K, V> configuration, ServiceConfiguration<?>... serviceConfigurationArr);

        void releaseStore(Store<?, ?> store);

        void initStore(Store<?, ?> store);

        int rank(Set<ResourceType<?>> set, Collection<ServiceConfiguration<?>> collection);
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/store/Store$PutStatus.class */
    public enum PutStatus {
        PUT,
        UPDATE,
        NOOP
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/store/Store$RemoveStatus.class */
    public enum RemoveStatus {
        REMOVED,
        KEY_PRESENT,
        KEY_MISSING
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/store/Store$ReplaceStatus.class */
    public enum ReplaceStatus {
        HIT,
        MISS_PRESENT,
        MISS_NOT_PRESENT
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/store/Store$ValueHolder.class */
    public interface ValueHolder<V> extends ValueSupplier<V> {
        public static final long NO_EXPIRE = -1;

        long creationTime(TimeUnit timeUnit);

        long expirationTime(TimeUnit timeUnit);

        boolean isExpired(long j, TimeUnit timeUnit);

        long lastAccessTime(TimeUnit timeUnit);

        float hitRate(long j, TimeUnit timeUnit);

        long hits();

        long getId();
    }

    ValueHolder<V> get(K k) throws StoreAccessException;

    boolean containsKey(K k) throws StoreAccessException;

    PutStatus put(K k, V v) throws StoreAccessException;

    ValueHolder<V> putIfAbsent(K k, V v) throws StoreAccessException;

    boolean remove(K k) throws StoreAccessException;

    RemoveStatus remove(K k, V v) throws StoreAccessException;

    ValueHolder<V> replace(K k, V v) throws StoreAccessException;

    ReplaceStatus replace(K k, V v, V v2) throws StoreAccessException;

    void clear() throws StoreAccessException;

    StoreEventSource<K, V> getStoreEventSource();

    Iterator<Cache.Entry<K, ValueHolder<V>>> iterator();

    ValueHolder<V> compute(K k, BiFunction<? super K, ? super V, ? extends V> biFunction) throws StoreAccessException;

    ValueHolder<V> compute(K k, BiFunction<? super K, ? super V, ? extends V> biFunction, NullaryFunction<Boolean> nullaryFunction) throws StoreAccessException;

    ValueHolder<V> computeIfAbsent(K k, Function<? super K, ? extends V> function) throws StoreAccessException;

    Map<K, ValueHolder<V>> bulkCompute(Set<? extends K> set, Function<Iterable<? extends Map.Entry<? extends K, ? extends V>>, Iterable<? extends Map.Entry<? extends K, ? extends V>>> function) throws StoreAccessException;

    Map<K, ValueHolder<V>> bulkCompute(Set<? extends K> set, Function<Iterable<? extends Map.Entry<? extends K, ? extends V>>, Iterable<? extends Map.Entry<? extends K, ? extends V>>> function, NullaryFunction<Boolean> nullaryFunction) throws StoreAccessException;

    Map<K, ValueHolder<V>> bulkComputeIfAbsent(Set<? extends K> set, Function<Iterable<? extends K>, Iterable<? extends Map.Entry<? extends K, ? extends V>>> function) throws StoreAccessException;
}
