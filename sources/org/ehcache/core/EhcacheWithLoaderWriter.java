package org.ehcache.core;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import ch.qos.logback.core.pattern.parser.Parser;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.ehcache.Cache;
import org.ehcache.Status;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.CacheRuntimeConfiguration;
import org.ehcache.core.events.CacheEventDispatcher;
import org.ehcache.core.exceptions.ExceptionFactory;
import org.ehcache.core.exceptions.StorePassThroughException;
import org.ehcache.core.internal.resilience.LoggingRobustResilienceStrategy;
import org.ehcache.core.internal.resilience.RecoveryCache;
import org.ehcache.core.internal.resilience.ResilienceStrategy;
import org.ehcache.core.internal.util.Functions;
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
import org.ehcache.spi.loaderwriter.BulkCacheLoadingException;
import org.ehcache.spi.loaderwriter.BulkCacheWritingException;
import org.ehcache.spi.loaderwriter.CacheLoaderWriter;
import org.ehcache.spi.loaderwriter.CacheLoadingException;
import org.ehcache.spi.loaderwriter.CacheWritingException;
import org.slf4j.Logger;
import org.terracotta.statistics.StatisticBuilder;
import org.terracotta.statistics.StatisticsManager;
import org.terracotta.statistics.jsr166e.LongAdder;
import org.terracotta.statistics.observer.OperationObserver;
import redis.clients.jedis.Protocol;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/EhcacheWithLoaderWriter.class */
public class EhcacheWithLoaderWriter<K, V> implements InternalCache<K, V> {
    private final StatusTransitioner statusTransitioner;
    private final Store<K, V> store;
    private final CacheLoaderWriter<? super K, V> cacheLoaderWriter;
    private final ResilienceStrategy<K, V> resilienceStrategy;
    private final EhcacheRuntimeConfiguration<K, V> runtimeConfiguration;
    private final EhcacheWithLoaderWriter<K, V>.Jsr107CacheImpl jsr107Cache;
    private final boolean useLoaderInAtomics;
    protected final Logger logger;
    private final OperationObserver<CacheOperationOutcomes.GetOutcome> getObserver;
    private final OperationObserver<CacheOperationOutcomes.GetAllOutcome> getAllObserver;
    private final OperationObserver<CacheOperationOutcomes.PutOutcome> putObserver;
    private final OperationObserver<CacheOperationOutcomes.PutAllOutcome> putAllObserver;
    private final OperationObserver<CacheOperationOutcomes.RemoveOutcome> removeObserver;
    private final OperationObserver<CacheOperationOutcomes.RemoveAllOutcome> removeAllObserver;
    private final OperationObserver<CacheOperationOutcomes.ConditionalRemoveOutcome> conditionalRemoveObserver;
    private final OperationObserver<CacheOperationOutcomes.CacheLoadingOutcome> cacheLoadingObserver;
    private final OperationObserver<CacheOperationOutcomes.PutIfAbsentOutcome> putIfAbsentObserver;
    private final OperationObserver<CacheOperationOutcomes.ReplaceOutcome> replaceObserver;
    private final Map<BulkOps, LongAdder> bulkMethodEntries;
    private static final NullaryFunction<Boolean> REPLACE_FALSE = new NullaryFunction<Boolean>() { // from class: org.ehcache.core.EhcacheWithLoaderWriter.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.ehcache.core.spi.function.NullaryFunction
        public Boolean apply() {
            return Boolean.FALSE;
        }
    };

    public EhcacheWithLoaderWriter(CacheConfiguration<K, V> configuration, Store<K, V> store, CacheLoaderWriter<? super K, V> cacheLoaderWriter, CacheEventDispatcher<K, V> eventDispatcher, Logger logger) {
        this(configuration, store, cacheLoaderWriter, eventDispatcher, true, logger);
    }

    EhcacheWithLoaderWriter(CacheConfiguration<K, V> runtimeConfiguration, Store<K, V> store, CacheLoaderWriter<? super K, V> cacheLoaderWriter, CacheEventDispatcher<K, V> eventDispatcher, boolean useLoaderInAtomics, Logger logger) {
        this(new EhcacheRuntimeConfiguration(runtimeConfiguration), store, cacheLoaderWriter, eventDispatcher, useLoaderInAtomics, logger, new StatusTransitioner(logger));
    }

    EhcacheWithLoaderWriter(EhcacheRuntimeConfiguration<K, V> runtimeConfiguration, Store<K, V> store, CacheLoaderWriter<? super K, V> cacheLoaderWriter, CacheEventDispatcher<K, V> eventDispatcher, boolean useLoaderInAtomics, Logger logger, StatusTransitioner statusTransitioner) {
        this.getObserver = StatisticBuilder.operation(CacheOperationOutcomes.GetOutcome.class).named(BeanUtil.PREFIX_GETTER_GET).of(this).tag("cache").build();
        this.getAllObserver = StatisticBuilder.operation(CacheOperationOutcomes.GetAllOutcome.class).named("getAll").of(this).tag("cache").build();
        this.putObserver = StatisticBuilder.operation(CacheOperationOutcomes.PutOutcome.class).named("put").of(this).tag("cache").build();
        this.putAllObserver = StatisticBuilder.operation(CacheOperationOutcomes.PutAllOutcome.class).named("putAll").of(this).tag("cache").build();
        this.removeObserver = StatisticBuilder.operation(CacheOperationOutcomes.RemoveOutcome.class).named(Protocol.SENTINEL_REMOVE).of(this).tag("cache").build();
        this.removeAllObserver = StatisticBuilder.operation(CacheOperationOutcomes.RemoveAllOutcome.class).named("removeAll").of(this).tag("cache").build();
        this.conditionalRemoveObserver = StatisticBuilder.operation(CacheOperationOutcomes.ConditionalRemoveOutcome.class).named("conditionalRemove").of(this).tag("cache").build();
        this.cacheLoadingObserver = StatisticBuilder.operation(CacheOperationOutcomes.CacheLoadingOutcome.class).named("cacheLoading").of(this).tag("cache").build();
        this.putIfAbsentObserver = StatisticBuilder.operation(CacheOperationOutcomes.PutIfAbsentOutcome.class).named("putIfAbsent").of(this).tag("cache").build();
        this.replaceObserver = StatisticBuilder.operation(CacheOperationOutcomes.ReplaceOutcome.class).named(Parser.REPLACE_CONVERTER_WORD).of(this).tag("cache").build();
        this.bulkMethodEntries = new EnumMap(BulkOps.class);
        this.store = store;
        runtimeConfiguration.addCacheConfigurationListener(store.getConfigurationChangeListeners());
        StatisticsManager.associate(store).withParent(this);
        if (cacheLoaderWriter == null) {
            throw new NullPointerException("CacheLoaderWriter cannot be null.");
        }
        this.cacheLoaderWriter = cacheLoaderWriter;
        if (store instanceof RecoveryCache) {
            this.resilienceStrategy = new LoggingRobustResilienceStrategy(castToRecoveryCache(store));
        } else {
            this.resilienceStrategy = new LoggingRobustResilienceStrategy(recoveryCache(store));
        }
        this.runtimeConfiguration = runtimeConfiguration;
        runtimeConfiguration.addCacheConfigurationListener(eventDispatcher.getConfigurationChangeListeners());
        this.jsr107Cache = new Jsr107CacheImpl();
        this.useLoaderInAtomics = useLoaderInAtomics;
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
    public V get(K k) throws CacheLoadingException {
        this.getObserver.begin();
        this.statusTransitioner.checkAvailable();
        checkNonNull(k);
        Function<? super K, ? extends V> functionMemoize = Functions.memoize(new Function<K, V>() { // from class: org.ehcache.core.EhcacheWithLoaderWriter.2
            @Override // org.ehcache.core.spi.function.Function
            public V apply(K k2) {
                try {
                    EhcacheWithLoaderWriter.this.cacheLoadingObserver.begin();
                    V v = (V) EhcacheWithLoaderWriter.this.cacheLoaderWriter.load(k2);
                    EhcacheWithLoaderWriter.this.cacheLoadingObserver.end(CacheOperationOutcomes.CacheLoadingOutcome.SUCCESS);
                    return v;
                } catch (Exception e) {
                    EhcacheWithLoaderWriter.this.cacheLoadingObserver.end(CacheOperationOutcomes.CacheLoadingOutcome.FAILURE);
                    throw new StorePassThroughException(ExceptionFactory.newCacheLoadingException(e));
                }
            }
        });
        try {
            Store.ValueHolder<V> valueHolderComputeIfAbsent = this.store.computeIfAbsent(k, functionMemoize);
            if (valueHolderComputeIfAbsent == null) {
                this.getObserver.end(CacheOperationOutcomes.GetOutcome.MISS);
                return null;
            }
            this.getObserver.end(CacheOperationOutcomes.GetOutcome.HIT);
            return valueHolderComputeIfAbsent.value();
        } catch (StoreAccessException e) {
            try {
                try {
                    V v = (V) this.resilienceStrategy.getFailure((ResilienceStrategy<K, V>) k, (K) functionMemoize.apply(k), e);
                    this.getObserver.end(CacheOperationOutcomes.GetOutcome.FAILURE);
                    return v;
                } catch (StorePassThroughException e2) {
                    V failure = this.resilienceStrategy.getFailure((ResilienceStrategy<K, V>) k, e, (CacheLoadingException) e2.getCause());
                    this.getObserver.end(CacheOperationOutcomes.GetOutcome.FAILURE);
                    return failure;
                }
            } catch (Throwable th) {
                this.getObserver.end(CacheOperationOutcomes.GetOutcome.FAILURE);
                throw th;
            }
        }
    }

    @Override // org.ehcache.Cache
    public void put(K key, final V value) throws CacheWritingException {
        this.putObserver.begin();
        this.statusTransitioner.checkAvailable();
        checkNonNull(key, value);
        final AtomicReference<V> previousMapping = new AtomicReference<>();
        BiFunction<? super K, ? super V, ? extends V> biFunctionMemoize = Functions.memoize(new BiFunction<K, V, V>() { // from class: org.ehcache.core.EhcacheWithLoaderWriter.3
            /* JADX WARN: Multi-variable type inference failed */
            @Override // org.ehcache.core.spi.function.BiFunction
            public V apply(K k, V v) {
                previousMapping.set(v);
                try {
                    EhcacheWithLoaderWriter.this.cacheLoaderWriter.write(k, value);
                    return (V) value;
                } catch (Exception e) {
                    throw new StorePassThroughException(ExceptionFactory.newCacheWritingException(e));
                }
            }
        });
        try {
            this.store.compute(key, biFunctionMemoize);
            if (previousMapping.get() != null) {
                this.putObserver.end(CacheOperationOutcomes.PutOutcome.UPDATED);
            } else {
                this.putObserver.end(CacheOperationOutcomes.PutOutcome.PUT);
            }
        } catch (StoreAccessException e) {
            try {
                try {
                    biFunctionMemoize.apply(key, value);
                    this.resilienceStrategy.putFailure(key, value, e);
                    this.putObserver.end(CacheOperationOutcomes.PutOutcome.FAILURE);
                } catch (StorePassThroughException cpte) {
                    this.resilienceStrategy.putFailure(key, value, e, (CacheWritingException) cpte.getCause());
                    this.putObserver.end(CacheOperationOutcomes.PutOutcome.FAILURE);
                }
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
    public void remove(K key) throws CacheWritingException {
        removeInternal(key);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean removeInternal(K key) throws CacheWritingException {
        this.removeObserver.begin();
        this.statusTransitioner.checkAvailable();
        checkNonNull(key);
        final AtomicBoolean modified = new AtomicBoolean();
        BiFunction<? super K, ? super V, ? extends V> biFunctionMemoize = Functions.memoize(new BiFunction<K, V, V>() { // from class: org.ehcache.core.EhcacheWithLoaderWriter.4
            @Override // org.ehcache.core.spi.function.BiFunction
            public V apply(K key2, V previousValue) {
                modified.set(previousValue != null);
                try {
                    EhcacheWithLoaderWriter.this.cacheLoaderWriter.delete(key2);
                    return null;
                } catch (Exception e) {
                    throw new StorePassThroughException(ExceptionFactory.newCacheWritingException(e));
                }
            }
        });
        try {
            this.store.compute(key, biFunctionMemoize);
            if (modified.get()) {
                this.removeObserver.end(CacheOperationOutcomes.RemoveOutcome.SUCCESS);
            } else {
                this.removeObserver.end(CacheOperationOutcomes.RemoveOutcome.NOOP);
            }
        } catch (StoreAccessException e) {
            try {
                try {
                    biFunctionMemoize.apply(key, null);
                } catch (StorePassThroughException f) {
                    this.resilienceStrategy.removeFailure(key, e, (CacheWritingException) f.getCause());
                }
                this.resilienceStrategy.removeFailure(key, e);
                this.removeObserver.end(CacheOperationOutcomes.RemoveOutcome.FAILURE);
            } catch (Throwable th) {
                this.removeObserver.end(CacheOperationOutcomes.RemoveOutcome.FAILURE);
                throw th;
            }
        }
        return modified.get();
    }

    @Override // org.ehcache.Cache
    public void clear() {
        this.statusTransitioner.checkAvailable();
        try {
            this.store.clear();
        } catch (StoreAccessException e) {
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
    public Map<K, V> getAllInternal(Set<? extends K> set, boolean z) throws BulkCacheLoadingException {
        this.getAllObserver.begin();
        this.statusTransitioner.checkAvailable();
        checkNonNullContent(set);
        if (set.isEmpty()) {
            this.getAllObserver.end(CacheOperationOutcomes.GetAllOutcome.SUCCESS);
            return Collections.emptyMap();
        }
        final Map<K, V> map = new HashMap<>();
        final HashMap map2 = new HashMap();
        Function<Iterable<? extends K>, Iterable<? extends Map.Entry<? extends K, ? extends V>>> function = new Function<Iterable<? extends K>, Iterable<? extends Map.Entry<? extends K, ? extends V>>>() { // from class: org.ehcache.core.EhcacheWithLoaderWriter.5
            @Override // org.ehcache.core.spi.function.Function
            public Iterable<? extends Map.Entry<? extends K, ? extends V>> apply(Iterable<? extends K> keys) {
                Map<K, V> computeResult = new LinkedHashMap<>();
                for (K key : keys) {
                    computeResult.put(key, null);
                }
                Map<K, V> mapEmptyMap = Collections.emptyMap();
                try {
                    mapEmptyMap = EhcacheWithLoaderWriter.this.cacheLoaderWriter.loadAll(computeResult.keySet());
                } catch (BulkCacheLoadingException bcle) {
                    EhcacheWithLoaderWriter.this.collectSuccessesAndFailures(bcle, map, map2);
                } catch (Exception e) {
                    Iterator i$ = computeResult.keySet().iterator();
                    while (i$.hasNext()) {
                        map2.put(i$.next(), e);
                    }
                }
                if (!mapEmptyMap.isEmpty()) {
                    for (K key2 : computeResult.keySet()) {
                        V value = mapEmptyMap.get(key2);
                        map.put(key2, value);
                        computeResult.put(key2, value);
                    }
                }
                return computeResult.entrySet();
            }
        };
        HashMap map3 = new HashMap();
        try {
            int i = 0;
            int i2 = 0;
            for (Map.Entry<K, Store.ValueHolder<V>> entry : this.store.bulkComputeIfAbsent(set, function).entrySet()) {
                i2++;
                if (entry.getValue() != null) {
                    map3.put(entry.getKey(), entry.getValue().value());
                    i++;
                } else if (z && map2.isEmpty()) {
                    map3.put(entry.getKey(), null);
                }
            }
            addBulkMethodEntriesCount(BulkOps.GET_ALL_HITS, i);
            if (map2.isEmpty()) {
                addBulkMethodEntriesCount(BulkOps.GET_ALL_MISS, i2 - i);
                this.getAllObserver.end(CacheOperationOutcomes.GetAllOutcome.SUCCESS);
                return map3;
            }
            map.putAll(map3);
            this.getAllObserver.end(CacheOperationOutcomes.GetAllOutcome.FAILURE);
            throw new BulkCacheLoadingException(map2, map);
        } catch (StoreAccessException e) {
            try {
                HashSet hashSet = new HashSet();
                Iterator<? extends K> it = set.iterator();
                while (it.hasNext()) {
                    hashSet.add(it.next());
                }
                hashSet.removeAll(map.keySet());
                hashSet.removeAll(map2.keySet());
                function.apply(hashSet);
                if (map2.isEmpty()) {
                    Map<K, V> allFailure = this.resilienceStrategy.getAllFailure(set, map, e);
                    this.getAllObserver.end(CacheOperationOutcomes.GetAllOutcome.FAILURE);
                    return allFailure;
                }
                Map<K, V> allFailure2 = this.resilienceStrategy.getAllFailure(set, e, new BulkCacheLoadingException(map2, map));
                this.getAllObserver.end(CacheOperationOutcomes.GetAllOutcome.FAILURE);
                return allFailure2;
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
        final Set<K> successes = new HashSet<>();
        final Map<K, Exception> failures = new HashMap<>();
        final Map<K, V> entriesToRemap = new HashMap<>();
        for (Map.Entry<? extends K, ? extends V> entry : entries.entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null) {
                throw new NullPointerException();
            }
            entriesToRemap.put(entry.getKey(), entry.getValue());
        }
        final AtomicInteger actualPutCount = new AtomicInteger();
        Function<Iterable<? extends Map.Entry<? extends K, ? extends V>>, Iterable<? extends Map.Entry<? extends K, ? extends V>>> computeFunction = new Function<Iterable<? extends Map.Entry<? extends K, ? extends V>>, Iterable<? extends Map.Entry<? extends K, ? extends V>>>() { // from class: org.ehcache.core.EhcacheWithLoaderWriter.6
            /* JADX WARN: Multi-variable type inference failed */
            @Override // org.ehcache.core.spi.function.Function
            public Iterable<? extends Map.Entry<? extends K, ? extends V>> apply(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries2) throws IllegalStateException {
                EhcacheWithLoaderWriter.this.cacheLoaderWriterWriteAllCall(entries2, entriesToRemap, successes, failures);
                LinkedHashMap linkedHashMap = new LinkedHashMap();
                for (Map.Entry<? extends K, ? extends V> entry2 : entries2) {
                    K key = entry2.getKey();
                    V existingValue = entry2.getValue();
                    Object objRemove = entriesToRemap.remove(key);
                    if (EhcacheWithLoaderWriter.this.newValueAlreadyExpired(key, existingValue, objRemove)) {
                        linkedHashMap.put(key, null);
                    } else if (successes.contains(key)) {
                        actualPutCount.incrementAndGet();
                        linkedHashMap.put(key, objRemove);
                    } else {
                        linkedHashMap.put(key, existingValue);
                    }
                }
                return linkedHashMap.entrySet();
            }
        };
        try {
            this.store.bulkCompute(entries.keySet(), computeFunction);
            addBulkMethodEntriesCount(BulkOps.PUT_ALL, actualPutCount.get());
            if (failures.isEmpty()) {
                this.putAllObserver.end(CacheOperationOutcomes.PutAllOutcome.SUCCESS);
                return;
            }
            BulkCacheWritingException cacheWritingException = new BulkCacheWritingException(failures, successes);
            tryRemoveFailedKeys(entries, failures, cacheWritingException);
            this.putAllObserver.end(CacheOperationOutcomes.PutAllOutcome.FAILURE);
            throw cacheWritingException;
        } catch (StoreAccessException e) {
            try {
                if (!entriesToRemap.isEmpty()) {
                    cacheLoaderWriterWriteAllCall(entriesToRemap.entrySet(), entriesToRemap, successes, failures);
                }
                if (failures.isEmpty()) {
                    this.resilienceStrategy.putAllFailure(entries, e);
                } else {
                    this.resilienceStrategy.putAllFailure(entries, e, new BulkCacheWritingException(failures, successes));
                }
            } finally {
                this.putAllObserver.end(CacheOperationOutcomes.PutAllOutcome.FAILURE);
            }
        }
    }

    private void tryRemoveFailedKeys(Map<? extends K, ? extends V> entries, Map<K, Exception> failures, BulkCacheWritingException cacheWritingException) {
        try {
            this.store.bulkCompute(failures.keySet(), new Function<Iterable<? extends Map.Entry<? extends K, ? extends V>>, Iterable<? extends Map.Entry<? extends K, ? extends V>>>() { // from class: org.ehcache.core.EhcacheWithLoaderWriter.7
                @Override // org.ehcache.core.spi.function.Function
                public Iterable<? extends Map.Entry<? extends K, ? extends V>> apply(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries2) {
                    HashMap<K, V> result = new HashMap<>();
                    for (Map.Entry<? extends K, ? extends V> entry : entries2) {
                        result.put(entry.getKey(), null);
                    }
                    return result.entrySet();
                }
            });
        } catch (StoreAccessException e) {
            this.resilienceStrategy.putAllFailure(entries, e, cacheWritingException);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cacheLoaderWriterWriteAllCall(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries, Map<K, V> entriesToRemap, Set<K> successes, Map<K, Exception> map) throws IllegalStateException {
        Map<K, V> toWrite = new HashMap<>();
        for (Map.Entry<? extends K, ? extends V> entry : entries) {
            V value = entriesToRemap.get(entry.getKey());
            if (value != null) {
                toWrite.put(entry.getKey(), value);
            }
        }
        try {
            if (!toWrite.isEmpty()) {
                this.cacheLoaderWriter.writeAll(toWrite.entrySet());
                successes.addAll(toWrite.keySet());
            }
        } catch (BulkCacheWritingException bcwe) {
            collectSuccessesAndFailures(bcwe, successes, map);
        } catch (Exception e) {
            for (K key : toWrite.keySet()) {
                map.put(key, e);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static <K> void collectSuccessesAndFailures(BulkCacheWritingException bcwe, Set<K> successes, Map<K, Exception> map) {
        successes.addAll(bcwe.getSuccesses());
        map.putAll(bcwe.getFailures());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public void collectSuccessesAndFailures(BulkCacheLoadingException bcle, Map<K, V> map, Map<K, Exception> map2) {
        map.putAll(bcle.getSuccesses());
        map2.putAll(bcle.getFailures());
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
        final Set<K> successes = new HashSet<>();
        final Map<K, Exception> failures = new HashMap<>();
        final Map<K, ? extends V> entriesToRemove = new HashMap<>();
        for (K key : keys) {
            if (key == null) {
                throw new NullPointerException();
            }
            entriesToRemove.put(key, null);
        }
        final AtomicInteger actualRemoveCount = new AtomicInteger();
        Function<Iterable<? extends Map.Entry<? extends K, ? extends V>>, Iterable<? extends Map.Entry<? extends K, ? extends V>>> removalFunction = new Function<Iterable<? extends Map.Entry<? extends K, ? extends V>>, Iterable<? extends Map.Entry<? extends K, ? extends V>>>() { // from class: org.ehcache.core.EhcacheWithLoaderWriter.8
            @Override // org.ehcache.core.spi.function.Function
            public Iterable<? extends Map.Entry<? extends K, ? extends V>> apply(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
                Set<K> unknowns = EhcacheWithLoaderWriter.this.cacheLoaderWriterDeleteAllCall(entries, entriesToRemove, successes, failures);
                Map<K, V> results = new LinkedHashMap<>();
                for (Map.Entry<? extends K, ? extends V> entry : entries) {
                    K key2 = entry.getKey();
                    V existingValue = entry.getValue();
                    if (successes.contains(key2)) {
                        if (existingValue != null) {
                            actualRemoveCount.incrementAndGet();
                        }
                        results.put(key2, null);
                        entriesToRemove.remove(key2);
                    } else if (unknowns.contains(key2)) {
                        results.put(key2, null);
                    } else {
                        results.put(key2, existingValue);
                    }
                }
                return results.entrySet();
            }
        };
        try {
            this.store.bulkCompute(keys, removalFunction);
            addBulkMethodEntriesCount(BulkOps.REMOVE_ALL, actualRemoveCount.get());
            if (failures.isEmpty()) {
                this.removeAllObserver.end(CacheOperationOutcomes.RemoveAllOutcome.SUCCESS);
            } else {
                this.removeAllObserver.end(CacheOperationOutcomes.RemoveAllOutcome.FAILURE);
                throw new BulkCacheWritingException(failures, successes);
            }
        } catch (StoreAccessException e) {
            try {
                if (!entriesToRemove.isEmpty()) {
                    cacheLoaderWriterDeleteAllCall(entriesToRemove.entrySet(), entriesToRemove, successes, failures);
                }
                if (failures.isEmpty()) {
                    this.resilienceStrategy.removeAllFailure(keys, e);
                } else {
                    this.resilienceStrategy.removeAllFailure(keys, e, new BulkCacheWritingException(failures, successes));
                }
            } finally {
                this.removeAllObserver.end(CacheOperationOutcomes.RemoveAllOutcome.FAILURE);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Set<K> cacheLoaderWriterDeleteAllCall(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries, Map<K, ? extends V> entriesToRemove, Set<K> successes, Map<K, Exception> map) {
        HashSet hashSet = new HashSet();
        Set<K> toDelete = new HashSet<>();
        for (Map.Entry<? extends K, ? extends V> entry : entries) {
            K key = entry.getKey();
            if (entriesToRemove.containsKey(key)) {
                toDelete.add(key);
            }
        }
        try {
            this.cacheLoaderWriter.deleteAll(toDelete);
            successes.addAll(toDelete);
        } catch (BulkCacheWritingException bcwe) {
            collectSuccessesAndFailures(bcwe, successes, map);
        } catch (Exception e) {
            for (K key2 : toDelete) {
                map.put(key2, e);
                hashSet.add(key2);
            }
        }
        return hashSet;
    }

    @Override // org.ehcache.Cache
    public V putIfAbsent(K k, final V v) throws CacheWritingException {
        this.putIfAbsentObserver.begin();
        this.statusTransitioner.checkAvailable();
        checkNonNull(k, v);
        final AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        Function<? super K, ? extends V> functionMemoize = Functions.memoize(new Function<K, V>() { // from class: org.ehcache.core.EhcacheWithLoaderWriter.9
            /* JADX WARN: Multi-variable type inference failed */
            @Override // org.ehcache.core.spi.function.Function
            public V apply(K k2) {
                if (EhcacheWithLoaderWriter.this.useLoaderInAtomics) {
                    try {
                        V v2 = (V) EhcacheWithLoaderWriter.this.cacheLoaderWriter.load(k2);
                        if (v2 != null) {
                            return v2;
                        }
                    } catch (Exception e) {
                        throw new StorePassThroughException(ExceptionFactory.newCacheLoadingException(e));
                    }
                }
                try {
                    EhcacheWithLoaderWriter.this.cacheLoaderWriter.write(k2, v);
                    atomicBoolean.set(true);
                    return (V) v;
                } catch (Exception e2) {
                    throw new StorePassThroughException(ExceptionFactory.newCacheWritingException(e2));
                }
            }
        });
        try {
            Store.ValueHolder<V> valueHolderComputeIfAbsent = this.store.computeIfAbsent(k, functionMemoize);
            if (atomicBoolean.get()) {
                this.putIfAbsentObserver.end(CacheOperationOutcomes.PutIfAbsentOutcome.PUT);
                return null;
            }
            if (valueHolderComputeIfAbsent == null) {
                this.putIfAbsentObserver.end(CacheOperationOutcomes.PutIfAbsentOutcome.HIT);
                return null;
            }
            this.putIfAbsentObserver.end(CacheOperationOutcomes.PutIfAbsentOutcome.HIT);
            return valueHolderComputeIfAbsent.value();
        } catch (StoreAccessException e) {
            try {
                try {
                    V v2 = (V) this.resilienceStrategy.putIfAbsentFailure(k, v, functionMemoize.apply(k), e, atomicBoolean.get());
                    this.putIfAbsentObserver.end(CacheOperationOutcomes.PutIfAbsentOutcome.FAILURE);
                    return v2;
                } catch (StorePassThroughException e2) {
                    Throwable cause = e2.getCause();
                    if (cause instanceof CacheLoadingException) {
                        V vPutIfAbsentFailure = this.resilienceStrategy.putIfAbsentFailure((ResilienceStrategy<K, V>) k, (K) v, e, (CacheLoadingException) cause);
                        this.putIfAbsentObserver.end(CacheOperationOutcomes.PutIfAbsentOutcome.FAILURE);
                        return vPutIfAbsentFailure;
                    }
                    if (cause instanceof CacheWritingException) {
                        V vPutIfAbsentFailure2 = this.resilienceStrategy.putIfAbsentFailure((ResilienceStrategy<K, V>) k, (K) v, e, (CacheWritingException) cause);
                        this.putIfAbsentObserver.end(CacheOperationOutcomes.PutIfAbsentOutcome.FAILURE);
                        return vPutIfAbsentFailure2;
                    }
                    throw new AssertionError();
                }
            } catch (Throwable th) {
                this.putIfAbsentObserver.end(CacheOperationOutcomes.PutIfAbsentOutcome.FAILURE);
                throw th;
            }
        }
    }

    @Override // org.ehcache.Cache
    public boolean remove(final K key, final V value) throws CacheWritingException {
        this.conditionalRemoveObserver.begin();
        this.statusTransitioner.checkAvailable();
        checkNonNull(key, value);
        final AtomicBoolean hit = new AtomicBoolean();
        final AtomicBoolean removed = new AtomicBoolean();
        BiFunction<? super K, ? super V, ? extends V> biFunctionMemoize = Functions.memoize(new BiFunction<K, V, V>() { // from class: org.ehcache.core.EhcacheWithLoaderWriter.10
            /* JADX WARN: Multi-variable type inference failed */
            @Override // org.ehcache.core.spi.function.BiFunction
            public V apply(K k, V inCache) {
                if (inCache == null) {
                    if (EhcacheWithLoaderWriter.this.useLoaderInAtomics) {
                        try {
                            inCache = EhcacheWithLoaderWriter.this.cacheLoaderWriter.load(key);
                            if (inCache == null) {
                                return null;
                            }
                        } catch (Exception e) {
                            throw new StorePassThroughException(ExceptionFactory.newCacheLoadingException(e));
                        }
                    } else {
                        return null;
                    }
                }
                hit.set(true);
                if (value.equals(inCache)) {
                    try {
                        EhcacheWithLoaderWriter.this.cacheLoaderWriter.delete(k);
                        removed.set(true);
                        return null;
                    } catch (Exception e2) {
                        throw new StorePassThroughException(ExceptionFactory.newCacheWritingException(e2));
                    }
                }
                return inCache;
            }
        });
        try {
            this.store.compute(key, biFunctionMemoize, REPLACE_FALSE);
            if (removed.get()) {
                this.conditionalRemoveObserver.end(CacheOperationOutcomes.ConditionalRemoveOutcome.SUCCESS);
            } else if (hit.get()) {
                this.conditionalRemoveObserver.end(CacheOperationOutcomes.ConditionalRemoveOutcome.FAILURE_KEY_PRESENT);
            } else {
                this.conditionalRemoveObserver.end(CacheOperationOutcomes.ConditionalRemoveOutcome.FAILURE_KEY_MISSING);
            }
            return removed.get();
        } catch (StoreAccessException e) {
            try {
                try {
                    biFunctionMemoize.apply(key, null);
                    boolean zRemoveFailure = this.resilienceStrategy.removeFailure((ResilienceStrategy<K, V>) key, (K) value, e, removed.get());
                    this.conditionalRemoveObserver.end(CacheOperationOutcomes.ConditionalRemoveOutcome.FAILURE);
                    return zRemoveFailure;
                } catch (StorePassThroughException f) {
                    Throwable cause = f.getCause();
                    if (cause instanceof CacheLoadingException) {
                        boolean zRemoveFailure2 = this.resilienceStrategy.removeFailure((ResilienceStrategy<K, V>) key, (K) value, e, (CacheLoadingException) cause);
                        this.conditionalRemoveObserver.end(CacheOperationOutcomes.ConditionalRemoveOutcome.FAILURE);
                        return zRemoveFailure2;
                    }
                    if (cause instanceof CacheWritingException) {
                        boolean zRemoveFailure3 = this.resilienceStrategy.removeFailure((ResilienceStrategy<K, V>) key, (K) value, e, (CacheWritingException) cause);
                        this.conditionalRemoveObserver.end(CacheOperationOutcomes.ConditionalRemoveOutcome.FAILURE);
                        return zRemoveFailure3;
                    }
                    throw new AssertionError();
                }
            } catch (Throwable th) {
                this.conditionalRemoveObserver.end(CacheOperationOutcomes.ConditionalRemoveOutcome.FAILURE);
                throw th;
            }
        }
    }

    @Override // org.ehcache.Cache
    public V replace(final K key, final V value) throws CacheLoadingException, CacheWritingException {
        this.replaceObserver.begin();
        this.statusTransitioner.checkAvailable();
        checkNonNull(key, value);
        final AtomicReference<V> old = new AtomicReference<>();
        BiFunction<? super K, ? super V, ? extends V> biFunctionMemoize = Functions.memoize(new BiFunction<K, V, V>() { // from class: org.ehcache.core.EhcacheWithLoaderWriter.11
            /* JADX WARN: Multi-variable type inference failed */
            @Override // org.ehcache.core.spi.function.BiFunction
            public V apply(K k, V v) {
                if (v == null) {
                    if (EhcacheWithLoaderWriter.this.useLoaderInAtomics) {
                        try {
                            v = EhcacheWithLoaderWriter.this.cacheLoaderWriter.load(key);
                            if (v == null) {
                                return null;
                            }
                        } catch (Exception e) {
                            throw new StorePassThroughException(ExceptionFactory.newCacheLoadingException(e));
                        }
                    } else {
                        return null;
                    }
                }
                try {
                    EhcacheWithLoaderWriter.this.cacheLoaderWriter.write(key, value);
                    old.set(v);
                    if (EhcacheWithLoaderWriter.this.newValueAlreadyExpired(key, v, value)) {
                        return null;
                    }
                    return (V) value;
                } catch (Exception e2) {
                    throw new StorePassThroughException(ExceptionFactory.newCacheWritingException(e2));
                }
            }
        });
        try {
            this.store.compute(key, biFunctionMemoize);
            if (old.get() != null) {
                this.replaceObserver.end(CacheOperationOutcomes.ReplaceOutcome.HIT);
            } else {
                this.replaceObserver.end(CacheOperationOutcomes.ReplaceOutcome.MISS_NOT_PRESENT);
            }
            return old.get();
        } catch (StoreAccessException e) {
            try {
                try {
                    biFunctionMemoize.apply(key, null);
                    V vReplaceFailure = this.resilienceStrategy.replaceFailure(key, value, e);
                    this.replaceObserver.end(CacheOperationOutcomes.ReplaceOutcome.FAILURE);
                    return vReplaceFailure;
                } catch (StorePassThroughException f) {
                    Throwable cause = f.getCause();
                    if (cause instanceof CacheLoadingException) {
                        V vReplaceFailure2 = this.resilienceStrategy.replaceFailure((ResilienceStrategy<K, V>) key, (K) value, e, (CacheLoadingException) cause);
                        this.replaceObserver.end(CacheOperationOutcomes.ReplaceOutcome.FAILURE);
                        return vReplaceFailure2;
                    }
                    if (cause instanceof CacheWritingException) {
                        V vReplaceFailure3 = this.resilienceStrategy.replaceFailure((ResilienceStrategy<K, V>) key, (K) value, e, (CacheWritingException) cause);
                        this.replaceObserver.end(CacheOperationOutcomes.ReplaceOutcome.FAILURE);
                        return vReplaceFailure3;
                    }
                    throw new AssertionError();
                }
            } catch (Throwable th) {
                this.replaceObserver.end(CacheOperationOutcomes.ReplaceOutcome.FAILURE);
                throw th;
            }
        }
    }

    @Override // org.ehcache.Cache
    public boolean replace(final K key, final V oldValue, final V newValue) throws CacheLoadingException, CacheWritingException {
        this.replaceObserver.begin();
        this.statusTransitioner.checkAvailable();
        checkNonNull(key, oldValue, newValue);
        final AtomicBoolean success = new AtomicBoolean();
        final AtomicBoolean hit = new AtomicBoolean();
        BiFunction<? super K, ? super V, ? extends V> biFunctionMemoize = Functions.memoize(new BiFunction<K, V, V>() { // from class: org.ehcache.core.EhcacheWithLoaderWriter.12
            /* JADX WARN: Multi-variable type inference failed */
            @Override // org.ehcache.core.spi.function.BiFunction
            public V apply(K k, V v) {
                if (v == null) {
                    if (EhcacheWithLoaderWriter.this.useLoaderInAtomics) {
                        try {
                            v = EhcacheWithLoaderWriter.this.cacheLoaderWriter.load(key);
                            if (v == null) {
                                return null;
                            }
                        } catch (Exception e) {
                            throw new StorePassThroughException(ExceptionFactory.newCacheLoadingException(e));
                        }
                    } else {
                        return null;
                    }
                }
                hit.set(true);
                if (oldValue.equals(v)) {
                    try {
                        EhcacheWithLoaderWriter.this.cacheLoaderWriter.write(key, newValue);
                        success.set(true);
                        if (EhcacheWithLoaderWriter.this.newValueAlreadyExpired(key, oldValue, newValue)) {
                            return null;
                        }
                        return (V) newValue;
                    } catch (Exception e2) {
                        throw new StorePassThroughException(ExceptionFactory.newCacheWritingException(e2));
                    }
                }
                return v;
            }
        });
        try {
            this.store.compute(key, biFunctionMemoize, REPLACE_FALSE);
            if (success.get()) {
                this.replaceObserver.end(CacheOperationOutcomes.ReplaceOutcome.HIT);
            } else if (hit.get()) {
                this.replaceObserver.end(CacheOperationOutcomes.ReplaceOutcome.MISS_PRESENT);
            } else {
                this.replaceObserver.end(CacheOperationOutcomes.ReplaceOutcome.MISS_NOT_PRESENT);
            }
            return success.get();
        } catch (StoreAccessException e) {
            try {
                try {
                    biFunctionMemoize.apply(key, null);
                    boolean zReplaceFailure = this.resilienceStrategy.replaceFailure((ResilienceStrategy<K, V>) key, oldValue, newValue, e, success.get());
                    this.replaceObserver.end(CacheOperationOutcomes.ReplaceOutcome.FAILURE);
                    return zReplaceFailure;
                } catch (StorePassThroughException f) {
                    Throwable cause = f.getCause();
                    if (cause instanceof CacheLoadingException) {
                        boolean zReplaceFailure2 = this.resilienceStrategy.replaceFailure((ResilienceStrategy<K, V>) key, oldValue, newValue, e, (CacheLoadingException) cause);
                        this.replaceObserver.end(CacheOperationOutcomes.ReplaceOutcome.FAILURE);
                        return zReplaceFailure2;
                    }
                    if (cause instanceof CacheWritingException) {
                        boolean zReplaceFailure3 = this.resilienceStrategy.replaceFailure((ResilienceStrategy<K, V>) key, oldValue, newValue, e, (CacheWritingException) cause);
                        this.replaceObserver.end(CacheOperationOutcomes.ReplaceOutcome.FAILURE);
                        return zReplaceFailure3;
                    }
                    throw new AssertionError();
                }
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
        return this.cacheLoaderWriter;
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/EhcacheWithLoaderWriter$Jsr107CacheImpl.class */
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
            return new SpecIterator(this, EhcacheWithLoaderWriter.this.store);
        }

        @Override // org.ehcache.core.Jsr107Cache
        public V getNoLoader(K k) {
            return (V) EhcacheWithLoaderWriter.this.getNoLoader(k);
        }

        @Override // org.ehcache.core.Jsr107Cache
        public Map<K, V> getAll(Set<? extends K> keys) {
            return EhcacheWithLoaderWriter.this.getAllInternal(keys, false);
        }

        private void loadAllAbsent(Set<? extends K> keys, final Function<Iterable<? extends K>, Map<K, V>> loadFunction) {
            try {
                EhcacheWithLoaderWriter.this.store.bulkComputeIfAbsent(keys, new Function<Iterable<? extends K>, Iterable<? extends Map.Entry<? extends K, ? extends V>>>() { // from class: org.ehcache.core.EhcacheWithLoaderWriter.Jsr107CacheImpl.1
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
                EhcacheWithLoaderWriter.this.store.bulkCompute(keys, new Function<Iterable<? extends Map.Entry<? extends K, ? extends V>>, Iterable<? extends Map.Entry<? extends K, ? extends V>>>() { // from class: org.ehcache.core.EhcacheWithLoaderWriter.Jsr107CacheImpl.2
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
        public void compute(K key, final BiFunction<? super K, ? super V, ? extends V> computeFunction, final NullaryFunction<Boolean> replaceEqual, final NullaryFunction<Boolean> invokeWriter, final NullaryFunction<Boolean> withStatsAndEvents) {
            EhcacheWithLoaderWriter.this.putObserver.begin();
            EhcacheWithLoaderWriter.this.removeObserver.begin();
            EhcacheWithLoaderWriter.this.getObserver.begin();
            try {
                BiFunction<K, V, V> fn = new BiFunction<K, V, V>() { // from class: org.ehcache.core.EhcacheWithLoaderWriter.Jsr107CacheImpl.3
                    @Override // org.ehcache.core.spi.function.BiFunction
                    public V apply(K k, V v) {
                        if (v == null) {
                            EhcacheWithLoaderWriter.this.getObserver.end(CacheOperationOutcomes.GetOutcome.MISS);
                        } else {
                            EhcacheWithLoaderWriter.this.getObserver.end(CacheOperationOutcomes.GetOutcome.HIT);
                        }
                        V v2 = (V) computeFunction.apply(k, v);
                        if (v2 == v && !((Boolean) replaceEqual.apply()).booleanValue()) {
                            return v;
                        }
                        if (((Boolean) invokeWriter.apply()).booleanValue()) {
                            try {
                                if (v2 != null) {
                                    EhcacheWithLoaderWriter.this.cacheLoaderWriter.write(k, v2);
                                } else {
                                    EhcacheWithLoaderWriter.this.cacheLoaderWriter.delete(k);
                                }
                            } catch (Exception e) {
                                throw new StorePassThroughException(ExceptionFactory.newCacheWritingException(e));
                            }
                        }
                        if (EhcacheWithLoaderWriter.this.newValueAlreadyExpired(k, v, v2)) {
                            return null;
                        }
                        if (((Boolean) withStatsAndEvents.apply()).booleanValue()) {
                            if (v2 == null) {
                                EhcacheWithLoaderWriter.this.removeObserver.end(CacheOperationOutcomes.RemoveOutcome.SUCCESS);
                            } else {
                                EhcacheWithLoaderWriter.this.putObserver.end(CacheOperationOutcomes.PutOutcome.PUT);
                            }
                        }
                        return v2;
                    }
                };
                EhcacheWithLoaderWriter.this.store.compute(key, fn, replaceEqual);
            } catch (StoreAccessException e) {
                throw new RuntimeException(e);
            }
        }

        @Override // org.ehcache.core.Jsr107Cache
        public V getAndRemove(K key) {
            EhcacheWithLoaderWriter.this.getObserver.begin();
            EhcacheWithLoaderWriter.this.removeObserver.begin();
            final AtomicReference<V> existingValue = new AtomicReference<>();
            try {
                EhcacheWithLoaderWriter.this.store.compute(key, new BiFunction<K, V, V>() { // from class: org.ehcache.core.EhcacheWithLoaderWriter.Jsr107CacheImpl.4
                    @Override // org.ehcache.core.spi.function.BiFunction
                    public V apply(K mappedKey, V mappedValue) {
                        existingValue.set(mappedValue);
                        try {
                            EhcacheWithLoaderWriter.this.cacheLoaderWriter.delete(mappedKey);
                            return null;
                        } catch (Exception e) {
                            throw new StorePassThroughException(ExceptionFactory.newCacheWritingException(e));
                        }
                    }
                });
                V returnValue = existingValue.get();
                if (returnValue != null) {
                    EhcacheWithLoaderWriter.this.getObserver.end(CacheOperationOutcomes.GetOutcome.HIT);
                    EhcacheWithLoaderWriter.this.removeObserver.end(CacheOperationOutcomes.RemoveOutcome.SUCCESS);
                } else {
                    EhcacheWithLoaderWriter.this.getObserver.end(CacheOperationOutcomes.GetOutcome.MISS);
                }
                return returnValue;
            } catch (StoreAccessException e) {
                EhcacheWithLoaderWriter.this.getObserver.end(CacheOperationOutcomes.GetOutcome.FAILURE);
                EhcacheWithLoaderWriter.this.removeObserver.end(CacheOperationOutcomes.RemoveOutcome.FAILURE);
                throw new RuntimeException(e);
            }
        }

        @Override // org.ehcache.core.Jsr107Cache
        public V getAndPut(K key, final V value) {
            EhcacheWithLoaderWriter.this.getObserver.begin();
            EhcacheWithLoaderWriter.this.putObserver.begin();
            final AtomicReference<V> existingValue = new AtomicReference<>();
            try {
                EhcacheWithLoaderWriter.this.store.compute(key, new BiFunction<K, V, V>() { // from class: org.ehcache.core.EhcacheWithLoaderWriter.Jsr107CacheImpl.5
                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // org.ehcache.core.spi.function.BiFunction
                    public V apply(K k, V v) {
                        existingValue.set(v);
                        try {
                            EhcacheWithLoaderWriter.this.cacheLoaderWriter.write(k, value);
                            if (EhcacheWithLoaderWriter.this.newValueAlreadyExpired(k, v, value)) {
                                return null;
                            }
                            return (V) value;
                        } catch (Exception e) {
                            throw new StorePassThroughException(ExceptionFactory.newCacheWritingException(e));
                        }
                    }
                });
                V returnValue = existingValue.get();
                if (returnValue != null) {
                    EhcacheWithLoaderWriter.this.getObserver.end(CacheOperationOutcomes.GetOutcome.HIT);
                    EhcacheWithLoaderWriter.this.putObserver.end(CacheOperationOutcomes.PutOutcome.UPDATED);
                } else {
                    EhcacheWithLoaderWriter.this.getObserver.end(CacheOperationOutcomes.GetOutcome.MISS);
                    EhcacheWithLoaderWriter.this.putObserver.end(CacheOperationOutcomes.PutOutcome.PUT);
                }
                return returnValue;
            } catch (StoreAccessException e) {
                EhcacheWithLoaderWriter.this.getObserver.end(CacheOperationOutcomes.GetOutcome.FAILURE);
                EhcacheWithLoaderWriter.this.putObserver.end(CacheOperationOutcomes.PutOutcome.FAILURE);
                throw new RuntimeException(e);
            }
        }

        @Override // org.ehcache.core.Jsr107Cache
        public boolean remove(K key) {
            return EhcacheWithLoaderWriter.this.removeInternal(key);
        }

        @Override // org.ehcache.core.Jsr107Cache
        public void removeAll() {
            Store.Iterator<Cache.Entry<K, Store.ValueHolder<V>>> iterator = EhcacheWithLoaderWriter.this.store.iterator();
            while (iterator.hasNext()) {
                try {
                    Cache.Entry<K, Store.ValueHolder<V>> next = iterator.next();
                    remove(next.getKey());
                } catch (StoreAccessException e) {
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/EhcacheWithLoaderWriter$CacheEntryIterator.class */
    private class CacheEntryIterator implements Iterator<Cache.Entry<K, V>> {
        private final Store.Iterator<Cache.Entry<K, Store.ValueHolder<V>>> iterator;
        private final boolean quiet;
        private Cache.Entry<K, Store.ValueHolder<V>> current;
        private Cache.Entry<K, Store.ValueHolder<V>> next;
        private StoreAccessException nextException;

        public CacheEntryIterator(boolean quiet) {
            this.quiet = quiet;
            this.iterator = EhcacheWithLoaderWriter.this.store.iterator();
            advance();
        }

        private void advance() {
            while (this.iterator.hasNext()) {
                try {
                    this.next = this.iterator.next();
                    if (EhcacheWithLoaderWriter.this.getNoLoader(this.next.getKey()) != null) {
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
            EhcacheWithLoaderWriter.this.statusTransitioner.checkAvailable();
            return (this.nextException == null && this.next == null) ? false : true;
        }

        @Override // java.util.Iterator
        public Cache.Entry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            if (!this.quiet) {
                EhcacheWithLoaderWriter.this.getObserver.begin();
            }
            if (this.nextException == null) {
                if (!this.quiet) {
                    EhcacheWithLoaderWriter.this.getObserver.end(CacheOperationOutcomes.GetOutcome.HIT);
                }
                this.current = this.next;
                advance();
                return new ValueHolderBasedEntry(this.current);
            }
            if (!this.quiet) {
                EhcacheWithLoaderWriter.this.getObserver.end(CacheOperationOutcomes.GetOutcome.FAILURE);
            }
            StoreAccessException cae = this.nextException;
            this.nextException = null;
            return EhcacheWithLoaderWriter.this.resilienceStrategy.iteratorFailure(cae);
        }

        @Override // java.util.Iterator
        public void remove() throws CacheWritingException {
            EhcacheWithLoaderWriter.this.statusTransitioner.checkAvailable();
            if (this.current == null) {
                throw new IllegalStateException("No current element");
            }
            EhcacheWithLoaderWriter.this.remove(this.current.getKey(), this.current.getValue().value());
            this.current = null;
        }
    }

    private static <K> RecoveryCache<K> recoveryCache(final Store<K, ?> store) {
        return new RecoveryCache<K>() { // from class: org.ehcache.core.EhcacheWithLoaderWriter.13
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
                    obliterate((AnonymousClass13) key);
                }
            }
        };
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/EhcacheWithLoaderWriter$ValueHolderBasedEntry.class */
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
}
