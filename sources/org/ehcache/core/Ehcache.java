package org.ehcache.core;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import ch.qos.logback.core.pattern.parser.Parser;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.ehcache.Cache;
import org.ehcache.Status;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.CacheRuntimeConfiguration;
import org.ehcache.core.events.CacheEventDispatcher;
import org.ehcache.core.exceptions.ExceptionFactory;
import org.ehcache.core.internal.resilience.LoggingRobustResilienceStrategy;
import org.ehcache.core.internal.resilience.RecoveryCache;
import org.ehcache.core.internal.resilience.ResilienceStrategy;
import org.ehcache.core.internal.util.ValueSuppliers;
import org.ehcache.core.spi.LifeCycled;
import org.ehcache.core.spi.function.BiFunction;
import org.ehcache.core.spi.function.Function;
import org.ehcache.core.spi.function.NullaryFunction;
import org.ehcache.core.spi.store.Store;
import org.ehcache.core.spi.store.StoreAccessException;
import org.ehcache.core.statistics.BulkOps;
import org.ehcache.core.statistics.CacheOperationOutcomes;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expiry;
import org.ehcache.spi.loaderwriter.BulkCacheLoadingException;
import org.ehcache.spi.loaderwriter.BulkCacheWritingException;
import org.ehcache.spi.loaderwriter.CacheLoaderWriter;
import org.slf4j.Logger;
import org.terracotta.statistics.StatisticBuilder;
import org.terracotta.statistics.StatisticsManager;
import org.terracotta.statistics.jsr166e.LongAdder;
import org.terracotta.statistics.observer.OperationObserver;
import redis.clients.jedis.Protocol;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/Ehcache.class */
public class Ehcache<K, V> implements InternalCache<K, V> {
    private final StatusTransitioner statusTransitioner;
    private final Store<K, V> store;
    private final ResilienceStrategy<K, V> resilienceStrategy;
    private final EhcacheRuntimeConfiguration<K, V> runtimeConfiguration;
    private final Ehcache<K, V>.Jsr107CacheImpl jsr107Cache;
    protected final Logger logger;
    private final OperationObserver<CacheOperationOutcomes.GetOutcome> getObserver;
    private final OperationObserver<CacheOperationOutcomes.GetAllOutcome> getAllObserver;
    private final OperationObserver<CacheOperationOutcomes.PutOutcome> putObserver;
    private final OperationObserver<CacheOperationOutcomes.PutAllOutcome> putAllObserver;
    private final OperationObserver<CacheOperationOutcomes.RemoveOutcome> removeObserver;
    private final OperationObserver<CacheOperationOutcomes.RemoveAllOutcome> removeAllObserver;
    private final OperationObserver<CacheOperationOutcomes.ConditionalRemoveOutcome> conditionalRemoveObserver;
    private final OperationObserver<CacheOperationOutcomes.PutIfAbsentOutcome> putIfAbsentObserver;
    private final OperationObserver<CacheOperationOutcomes.ReplaceOutcome> replaceObserver;
    private final Map<BulkOps, LongAdder> bulkMethodEntries;
    private final OperationObserver<CacheOperationOutcomes.ClearOutcome> clearObserver;

    public Ehcache(CacheConfiguration<K, V> configuration, Store<K, V> store, CacheEventDispatcher<K, V> eventDispatcher, Logger logger) {
        this(new EhcacheRuntimeConfiguration(configuration), store, eventDispatcher, logger, new StatusTransitioner(logger));
    }

    Ehcache(EhcacheRuntimeConfiguration<K, V> runtimeConfiguration, Store<K, V> store, CacheEventDispatcher<K, V> eventDispatcher, Logger logger, StatusTransitioner statusTransitioner) {
        this.getObserver = StatisticBuilder.operation(CacheOperationOutcomes.GetOutcome.class).named(BeanUtil.PREFIX_GETTER_GET).of(this).tag("cache").build();
        this.getAllObserver = StatisticBuilder.operation(CacheOperationOutcomes.GetAllOutcome.class).named("getAll").of(this).tag("cache").build();
        this.putObserver = StatisticBuilder.operation(CacheOperationOutcomes.PutOutcome.class).named("put").of(this).tag("cache").build();
        this.putAllObserver = StatisticBuilder.operation(CacheOperationOutcomes.PutAllOutcome.class).named("putAll").of(this).tag("cache").build();
        this.removeObserver = StatisticBuilder.operation(CacheOperationOutcomes.RemoveOutcome.class).named(Protocol.SENTINEL_REMOVE).of(this).tag("cache").build();
        this.removeAllObserver = StatisticBuilder.operation(CacheOperationOutcomes.RemoveAllOutcome.class).named("removeAll").of(this).tag("cache").build();
        this.conditionalRemoveObserver = StatisticBuilder.operation(CacheOperationOutcomes.ConditionalRemoveOutcome.class).named("conditionalRemove").of(this).tag("cache").build();
        this.putIfAbsentObserver = StatisticBuilder.operation(CacheOperationOutcomes.PutIfAbsentOutcome.class).named("putIfAbsent").of(this).tag("cache").build();
        this.replaceObserver = StatisticBuilder.operation(CacheOperationOutcomes.ReplaceOutcome.class).named(Parser.REPLACE_CONVERTER_WORD).of(this).tag("cache").build();
        this.bulkMethodEntries = new EnumMap(BulkOps.class);
        this.clearObserver = StatisticBuilder.operation(CacheOperationOutcomes.ClearOutcome.class).named("clear").of(this).tag("cache").build();
        this.store = store;
        runtimeConfiguration.addCacheConfigurationListener(store.getConfigurationChangeListeners());
        StatisticsManager.associate(store).withParent(this);
        if (store instanceof RecoveryCache) {
            this.resilienceStrategy = new LoggingRobustResilienceStrategy(castToRecoveryCache(store));
        } else {
            this.resilienceStrategy = new LoggingRobustResilienceStrategy(recoveryCache(store));
        }
        this.runtimeConfiguration = runtimeConfiguration;
        runtimeConfiguration.addCacheConfigurationListener(eventDispatcher.getConfigurationChangeListeners());
        this.jsr107Cache = new Jsr107CacheImpl();
        this.logger = logger;
        this.statusTransitioner = statusTransitioner;
        BulkOps[] arr$ = BulkOps.values();
        for (BulkOps bulkOp : arr$) {
            this.bulkMethodEntries.put(bulkOp, new LongAdder());
        }
    }

    @Override // org.ehcache.core.InternalCache
    public Map<BulkOps, LongAdder> getBulkMethodEntries() {
        return this.bulkMethodEntries;
    }

    private RecoveryCache<K> castToRecoveryCache(Store<K, V> store) {
        return (RecoveryCache) store;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public V getNoLoader(K key) {
        return get(key);
    }

    @Override // org.ehcache.Cache
    public V get(K key) {
        this.getObserver.begin();
        this.statusTransitioner.checkAvailable();
        checkNonNull(key);
        try {
            Store.ValueHolder<V> valueHolder = this.store.get(key);
            if (valueHolder == null) {
                this.getObserver.end(CacheOperationOutcomes.GetOutcome.MISS);
                return null;
            }
            this.getObserver.end(CacheOperationOutcomes.GetOutcome.HIT);
            return valueHolder.value();
        } catch (StoreAccessException e) {
            try {
                V failure = this.resilienceStrategy.getFailure(key, e);
                this.getObserver.end(CacheOperationOutcomes.GetOutcome.FAILURE);
                return failure;
            } catch (Throwable th) {
                this.getObserver.end(CacheOperationOutcomes.GetOutcome.FAILURE);
                throw th;
            }
        }
    }

    @Override // org.ehcache.Cache
    public void put(K key, V value) {
        this.putObserver.begin();
        this.statusTransitioner.checkAvailable();
        checkNonNull(key, value);
        try {
            Store.PutStatus status = this.store.put(key, value);
            switch (status) {
                case PUT:
                    this.putObserver.end(CacheOperationOutcomes.PutOutcome.PUT);
                    break;
                case UPDATE:
                    this.putObserver.end(CacheOperationOutcomes.PutOutcome.UPDATED);
                    break;
                case NOOP:
                    this.putObserver.end(CacheOperationOutcomes.PutOutcome.NOOP);
                    break;
                default:
                    throw new AssertionError("Invalid Status.");
            }
        } catch (StoreAccessException e) {
            try {
                this.resilienceStrategy.putFailure(key, value, e);
                this.putObserver.end(CacheOperationOutcomes.PutOutcome.FAILURE);
            } catch (Throwable th) {
                this.putObserver.end(CacheOperationOutcomes.PutOutcome.FAILURE);
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean newValueAlreadyExpired(K key, V oldValue, V newValue) {
        Duration duration;
        if (newValue == null) {
            return false;
        }
        if (oldValue == null) {
            try {
                duration = this.runtimeConfiguration.getExpiry().getExpiryForCreation(key, newValue);
            } catch (RuntimeException re) {
                this.logger.error("Expiry computation caused an exception - Expiry duration will be 0 ", (Throwable) re);
                return true;
            }
        } else {
            try {
                duration = this.runtimeConfiguration.getExpiry().getExpiryForUpdate(key, ValueSuppliers.supplierOf(oldValue), newValue);
            } catch (RuntimeException re2) {
                this.logger.error("Expiry computation caused an exception - Expiry duration will be 0 ", (Throwable) re2);
                return true;
            }
        }
        return Duration.ZERO.equals(duration);
    }

    @Override // org.ehcache.Cache
    public boolean containsKey(K key) {
        this.statusTransitioner.checkAvailable();
        checkNonNull(key);
        try {
            return this.store.containsKey(key);
        } catch (StoreAccessException e) {
            return this.resilienceStrategy.containsKeyFailure(key, e);
        }
    }

    @Override // org.ehcache.Cache
    public void remove(K key) {
        removeInternal(key);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean removeInternal(K key) {
        this.removeObserver.begin();
        this.statusTransitioner.checkAvailable();
        checkNonNull(key);
        boolean removed = false;
        try {
            removed = this.store.remove(key);
            if (removed) {
                this.removeObserver.end(CacheOperationOutcomes.RemoveOutcome.SUCCESS);
            } else {
                this.removeObserver.end(CacheOperationOutcomes.RemoveOutcome.NOOP);
            }
        } catch (StoreAccessException e) {
            try {
                this.resilienceStrategy.removeFailure(key, e);
                this.removeObserver.end(CacheOperationOutcomes.RemoveOutcome.FAILURE);
            } catch (Throwable th) {
                this.removeObserver.end(CacheOperationOutcomes.RemoveOutcome.FAILURE);
                throw th;
            }
        }
        return removed;
    }

    @Override // org.ehcache.Cache
    public void clear() {
        this.clearObserver.begin();
        this.statusTransitioner.checkAvailable();
        try {
            this.store.clear();
            this.clearObserver.end(CacheOperationOutcomes.ClearOutcome.SUCCESS);
        } catch (StoreAccessException e) {
            this.clearObserver.end(CacheOperationOutcomes.ClearOutcome.FAILURE);
            this.resilienceStrategy.clearFailure(e);
        }
    }

    @Override // java.lang.Iterable
    public Iterator<Cache.Entry<K, V>> iterator() {
        this.statusTransitioner.checkAvailable();
        return new CacheEntryIterator(false);
    }

    @Override // org.ehcache.Cache
    public Map<K, V> getAll(Set<? extends K> keys) throws BulkCacheLoadingException {
        return getAllInternal(keys, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Map<K, V> getAllInternal(Set<? extends K> keys, boolean includeNulls) throws BulkCacheLoadingException {
        this.getAllObserver.begin();
        this.statusTransitioner.checkAvailable();
        checkNonNullContent(keys);
        if (keys.isEmpty()) {
            this.getAllObserver.end(CacheOperationOutcomes.GetAllOutcome.SUCCESS);
            return Collections.emptyMap();
        }
        Map<K, V> result = new HashMap<>();
        try {
            Map<K, Store.ValueHolder<V>> computedMap = this.store.bulkComputeIfAbsent(keys, new GetAllFunction());
            int hits = 0;
            int keyCount = 0;
            for (Map.Entry<K, Store.ValueHolder<V>> entry : computedMap.entrySet()) {
                keyCount++;
                if (entry.getValue() != null) {
                    result.put(entry.getKey(), entry.getValue().value());
                    hits++;
                } else if (includeNulls) {
                    result.put(entry.getKey(), null);
                }
            }
            addBulkMethodEntriesCount(BulkOps.GET_ALL_HITS, hits);
            addBulkMethodEntriesCount(BulkOps.GET_ALL_MISS, keyCount - hits);
            this.getAllObserver.end(CacheOperationOutcomes.GetAllOutcome.SUCCESS);
            return result;
        } catch (StoreAccessException e) {
            try {
                Map<K, V> allFailure = this.resilienceStrategy.getAllFailure(keys, e);
                this.getAllObserver.end(CacheOperationOutcomes.GetAllOutcome.FAILURE);
                return allFailure;
            } catch (Throwable th) {
                this.getAllObserver.end(CacheOperationOutcomes.GetAllOutcome.FAILURE);
                throw th;
            }
        }
    }

    LinkedHashSet<Map.Entry<? extends K, ? extends V>> nullValuesForKeys(Iterable<? extends K> keys) {
        LinkedHashSet<Map.Entry<? extends K, ? extends V>> entries = new LinkedHashSet<>();
        for (K key : keys) {
            entries.add(new AbstractMap.SimpleEntry(key, null));
        }
        return entries;
    }

    @Override // org.ehcache.Cache
    public void putAll(Map<? extends K, ? extends V> entries) throws BulkCacheWritingException {
        this.putAllObserver.begin();
        this.statusTransitioner.checkAvailable();
        checkNonNull(entries);
        if (entries.isEmpty()) {
            this.putAllObserver.end(CacheOperationOutcomes.PutAllOutcome.SUCCESS);
            return;
        }
        Map<K, V> entriesToRemap = new HashMap<>();
        for (Map.Entry<? extends K, ? extends V> entry : entries.entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null) {
                throw new NullPointerException();
            }
            entriesToRemap.put(entry.getKey(), entry.getValue());
        }
        try {
            PutAllFunction<K, V> putAllFunction = new PutAllFunction<>(this.logger, entriesToRemap, this.runtimeConfiguration.getExpiry());
            this.store.bulkCompute(entries.keySet(), putAllFunction);
            addBulkMethodEntriesCount(BulkOps.PUT_ALL, putAllFunction.getActualPutCount().get());
            this.putAllObserver.end(CacheOperationOutcomes.PutAllOutcome.SUCCESS);
        } catch (StoreAccessException e) {
            try {
                this.resilienceStrategy.putAllFailure(entries, e);
                this.putAllObserver.end(CacheOperationOutcomes.PutAllOutcome.FAILURE);
            } catch (Throwable th) {
                this.putAllObserver.end(CacheOperationOutcomes.PutAllOutcome.FAILURE);
                throw th;
            }
        }
    }

    @Override // org.ehcache.Cache
    public void removeAll(Set<? extends K> keys) throws BulkCacheWritingException {
        this.removeAllObserver.begin();
        this.statusTransitioner.checkAvailable();
        checkNonNull(keys);
        if (keys.isEmpty()) {
            this.removeAllObserver.end(CacheOperationOutcomes.RemoveAllOutcome.SUCCESS);
            return;
        }
        for (K key : keys) {
            if (key == null) {
                throw new NullPointerException();
            }
        }
        try {
            RemoveAllFunction<K, V> removeAllFunction = new RemoveAllFunction<>();
            this.store.bulkCompute(keys, removeAllFunction);
            addBulkMethodEntriesCount(BulkOps.REMOVE_ALL, removeAllFunction.getActualRemoveCount().get());
            this.removeAllObserver.end(CacheOperationOutcomes.RemoveAllOutcome.SUCCESS);
        } catch (StoreAccessException e) {
            try {
                this.resilienceStrategy.removeAllFailure(keys, e);
                this.removeAllObserver.end(CacheOperationOutcomes.RemoveAllOutcome.FAILURE);
            } catch (Throwable th) {
                this.removeAllObserver.end(CacheOperationOutcomes.RemoveAllOutcome.FAILURE);
                throw th;
            }
        }
    }

    @Override // org.ehcache.Cache
    public V putIfAbsent(K key, V value) {
        this.putIfAbsentObserver.begin();
        this.statusTransitioner.checkAvailable();
        checkNonNull(key, value);
        try {
            Store.ValueHolder<V> inCache = this.store.putIfAbsent(key, value);
            boolean absent = inCache == null;
            if (absent) {
                this.putIfAbsentObserver.end(CacheOperationOutcomes.PutIfAbsentOutcome.PUT);
                return null;
            }
            this.putIfAbsentObserver.end(CacheOperationOutcomes.PutIfAbsentOutcome.HIT);
            return inCache.value();
        } catch (StoreAccessException e) {
            try {
                V vPutIfAbsentFailure = this.resilienceStrategy.putIfAbsentFailure(key, value, null, e, false);
                this.putIfAbsentObserver.end(CacheOperationOutcomes.PutIfAbsentOutcome.FAILURE);
                return vPutIfAbsentFailure;
            } catch (Throwable th) {
                this.putIfAbsentObserver.end(CacheOperationOutcomes.PutIfAbsentOutcome.FAILURE);
                throw th;
            }
        }
    }

    @Override // org.ehcache.Cache
    public boolean remove(K key, V value) {
        this.conditionalRemoveObserver.begin();
        this.statusTransitioner.checkAvailable();
        checkNonNull(key, value);
        boolean removed = false;
        try {
            Store.RemoveStatus status = this.store.remove(key, value);
            switch (status) {
                case REMOVED:
                    removed = true;
                    this.conditionalRemoveObserver.end(CacheOperationOutcomes.ConditionalRemoveOutcome.SUCCESS);
                    break;
                case KEY_MISSING:
                    this.conditionalRemoveObserver.end(CacheOperationOutcomes.ConditionalRemoveOutcome.FAILURE_KEY_MISSING);
                    break;
                case KEY_PRESENT:
                    this.conditionalRemoveObserver.end(CacheOperationOutcomes.ConditionalRemoveOutcome.FAILURE_KEY_PRESENT);
                    break;
                default:
                    throw new AssertionError("Invalid Status.");
            }
            return removed;
        } catch (StoreAccessException e) {
            try {
                boolean zRemoveFailure = this.resilienceStrategy.removeFailure((ResilienceStrategy<K, V>) key, (K) value, e, false);
                this.conditionalRemoveObserver.end(CacheOperationOutcomes.ConditionalRemoveOutcome.FAILURE);
                return zRemoveFailure;
            } catch (Throwable th) {
                this.conditionalRemoveObserver.end(CacheOperationOutcomes.ConditionalRemoveOutcome.FAILURE);
                throw th;
            }
        }
    }

    @Override // org.ehcache.Cache
    public V replace(K key, V value) {
        this.replaceObserver.begin();
        this.statusTransitioner.checkAvailable();
        checkNonNull(key, value);
        try {
            Store.ValueHolder<V> old = this.store.replace(key, value);
            if (old != null) {
                this.replaceObserver.end(CacheOperationOutcomes.ReplaceOutcome.HIT);
            } else {
                this.replaceObserver.end(CacheOperationOutcomes.ReplaceOutcome.MISS_NOT_PRESENT);
            }
            if (old == null) {
                return null;
            }
            return old.value();
        } catch (StoreAccessException e) {
            try {
                V vReplaceFailure = this.resilienceStrategy.replaceFailure(key, value, e);
                this.replaceObserver.end(CacheOperationOutcomes.ReplaceOutcome.FAILURE);
                return vReplaceFailure;
            } catch (Throwable th) {
                this.replaceObserver.end(CacheOperationOutcomes.ReplaceOutcome.FAILURE);
                throw th;
            }
        }
    }

    @Override // org.ehcache.Cache
    public boolean replace(K key, V oldValue, V newValue) {
        this.replaceObserver.begin();
        this.statusTransitioner.checkAvailable();
        checkNonNull(key, oldValue, newValue);
        boolean success = false;
        try {
            Store.ReplaceStatus status = this.store.replace(key, oldValue, newValue);
            switch (status) {
                case HIT:
                    success = true;
                    this.replaceObserver.end(CacheOperationOutcomes.ReplaceOutcome.HIT);
                    break;
                case MISS_PRESENT:
                    this.replaceObserver.end(CacheOperationOutcomes.ReplaceOutcome.MISS_PRESENT);
                    break;
                case MISS_NOT_PRESENT:
                    this.replaceObserver.end(CacheOperationOutcomes.ReplaceOutcome.MISS_NOT_PRESENT);
                    break;
                default:
                    throw new AssertionError("Invalid Status.");
            }
            return success;
        } catch (StoreAccessException e) {
            try {
                boolean zReplaceFailure = this.resilienceStrategy.replaceFailure((ResilienceStrategy<K, V>) key, (Object) oldValue, (Object) newValue, e, false);
                this.replaceObserver.end(CacheOperationOutcomes.ReplaceOutcome.FAILURE);
                return zReplaceFailure;
            } catch (Throwable th) {
                this.replaceObserver.end(CacheOperationOutcomes.ReplaceOutcome.FAILURE);
                throw th;
            }
        }
    }

    @Override // org.ehcache.Cache
    public CacheRuntimeConfiguration<K, V> getRuntimeConfiguration() {
        return this.runtimeConfiguration;
    }

    @Override // org.ehcache.UserManagedCache
    public void init() {
        this.statusTransitioner.init().succeeded();
    }

    @Override // org.ehcache.UserManagedCache, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.statusTransitioner.close().succeeded();
    }

    @Override // org.ehcache.UserManagedCache
    public Status getStatus() {
        return this.statusTransitioner.currentStatus();
    }

    @Override // org.ehcache.core.InternalCache
    public void addHook(LifeCycled hook) {
        this.statusTransitioner.addHook(hook);
    }

    void removeHook(LifeCycled hook) {
        this.statusTransitioner.removeHook(hook);
    }

    private static void checkNonNull(Object thing) {
        if (thing == null) {
            throw new NullPointerException();
        }
    }

    private static void checkNonNull(Object... things) {
        for (Object thing : things) {
            checkNonNull(thing);
        }
    }

    private void checkNonNullContent(Collection<?> collectionOfThings) {
        checkNonNull(collectionOfThings);
        for (Object thing : collectionOfThings) {
            checkNonNull(thing);
        }
    }

    private void addBulkMethodEntriesCount(BulkOps op, long count) {
        this.bulkMethodEntries.get(op).add(count);
    }

    @Override // org.ehcache.core.InternalCache
    public Jsr107Cache<K, V> getJsr107Cache() {
        return this.jsr107Cache;
    }

    @Override // org.ehcache.core.InternalCache
    public CacheLoaderWriter<? super K, V> getCacheLoaderWriter() {
        return null;
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/Ehcache$Jsr107CacheImpl.class */
    private final class Jsr107CacheImpl implements Jsr107Cache<K, V> {
        private Jsr107CacheImpl() {
        }

        @Override // org.ehcache.core.Jsr107Cache
        public void loadAll(Set<? extends K> keys, boolean replaceExistingValues, Function<Iterable<? extends K>, Map<K, V>> loadFunction) {
            if (keys.isEmpty()) {
                return;
            }
            if (replaceExistingValues) {
                loadAllReplace(keys, loadFunction);
            } else {
                loadAllAbsent(keys, loadFunction);
            }
        }

        @Override // org.ehcache.core.Jsr107Cache
        public Iterator<Cache.Entry<K, V>> specIterator() {
            return new SpecIterator(this, Ehcache.this.store);
        }

        @Override // org.ehcache.core.Jsr107Cache
        public V getNoLoader(K k) {
            return (V) Ehcache.this.getNoLoader(k);
        }

        @Override // org.ehcache.core.Jsr107Cache
        public Map<K, V> getAll(Set<? extends K> keys) {
            return Ehcache.this.getAllInternal(keys, false);
        }

        private void loadAllAbsent(Set<? extends K> keys, final Function<Iterable<? extends K>, Map<K, V>> loadFunction) {
            try {
                Ehcache.this.store.bulkComputeIfAbsent(keys, new Function<Iterable<? extends K>, Iterable<? extends Map.Entry<? extends K, ? extends V>>>() { // from class: org.ehcache.core.Ehcache.Jsr107CacheImpl.1
                    @Override // org.ehcache.core.spi.function.Function
                    public Iterable<? extends Map.Entry<? extends K, ? extends V>> apply(Iterable<? extends K> absentKeys) {
                        return Jsr107CacheImpl.this.cacheLoaderWriterLoadAllForKeys(absentKeys, loadFunction).entrySet();
                    }
                });
            } catch (StoreAccessException e) {
                throw ExceptionFactory.newCacheLoadingException(e);
            }
        }

        Map<K, V> cacheLoaderWriterLoadAllForKeys(Iterable<? extends K> keys, Function<Iterable<? extends K>, Map<K, V>> loadFunction) {
            try {
                Map<K, V> mapApply = loadFunction.apply(keys);
                Map<K, V> rv = new LinkedHashMap<>();
                for (K key : keys) {
                    rv.put(key, mapApply.get(key));
                }
                return rv;
            } catch (Exception e) {
                throw ExceptionFactory.newCacheLoadingException(e);
            }
        }

        private void loadAllReplace(Set<? extends K> keys, final Function<Iterable<? extends K>, Map<K, V>> loadFunction) {
            try {
                Ehcache.this.store.bulkCompute(keys, new Function<Iterable<? extends Map.Entry<? extends K, ? extends V>>, Iterable<? extends Map.Entry<? extends K, ? extends V>>>() { // from class: org.ehcache.core.Ehcache.Jsr107CacheImpl.2
                    @Override // org.ehcache.core.spi.function.Function
                    public Iterable<? extends Map.Entry<? extends K, ? extends V>> apply(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
                        ArrayList arrayList = new ArrayList();
                        for (Map.Entry<? extends K, ? extends V> entry : entries) {
                            arrayList.add(entry.getKey());
                        }
                        return Jsr107CacheImpl.this.cacheLoaderWriterLoadAllForKeys(arrayList, loadFunction).entrySet();
                    }
                });
            } catch (StoreAccessException e) {
                throw ExceptionFactory.newCacheLoadingException(e);
            }
        }

        @Override // org.ehcache.core.Jsr107Cache
        public void compute(K key, final BiFunction<? super K, ? super V, ? extends V> computeFunction, final NullaryFunction<Boolean> replaceEqual, NullaryFunction<Boolean> invokeWriter, final NullaryFunction<Boolean> withStatsAndEvents) {
            Ehcache.this.putObserver.begin();
            Ehcache.this.removeObserver.begin();
            Ehcache.this.getObserver.begin();
            try {
                BiFunction<K, V, V> fn = new BiFunction<K, V, V>() { // from class: org.ehcache.core.Ehcache.Jsr107CacheImpl.3
                    @Override // org.ehcache.core.spi.function.BiFunction
                    public V apply(K k, V v) {
                        if (v == null) {
                            Ehcache.this.getObserver.end(CacheOperationOutcomes.GetOutcome.MISS);
                        } else {
                            Ehcache.this.getObserver.end(CacheOperationOutcomes.GetOutcome.HIT);
                        }
                        V v2 = (V) computeFunction.apply(k, v);
                        if (v2 != v || ((Boolean) replaceEqual.apply()).booleanValue()) {
                            if (Ehcache.this.newValueAlreadyExpired(k, v, v2)) {
                                return null;
                            }
                            if (((Boolean) withStatsAndEvents.apply()).booleanValue()) {
                                if (v2 == null) {
                                    Ehcache.this.removeObserver.end(CacheOperationOutcomes.RemoveOutcome.SUCCESS);
                                } else {
                                    Ehcache.this.putObserver.end(CacheOperationOutcomes.PutOutcome.PUT);
                                }
                            }
                            return v2;
                        }
                        return v;
                    }
                };
                Ehcache.this.store.compute(key, fn, replaceEqual);
            } catch (StoreAccessException e) {
                throw new RuntimeException(e);
            }
        }

        @Override // org.ehcache.core.Jsr107Cache
        public V getAndRemove(K key) {
            Ehcache.this.getObserver.begin();
            Ehcache.this.removeObserver.begin();
            final AtomicReference<V> existingValue = new AtomicReference<>();
            try {
                Ehcache.this.store.compute(key, new BiFunction<K, V, V>() { // from class: org.ehcache.core.Ehcache.Jsr107CacheImpl.4
                    @Override // org.ehcache.core.spi.function.BiFunction
                    public V apply(K mappedKey, V mappedValue) {
                        existingValue.set(mappedValue);
                        return null;
                    }
                });
                V returnValue = existingValue.get();
                if (returnValue != null) {
                    Ehcache.this.getObserver.end(CacheOperationOutcomes.GetOutcome.HIT);
                    Ehcache.this.removeObserver.end(CacheOperationOutcomes.RemoveOutcome.SUCCESS);
                } else {
                    Ehcache.this.getObserver.end(CacheOperationOutcomes.GetOutcome.MISS);
                }
                return returnValue;
            } catch (StoreAccessException e) {
                Ehcache.this.getObserver.end(CacheOperationOutcomes.GetOutcome.FAILURE);
                Ehcache.this.removeObserver.end(CacheOperationOutcomes.RemoveOutcome.FAILURE);
                throw new RuntimeException(e);
            }
        }

        @Override // org.ehcache.core.Jsr107Cache
        public V getAndPut(K key, final V value) {
            Ehcache.this.getObserver.begin();
            Ehcache.this.putObserver.begin();
            final AtomicReference<V> existingValue = new AtomicReference<>();
            try {
                Ehcache.this.store.compute(key, new BiFunction<K, V, V>() { // from class: org.ehcache.core.Ehcache.Jsr107CacheImpl.5
                    @Override // org.ehcache.core.spi.function.BiFunction
                    public V apply(K k, V v) {
                        existingValue.set(v);
                        if (Ehcache.this.newValueAlreadyExpired(k, v, value)) {
                            return null;
                        }
                        return (V) value;
                    }
                });
                V returnValue = existingValue.get();
                if (returnValue != null) {
                    Ehcache.this.getObserver.end(CacheOperationOutcomes.GetOutcome.HIT);
                    Ehcache.this.putObserver.end(CacheOperationOutcomes.PutOutcome.UPDATED);
                } else {
                    Ehcache.this.getObserver.end(CacheOperationOutcomes.GetOutcome.MISS);
                    Ehcache.this.putObserver.end(CacheOperationOutcomes.PutOutcome.PUT);
                }
                return returnValue;
            } catch (StoreAccessException e) {
                Ehcache.this.getObserver.end(CacheOperationOutcomes.GetOutcome.FAILURE);
                Ehcache.this.putObserver.end(CacheOperationOutcomes.PutOutcome.FAILURE);
                throw new RuntimeException(e);
            }
        }

        @Override // org.ehcache.core.Jsr107Cache
        public boolean remove(K key) {
            return Ehcache.this.removeInternal(key);
        }

        @Override // org.ehcache.core.Jsr107Cache
        public void removeAll() {
            Store.Iterator<Cache.Entry<K, Store.ValueHolder<V>>> iterator = Ehcache.this.store.iterator();
            while (iterator.hasNext()) {
                try {
                    Cache.Entry<K, Store.ValueHolder<V>> next = iterator.next();
                    remove(next.getKey());
                } catch (StoreAccessException e) {
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/Ehcache$CacheEntryIterator.class */
    private class CacheEntryIterator implements Iterator<Cache.Entry<K, V>> {
        private final Store.Iterator<Cache.Entry<K, Store.ValueHolder<V>>> iterator;
        private final boolean quiet;
        private Cache.Entry<K, Store.ValueHolder<V>> current;
        private Cache.Entry<K, Store.ValueHolder<V>> next;
        private StoreAccessException nextException;

        public CacheEntryIterator(boolean quiet) {
            this.quiet = quiet;
            this.iterator = Ehcache.this.store.iterator();
            advance();
        }

        private void advance() {
            while (this.iterator.hasNext()) {
                try {
                    this.next = this.iterator.next();
                    if (Ehcache.this.getNoLoader(this.next.getKey()) != null) {
                        return;
                    }
                } catch (RuntimeException re) {
                    this.nextException = new StoreAccessException(re);
                    this.next = null;
                    return;
                } catch (StoreAccessException cae) {
                    this.nextException = cae;
                    this.next = null;
                    return;
                }
            }
            this.next = null;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            Ehcache.this.statusTransitioner.checkAvailable();
            return (this.nextException == null && this.next == null) ? false : true;
        }

        @Override // java.util.Iterator
        public Cache.Entry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            if (!this.quiet) {
                Ehcache.this.getObserver.begin();
            }
            if (this.nextException == null) {
                if (!this.quiet) {
                    Ehcache.this.getObserver.end(CacheOperationOutcomes.GetOutcome.HIT);
                }
                this.current = this.next;
                advance();
                return new ValueHolderBasedEntry(this.current);
            }
            if (!this.quiet) {
                Ehcache.this.getObserver.end(CacheOperationOutcomes.GetOutcome.FAILURE);
            }
            StoreAccessException cae = this.nextException;
            this.nextException = null;
            return Ehcache.this.resilienceStrategy.iteratorFailure(cae);
        }

        @Override // java.util.Iterator
        public void remove() {
            Ehcache.this.statusTransitioner.checkAvailable();
            if (this.current == null) {
                throw new IllegalStateException("No current element");
            }
            Ehcache.this.remove(this.current.getKey(), this.current.getValue().value());
            this.current = null;
        }
    }

    private static <K> RecoveryCache<K> recoveryCache(final Store<K, ?> store) {
        return new RecoveryCache<K>() { // from class: org.ehcache.core.Ehcache.1
            @Override // org.ehcache.core.internal.resilience.RecoveryCache
            public void obliterate() throws StoreAccessException {
                store.clear();
            }

            @Override // org.ehcache.core.internal.resilience.RecoveryCache
            public void obliterate(K key) throws StoreAccessException {
                store.remove(key);
            }

            @Override // org.ehcache.core.internal.resilience.RecoveryCache
            public void obliterate(Iterable<? extends K> keys) throws StoreAccessException {
                for (K key : keys) {
                    obliterate((AnonymousClass1) key);
                }
            }
        };
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/Ehcache$ValueHolderBasedEntry.class */
    private static class ValueHolderBasedEntry<K, V> implements Cache.Entry<K, V> {
        private final Cache.Entry<K, Store.ValueHolder<V>> storeEntry;

        ValueHolderBasedEntry(Cache.Entry<K, Store.ValueHolder<V>> storeEntry) {
            this.storeEntry = storeEntry;
        }

        @Override // org.ehcache.Cache.Entry
        public K getKey() {
            return this.storeEntry.getKey();
        }

        @Override // org.ehcache.Cache.Entry
        public V getValue() {
            return this.storeEntry.getValue().value();
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/Ehcache$PutAllFunction.class */
    public static class PutAllFunction<K, V> implements Function<Iterable<? extends Map.Entry<? extends K, ? extends V>>, Iterable<? extends Map.Entry<? extends K, ? extends V>>> {
        private final Logger logger;
        private final Map<K, V> entriesToRemap;
        private final Expiry<? super K, ? super V> expiry;
        private final AtomicInteger actualPutCount = new AtomicInteger();

        public PutAllFunction(Logger logger, Map<K, V> entriesToRemap, Expiry<? super K, ? super V> expiry) {
            this.logger = logger;
            this.entriesToRemap = entriesToRemap;
            this.expiry = expiry;
        }

        @Override // org.ehcache.core.spi.function.Function
        public Iterable<? extends Map.Entry<? extends K, ? extends V>> apply(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
            Map<K, V> mutations = new LinkedHashMap<>();
            for (Map.Entry<? extends K, ? extends V> entry : entries) {
                K key = entry.getKey();
                V existingValue = entry.getValue();
                V newValue = this.entriesToRemap.remove(key);
                if (newValueAlreadyExpired(key, existingValue, newValue)) {
                    mutations.put(key, null);
                } else {
                    this.actualPutCount.incrementAndGet();
                    mutations.put(key, newValue);
                }
            }
            return mutations.entrySet();
        }

        public Map<K, V> getEntriesToRemap() {
            return this.entriesToRemap;
        }

        private boolean newValueAlreadyExpired(K key, V oldValue, V newValue) {
            Duration duration;
            if (newValue == null) {
                return false;
            }
            if (oldValue == null) {
                try {
                    duration = this.expiry.getExpiryForCreation(key, newValue);
                } catch (RuntimeException re) {
                    this.logger.error("Expiry computation caused an exception - Expiry duration will be 0 ", (Throwable) re);
                    return true;
                }
            } else {
                try {
                    duration = this.expiry.getExpiryForUpdate(key, ValueSuppliers.supplierOf(oldValue), newValue);
                } catch (RuntimeException re2) {
                    this.logger.error("Expiry computation caused an exception - Expiry duration will be 0 ", (Throwable) re2);
                    return true;
                }
            }
            return Duration.ZERO.equals(duration);
        }

        public AtomicInteger getActualPutCount() {
            return this.actualPutCount;
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/Ehcache$RemoveAllFunction.class */
    public static class RemoveAllFunction<K, V> implements Function<Iterable<? extends Map.Entry<? extends K, ? extends V>>, Iterable<? extends Map.Entry<? extends K, ? extends V>>> {
        private final AtomicInteger actualRemoveCount = new AtomicInteger();

        @Override // org.ehcache.core.spi.function.Function
        public Iterable<? extends Map.Entry<? extends K, ? extends V>> apply(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
            Map<K, V> results = new LinkedHashMap<>();
            for (Map.Entry<? extends K, ? extends V> entry : entries) {
                K key = entry.getKey();
                V existingValue = entry.getValue();
                if (existingValue != null) {
                    this.actualRemoveCount.incrementAndGet();
                }
                results.put(key, null);
            }
            return results.entrySet();
        }

        public AtomicInteger getActualRemoveCount() {
            return this.actualRemoveCount;
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/Ehcache$GetAllFunction.class */
    public static class GetAllFunction<K, V> implements Function<Iterable<? extends K>, Iterable<? extends Map.Entry<? extends K, ? extends V>>> {
        @Override // org.ehcache.core.spi.function.Function
        public Iterable<? extends Map.Entry<? extends K, ? extends V>> apply(Iterable<? extends K> keys) {
            Map<K, V> computeResult = new LinkedHashMap<>();
            for (K key : keys) {
                computeResult.put(key, null);
            }
            return computeResult.entrySet();
        }
    }
}
