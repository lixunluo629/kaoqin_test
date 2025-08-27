package org.ehcache.impl.internal.store.offheap;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import ch.qos.logback.core.pattern.parser.Parser;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.ehcache.Cache;
import org.ehcache.config.EvictionAdvisor;
import org.ehcache.core.events.StoreEventDispatcher;
import org.ehcache.core.events.StoreEventSink;
import org.ehcache.core.exceptions.StorePassThroughException;
import org.ehcache.core.internal.util.ValueSuppliers;
import org.ehcache.core.spi.function.BiFunction;
import org.ehcache.core.spi.function.Function;
import org.ehcache.core.spi.function.NullaryFunction;
import org.ehcache.core.spi.store.Store;
import org.ehcache.core.spi.store.StoreAccessException;
import org.ehcache.core.spi.store.events.StoreEventSource;
import org.ehcache.core.spi.store.tiering.AuthoritativeTier;
import org.ehcache.core.spi.store.tiering.CachingTier;
import org.ehcache.core.spi.store.tiering.LowerCachingTier;
import org.ehcache.core.spi.time.TimeSource;
import org.ehcache.core.statistics.AuthoritativeTierOperationOutcomes;
import org.ehcache.core.statistics.LowerCachingTierOperationsOutcome;
import org.ehcache.core.statistics.StoreOperationOutcomes;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expiry;
import org.ehcache.impl.internal.store.BinaryValueHolder;
import org.ehcache.impl.internal.store.offheap.factories.EhcacheSegmentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.offheapstore.exceptions.OversizeMappingException;
import org.terracotta.statistics.StatisticBuilder;
import org.terracotta.statistics.StatisticsManager;
import org.terracotta.statistics.observer.OperationObserver;
import redis.clients.jedis.Protocol;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/offheap/AbstractOffHeapStore.class */
public abstract class AbstractOffHeapStore<K, V> implements AuthoritativeTier<K, V>, LowerCachingTier<K, V> {
    private final Class<K> keyType;
    private final Class<V> valueType;
    private final TimeSource timeSource;
    private final StoreEventDispatcher<K, V> eventDispatcher;
    private final Expiry<? super K, ? super V> expiry;
    private final OperationObserver<StoreOperationOutcomes.GetOutcome> getObserver;
    private final OperationObserver<StoreOperationOutcomes.PutOutcome> putObserver;
    private final OperationObserver<StoreOperationOutcomes.PutIfAbsentOutcome> putIfAbsentObserver;
    private final OperationObserver<StoreOperationOutcomes.RemoveOutcome> removeObserver;
    private final OperationObserver<StoreOperationOutcomes.ConditionalRemoveOutcome> conditionalRemoveObserver;
    private final OperationObserver<StoreOperationOutcomes.ReplaceOutcome> replaceObserver;
    private final OperationObserver<StoreOperationOutcomes.ConditionalReplaceOutcome> conditionalReplaceObserver;
    private final OperationObserver<StoreOperationOutcomes.ComputeOutcome> computeObserver;
    private final OperationObserver<StoreOperationOutcomes.ComputeIfAbsentOutcome> computeIfAbsentObserver;
    private final OperationObserver<StoreOperationOutcomes.EvictionOutcome> evictionObserver;
    private final OperationObserver<StoreOperationOutcomes.ExpirationOutcome> expirationObserver;
    private final OperationObserver<AuthoritativeTierOperationOutcomes.GetAndFaultOutcome> getAndFaultObserver;
    private final OperationObserver<AuthoritativeTierOperationOutcomes.ComputeIfAbsentAndFaultOutcome> computeIfAbsentAndFaultObserver;
    private final OperationObserver<AuthoritativeTierOperationOutcomes.FlushOutcome> flushObserver;
    private final OperationObserver<LowerCachingTierOperationsOutcome.InvalidateOutcome> invalidateObserver;
    private final OperationObserver<LowerCachingTierOperationsOutcome.InvalidateAllOutcome> invalidateAllObserver;
    private final OperationObserver<LowerCachingTierOperationsOutcome.InvalidateAllWithHashOutcome> invalidateAllWithHashObserver;
    private final OperationObserver<LowerCachingTierOperationsOutcome.GetAndRemoveOutcome> getAndRemoveObserver;
    private final OperationObserver<LowerCachingTierOperationsOutcome.InstallMappingOutcome> installMappingObserver;
    private volatile AuthoritativeTier.InvalidationValve valve;
    protected BackingMapEvictionListener<K, V> mapEvictionListener;
    private volatile CachingTier.InvalidationListener<K, V> invalidationListener = (CachingTier.InvalidationListener<K, V>) NULL_INVALIDATION_LISTENER;
    private static final Logger LOG = LoggerFactory.getLogger((Class<?>) AbstractOffHeapStore.class);
    private static final CachingTier.InvalidationListener<?, ?> NULL_INVALIDATION_LISTENER = new CachingTier.InvalidationListener<Object, Object>() { // from class: org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore.1
        @Override // org.ehcache.core.spi.store.tiering.CachingTier.InvalidationListener
        public void onInvalidation(Object key, Store.ValueHolder<Object> valueHolder) {
        }
    };
    private static final NullaryFunction<Boolean> REPLACE_EQUALS_TRUE = new NullaryFunction<Boolean>() { // from class: org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore.33
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.ehcache.core.spi.function.NullaryFunction
        public Boolean apply() {
            return Boolean.TRUE;
        }
    };

    protected abstract EhcacheOffHeapBackingMap<K, OffHeapValueHolder<V>> backingMap();

    protected abstract SwitchableEvictionAdvisor<K, OffHeapValueHolder<V>> evictionAdvisor();

    public AbstractOffHeapStore(String str, Store.Configuration<K, V> configuration, TimeSource timeSource, StoreEventDispatcher<K, V> storeEventDispatcher) {
        this.keyType = configuration.getKeyType();
        this.valueType = configuration.getValueType();
        this.expiry = configuration.getExpiry();
        this.timeSource = timeSource;
        this.eventDispatcher = storeEventDispatcher;
        this.getObserver = StatisticBuilder.operation(StoreOperationOutcomes.GetOutcome.class).of(this).named(BeanUtil.PREFIX_GETTER_GET).tag(str).build();
        this.putObserver = StatisticBuilder.operation(StoreOperationOutcomes.PutOutcome.class).of(this).named("put").tag(str).build();
        this.putIfAbsentObserver = StatisticBuilder.operation(StoreOperationOutcomes.PutIfAbsentOutcome.class).of(this).named("putIfAbsent").tag(str).build();
        this.removeObserver = StatisticBuilder.operation(StoreOperationOutcomes.RemoveOutcome.class).of(this).named(Protocol.SENTINEL_REMOVE).tag(str).build();
        this.conditionalRemoveObserver = StatisticBuilder.operation(StoreOperationOutcomes.ConditionalRemoveOutcome.class).of(this).named("conditionalRemove").tag(str).build();
        this.replaceObserver = StatisticBuilder.operation(StoreOperationOutcomes.ReplaceOutcome.class).of(this).named(Parser.REPLACE_CONVERTER_WORD).tag(str).build();
        this.conditionalReplaceObserver = StatisticBuilder.operation(StoreOperationOutcomes.ConditionalReplaceOutcome.class).of(this).named("conditionalReplace").tag(str).build();
        this.computeObserver = StatisticBuilder.operation(StoreOperationOutcomes.ComputeOutcome.class).of(this).named("compute").tag(str).build();
        this.computeIfAbsentObserver = StatisticBuilder.operation(StoreOperationOutcomes.ComputeIfAbsentOutcome.class).of(this).named("computeIfAbsent").tag(str).build();
        this.evictionObserver = StatisticBuilder.operation(StoreOperationOutcomes.EvictionOutcome.class).of(this).named("eviction").tag(str).build();
        this.expirationObserver = StatisticBuilder.operation(StoreOperationOutcomes.ExpirationOutcome.class).of(this).named("expiration").tag(str).build();
        this.getAndFaultObserver = StatisticBuilder.operation(AuthoritativeTierOperationOutcomes.GetAndFaultOutcome.class).of(this).named("getAndFault").tag(str).build();
        this.computeIfAbsentAndFaultObserver = StatisticBuilder.operation(AuthoritativeTierOperationOutcomes.ComputeIfAbsentAndFaultOutcome.class).of(this).named("computeIfAbsentAndFault").tag(str).build();
        this.flushObserver = StatisticBuilder.operation(AuthoritativeTierOperationOutcomes.FlushOutcome.class).of(this).named("flush").tag(str).build();
        this.invalidateObserver = StatisticBuilder.operation(LowerCachingTierOperationsOutcome.InvalidateOutcome.class).of(this).named("invalidate").tag(str).build();
        this.invalidateAllObserver = StatisticBuilder.operation(LowerCachingTierOperationsOutcome.InvalidateAllOutcome.class).of(this).named("invalidateAll").tag(str).build();
        this.invalidateAllWithHashObserver = StatisticBuilder.operation(LowerCachingTierOperationsOutcome.InvalidateAllWithHashOutcome.class).of(this).named("invalidateAllWithHash").tag(str).build();
        this.getAndRemoveObserver = StatisticBuilder.operation(LowerCachingTierOperationsOutcome.GetAndRemoveOutcome.class).of(this).named("getAndRemove").tag(str).build();
        this.installMappingObserver = StatisticBuilder.operation(LowerCachingTierOperationsOutcome.InstallMappingOutcome.class).of(this).named("installMapping").tag(str).build();
        HashSet hashSet = new HashSet(Arrays.asList(str, "tier"));
        HashMap map = new HashMap();
        map.put("discriminator", str);
        StatisticsManager.createPassThroughStatistic(this, "allocatedMemory", hashSet, map, new Callable<Number>() { // from class: org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Number call() throws Exception {
                return Long.valueOf(AbstractOffHeapStore.this.backingMap().allocatedMemory());
            }
        });
        StatisticsManager.createPassThroughStatistic(this, "occupiedMemory", hashSet, map, new Callable<Number>() { // from class: org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Number call() throws Exception {
                return Long.valueOf(AbstractOffHeapStore.this.backingMap().occupiedMemory());
            }
        });
        StatisticsManager.createPassThroughStatistic(this, "dataAllocatedMemory", hashSet, map, new Callable<Number>() { // from class: org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Number call() throws Exception {
                return Long.valueOf(AbstractOffHeapStore.this.backingMap().dataAllocatedMemory());
            }
        });
        StatisticsManager.createPassThroughStatistic(this, "dataOccupiedMemory", hashSet, map, new Callable<Number>() { // from class: org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Number call() throws Exception {
                return Long.valueOf(AbstractOffHeapStore.this.backingMap().dataOccupiedMemory());
            }
        });
        StatisticsManager.createPassThroughStatistic(this, "dataSize", hashSet, map, new Callable<Number>() { // from class: org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore.6
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Number call() throws Exception {
                return Long.valueOf(AbstractOffHeapStore.this.backingMap().dataSize());
            }
        });
        StatisticsManager.createPassThroughStatistic(this, "dataVitalMemory", hashSet, map, new Callable<Number>() { // from class: org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore.7
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Number call() throws Exception {
                return Long.valueOf(AbstractOffHeapStore.this.backingMap().dataVitalMemory());
            }
        });
        StatisticsManager.createPassThroughStatistic(this, "mappings", hashSet, map, new Callable<Number>() { // from class: org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore.8
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Number call() throws Exception {
                return Long.valueOf(AbstractOffHeapStore.this.backingMap().longSize());
            }
        });
        StatisticsManager.createPassThroughStatistic(this, "maxMappings", hashSet, map, new Callable<Number>() { // from class: org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore.9
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Number call() throws Exception {
                return -1L;
            }
        });
        StatisticsManager.createPassThroughStatistic(this, "vitalMemory", hashSet, map, new Callable<Number>() { // from class: org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore.10
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Number call() throws Exception {
                return Long.valueOf(AbstractOffHeapStore.this.backingMap().vitalMemory());
            }
        });
        StatisticsManager.createPassThroughStatistic(this, "removedSlotCount", hashSet, map, new Callable<Number>() { // from class: org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore.11
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Number call() throws Exception {
                return Long.valueOf(AbstractOffHeapStore.this.backingMap().removedSlotCount());
            }
        });
        StatisticsManager.createPassThroughStatistic(this, "reprobeLength", hashSet, map, new Callable<Number>() { // from class: org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore.12
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Number call() throws Exception {
                return Long.valueOf(AbstractOffHeapStore.this.backingMap().reprobeLength());
            }
        });
        StatisticsManager.createPassThroughStatistic(this, "usedSlotCount", hashSet, map, new Callable<Number>() { // from class: org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore.13
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Number call() throws Exception {
                return Long.valueOf(AbstractOffHeapStore.this.backingMap().usedSlotCount());
            }
        });
        StatisticsManager.createPassThroughStatistic(this, "tableCapacity", hashSet, map, new Callable<Number>() { // from class: org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore.14
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Number call() throws Exception {
                return Long.valueOf(AbstractOffHeapStore.this.backingMap().tableCapacity());
            }
        });
        this.mapEvictionListener = new BackingMapEvictionListener<>(storeEventDispatcher, this.evictionObserver);
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.ValueHolder<V> get(K key) throws StoreAccessException {
        checkKey(key);
        this.getObserver.begin();
        Store.ValueHolder<V> result = internalGet(key, true, true);
        if (result == null) {
            this.getObserver.end(StoreOperationOutcomes.GetOutcome.MISS);
        } else {
            this.getObserver.end(StoreOperationOutcomes.GetOutcome.HIT);
        }
        return result;
    }

    private Store.ValueHolder<V> internalGet(K key, final boolean updateAccess, final boolean touchValue) throws StoreAccessException {
        final StoreEventSink<K, V> eventSink = this.eventDispatcher.eventSink();
        final AtomicReference<OffHeapValueHolder<V>> heldValue = new AtomicReference<>();
        try {
            OffHeapValueHolder<V> result = backingMap().computeIfPresent((EhcacheOffHeapBackingMap<K, OffHeapValueHolder<V>>) key, (BiFunction<EhcacheOffHeapBackingMap<K, OffHeapValueHolder<V>>, OffHeapValueHolder<V>, OffHeapValueHolder<V>>) new BiFunction<K, OffHeapValueHolder<V>, OffHeapValueHolder<V>>() { // from class: org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore.15
                @Override // org.ehcache.core.spi.function.BiFunction
                public /* bridge */ /* synthetic */ Object apply(Object x0, Object x1) {
                    return apply((AnonymousClass15) x0, (OffHeapValueHolder) x1);
                }

                public OffHeapValueHolder<V> apply(K mappedKey, OffHeapValueHolder<V> mappedValue) {
                    long now = AbstractOffHeapStore.this.timeSource.getTimeMillis();
                    if (mappedValue.isExpired(now, TimeUnit.MILLISECONDS)) {
                        AbstractOffHeapStore.this.onExpiration(mappedKey, mappedValue, eventSink);
                        return null;
                    }
                    if (updateAccess) {
                        mappedValue.forceDeserialization();
                        OffHeapValueHolder<V> valueHolder = AbstractOffHeapStore.this.setAccessTimeAndExpiryThenReturnMapping(mappedKey, mappedValue, now, eventSink);
                        if (valueHolder == null) {
                            heldValue.set(mappedValue);
                        }
                        return valueHolder;
                    }
                    if (touchValue) {
                        mappedValue.forceDeserialization();
                    }
                    return mappedValue;
                }
            });
            if (result == null && heldValue.get() != null) {
                result = heldValue.get();
            }
            this.eventDispatcher.releaseEventSink(eventSink);
            return result;
        } catch (RuntimeException re) {
            this.eventDispatcher.releaseEventSinkAfterFailure(eventSink, re);
            StorePassThroughException.handleRuntimeException(re);
            return null;
        }
    }

    @Override // org.ehcache.core.spi.store.Store
    public boolean containsKey(K key) throws StoreAccessException {
        checkKey(key);
        return internalGet(key, false, false) != null;
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.PutStatus put(final K key, final V value) throws StoreAccessException {
        this.putObserver.begin();
        checkKey(key);
        checkValue(value);
        final AtomicBoolean added = new AtomicBoolean();
        final AtomicReference<OffHeapValueHolder<V>> replacedVal = new AtomicReference<>(null);
        final StoreEventSink<K, V> eventSink = this.eventDispatcher.eventSink();
        final long now = this.timeSource.getTimeMillis();
        try {
            BiFunction<K, OffHeapValueHolder<V>, OffHeapValueHolder<V>> mappingFunction = new BiFunction<K, OffHeapValueHolder<V>, OffHeapValueHolder<V>>() { // from class: org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore.16
                @Override // org.ehcache.core.spi.function.BiFunction
                public /* bridge */ /* synthetic */ Object apply(Object x0, Object x1) {
                    return apply((AnonymousClass16) x0, (OffHeapValueHolder) x1);
                }

                public OffHeapValueHolder<V> apply(K mappedKey, OffHeapValueHolder<V> mappedValue) {
                    if (mappedValue != null && mappedValue.isExpired(now, TimeUnit.MILLISECONDS)) {
                        mappedValue = null;
                    }
                    if (mappedValue == null) {
                        OffHeapValueHolder<V> newValue = AbstractOffHeapStore.this.newCreateValueHolder(key, value, now, eventSink);
                        added.set(newValue != null);
                        return newValue;
                    }
                    OffHeapValueHolder<V> newValue2 = AbstractOffHeapStore.this.newUpdatedValueHolder(key, value, mappedValue, now, eventSink);
                    replacedVal.set(mappedValue);
                    return newValue2;
                }
            };
            computeWithRetry(key, mappingFunction, false);
            this.eventDispatcher.releaseEventSink(eventSink);
            if (replacedVal.get() != null) {
                this.putObserver.end(StoreOperationOutcomes.PutOutcome.REPLACED);
                return Store.PutStatus.UPDATE;
            }
            if (added.get()) {
                this.putObserver.end(StoreOperationOutcomes.PutOutcome.PUT);
                return Store.PutStatus.PUT;
            }
            this.putObserver.end(StoreOperationOutcomes.PutOutcome.REPLACED);
            return Store.PutStatus.NOOP;
        } catch (RuntimeException re) {
            this.eventDispatcher.releaseEventSinkAfterFailure(eventSink, re);
            throw re;
        } catch (StoreAccessException caex) {
            this.eventDispatcher.releaseEventSinkAfterFailure(eventSink, caex);
            throw caex;
        }
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.ValueHolder<V> putIfAbsent(K key, final V value) throws StoreAccessException, NullPointerException {
        this.putIfAbsentObserver.begin();
        checkKey(key);
        checkValue(value);
        final AtomicReference<Store.ValueHolder<V>> returnValue = new AtomicReference<>();
        final StoreEventSink<K, V> eventSink = this.eventDispatcher.eventSink();
        try {
            BiFunction<K, OffHeapValueHolder<V>, OffHeapValueHolder<V>> mappingFunction = new BiFunction<K, OffHeapValueHolder<V>, OffHeapValueHolder<V>>() { // from class: org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore.17
                @Override // org.ehcache.core.spi.function.BiFunction
                public /* bridge */ /* synthetic */ Object apply(Object x0, Object x1) {
                    return apply((AnonymousClass17) x0, (OffHeapValueHolder) x1);
                }

                public OffHeapValueHolder<V> apply(K mappedKey, OffHeapValueHolder<V> mappedValue) {
                    long now = AbstractOffHeapStore.this.timeSource.getTimeMillis();
                    if (mappedValue == null || mappedValue.isExpired(now, TimeUnit.MILLISECONDS)) {
                        if (mappedValue != null) {
                            AbstractOffHeapStore.this.onExpiration(mappedKey, mappedValue, eventSink);
                        }
                        return AbstractOffHeapStore.this.newCreateValueHolder(mappedKey, value, now, eventSink);
                    }
                    mappedValue.forceDeserialization();
                    returnValue.set(mappedValue);
                    return AbstractOffHeapStore.this.setAccessTimeAndExpiryThenReturnMapping(mappedKey, mappedValue, now, eventSink);
                }
            };
            computeWithRetry(key, mappingFunction, false);
            this.eventDispatcher.releaseEventSink(eventSink);
            Store.ValueHolder<V> resultHolder = returnValue.get();
            if (resultHolder == null) {
                this.putIfAbsentObserver.end(StoreOperationOutcomes.PutIfAbsentOutcome.PUT);
                return null;
            }
            this.putIfAbsentObserver.end(StoreOperationOutcomes.PutIfAbsentOutcome.HIT);
            return resultHolder;
        } catch (RuntimeException re) {
            this.eventDispatcher.releaseEventSinkAfterFailure(eventSink, re);
            throw re;
        } catch (StoreAccessException caex) {
            this.eventDispatcher.releaseEventSinkAfterFailure(eventSink, caex);
            throw caex;
        }
    }

    @Override // org.ehcache.core.spi.store.Store
    public boolean remove(K key) throws StoreAccessException {
        this.removeObserver.begin();
        checkKey(key);
        final StoreEventSink<K, V> eventSink = this.eventDispatcher.eventSink();
        final long now = this.timeSource.getTimeMillis();
        final AtomicBoolean removed = new AtomicBoolean(false);
        try {
            backingMap().computeIfPresent((EhcacheOffHeapBackingMap<K, OffHeapValueHolder<V>>) key, (BiFunction<EhcacheOffHeapBackingMap<K, OffHeapValueHolder<V>>, OffHeapValueHolder<V>, OffHeapValueHolder<V>>) new BiFunction<K, OffHeapValueHolder<V>, OffHeapValueHolder<V>>() { // from class: org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore.18
                @Override // org.ehcache.core.spi.function.BiFunction
                public /* bridge */ /* synthetic */ Object apply(Object x0, Object x1) {
                    return apply((AnonymousClass18) x0, (OffHeapValueHolder) x1);
                }

                public OffHeapValueHolder<V> apply(K mappedKey, OffHeapValueHolder<V> mappedValue) {
                    if (mappedValue != null && mappedValue.isExpired(now, TimeUnit.MILLISECONDS)) {
                        AbstractOffHeapStore.this.onExpiration(mappedKey, mappedValue, eventSink);
                        return null;
                    }
                    if (mappedValue != null) {
                        removed.set(true);
                        eventSink.removed(mappedKey, mappedValue);
                        return null;
                    }
                    return null;
                }
            });
            this.eventDispatcher.releaseEventSink(eventSink);
            if (removed.get()) {
                this.removeObserver.end(StoreOperationOutcomes.RemoveOutcome.REMOVED);
            } else {
                this.removeObserver.end(StoreOperationOutcomes.RemoveOutcome.MISS);
            }
            return removed.get();
        } catch (RuntimeException re) {
            this.eventDispatcher.releaseEventSinkAfterFailure(eventSink, re);
            StorePassThroughException.handleRuntimeException(re);
            return false;
        }
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.RemoveStatus remove(K key, final V value) throws StoreAccessException {
        this.conditionalRemoveObserver.begin();
        checkKey(key);
        checkValue(value);
        final AtomicBoolean removed = new AtomicBoolean(false);
        final StoreEventSink<K, V> eventSink = this.eventDispatcher.eventSink();
        final AtomicBoolean mappingExists = new AtomicBoolean();
        try {
            backingMap().computeIfPresent((EhcacheOffHeapBackingMap<K, OffHeapValueHolder<V>>) key, (BiFunction<EhcacheOffHeapBackingMap<K, OffHeapValueHolder<V>>, OffHeapValueHolder<V>, OffHeapValueHolder<V>>) new BiFunction<K, OffHeapValueHolder<V>, OffHeapValueHolder<V>>() { // from class: org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore.19
                @Override // org.ehcache.core.spi.function.BiFunction
                public /* bridge */ /* synthetic */ Object apply(Object x0, Object x1) {
                    return apply((AnonymousClass19) x0, (OffHeapValueHolder) x1);
                }

                public OffHeapValueHolder<V> apply(K mappedKey, OffHeapValueHolder<V> mappedValue) {
                    long now = AbstractOffHeapStore.this.timeSource.getTimeMillis();
                    if (mappedValue.isExpired(now, TimeUnit.MILLISECONDS)) {
                        AbstractOffHeapStore.this.onExpiration(mappedKey, mappedValue, eventSink);
                        return null;
                    }
                    if (mappedValue.value().equals(value)) {
                        removed.set(true);
                        eventSink.removed(mappedKey, mappedValue);
                        return null;
                    }
                    mappingExists.set(true);
                    return AbstractOffHeapStore.this.setAccessTimeAndExpiryThenReturnMapping(mappedKey, mappedValue, now, eventSink);
                }
            });
            this.eventDispatcher.releaseEventSink(eventSink);
            if (removed.get()) {
                this.conditionalRemoveObserver.end(StoreOperationOutcomes.ConditionalRemoveOutcome.REMOVED);
                return Store.RemoveStatus.REMOVED;
            }
            this.conditionalRemoveObserver.end(StoreOperationOutcomes.ConditionalRemoveOutcome.MISS);
            if (mappingExists.get()) {
                return Store.RemoveStatus.KEY_PRESENT;
            }
            return Store.RemoveStatus.KEY_MISSING;
        } catch (RuntimeException re) {
            this.eventDispatcher.releaseEventSinkAfterFailure(eventSink, re);
            StorePassThroughException.handleRuntimeException(re);
            return Store.RemoveStatus.KEY_MISSING;
        }
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.ValueHolder<V> replace(K key, final V value) throws StoreAccessException, NullPointerException {
        this.replaceObserver.begin();
        checkKey(key);
        checkValue(value);
        final AtomicReference<Store.ValueHolder<V>> returnValue = new AtomicReference<>(null);
        final StoreEventSink<K, V> eventSink = this.eventDispatcher.eventSink();
        BiFunction<K, OffHeapValueHolder<V>, OffHeapValueHolder<V>> mappingFunction = new BiFunction<K, OffHeapValueHolder<V>, OffHeapValueHolder<V>>() { // from class: org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore.20
            @Override // org.ehcache.core.spi.function.BiFunction
            public /* bridge */ /* synthetic */ Object apply(Object x0, Object x1) {
                return apply((AnonymousClass20) x0, (OffHeapValueHolder) x1);
            }

            public OffHeapValueHolder<V> apply(K mappedKey, OffHeapValueHolder<V> mappedValue) {
                long now = AbstractOffHeapStore.this.timeSource.getTimeMillis();
                if (mappedValue == null || mappedValue.isExpired(now, TimeUnit.MILLISECONDS)) {
                    if (mappedValue != null) {
                        AbstractOffHeapStore.this.onExpiration(mappedKey, mappedValue, eventSink);
                        return null;
                    }
                    return null;
                }
                returnValue.set(mappedValue);
                return AbstractOffHeapStore.this.newUpdatedValueHolder(mappedKey, value, mappedValue, now, eventSink);
            }
        };
        try {
            computeWithRetry(key, mappingFunction, false);
            this.eventDispatcher.releaseEventSink(eventSink);
            Store.ValueHolder<V> resultHolder = returnValue.get();
            if (resultHolder != null) {
                this.replaceObserver.end(StoreOperationOutcomes.ReplaceOutcome.REPLACED);
            } else {
                this.replaceObserver.end(StoreOperationOutcomes.ReplaceOutcome.MISS);
            }
            return resultHolder;
        } catch (RuntimeException re) {
            this.eventDispatcher.releaseEventSinkAfterFailure(eventSink, re);
            throw re;
        } catch (StoreAccessException caex) {
            this.eventDispatcher.releaseEventSinkAfterFailure(eventSink, caex);
            throw caex;
        }
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.ReplaceStatus replace(K key, final V oldValue, final V newValue) throws StoreAccessException, IllegalArgumentException, NullPointerException {
        this.conditionalReplaceObserver.begin();
        checkKey(key);
        checkValue(oldValue);
        checkValue(newValue);
        final AtomicBoolean replaced = new AtomicBoolean(false);
        final StoreEventSink<K, V> eventSink = this.eventDispatcher.eventSink();
        final AtomicBoolean mappingExists = new AtomicBoolean();
        BiFunction<K, OffHeapValueHolder<V>, OffHeapValueHolder<V>> mappingFunction = new BiFunction<K, OffHeapValueHolder<V>, OffHeapValueHolder<V>>() { // from class: org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore.21
            @Override // org.ehcache.core.spi.function.BiFunction
            public /* bridge */ /* synthetic */ Object apply(Object x0, Object x1) {
                return apply((AnonymousClass21) x0, (OffHeapValueHolder) x1);
            }

            public OffHeapValueHolder<V> apply(K mappedKey, OffHeapValueHolder<V> mappedValue) {
                long now = AbstractOffHeapStore.this.timeSource.getTimeMillis();
                if (mappedValue == null || mappedValue.isExpired(now, TimeUnit.MILLISECONDS)) {
                    if (mappedValue != null) {
                        AbstractOffHeapStore.this.onExpiration(mappedKey, mappedValue, eventSink);
                        return null;
                    }
                    return null;
                }
                if (oldValue.equals(mappedValue.value())) {
                    replaced.set(true);
                    return AbstractOffHeapStore.this.newUpdatedValueHolder(mappedKey, newValue, mappedValue, now, eventSink);
                }
                mappingExists.set(true);
                return AbstractOffHeapStore.this.setAccessTimeAndExpiryThenReturnMapping(mappedKey, mappedValue, now, eventSink);
            }
        };
        try {
            computeWithRetry(key, mappingFunction, false);
            this.eventDispatcher.releaseEventSink(eventSink);
            if (replaced.get()) {
                this.conditionalReplaceObserver.end(StoreOperationOutcomes.ConditionalReplaceOutcome.REPLACED);
                return Store.ReplaceStatus.HIT;
            }
            this.conditionalReplaceObserver.end(StoreOperationOutcomes.ConditionalReplaceOutcome.MISS);
            if (mappingExists.get()) {
                return Store.ReplaceStatus.MISS_PRESENT;
            }
            return Store.ReplaceStatus.MISS_NOT_PRESENT;
        } catch (RuntimeException re) {
            this.eventDispatcher.releaseEventSinkAfterFailure(eventSink, re);
            throw re;
        } catch (StoreAccessException caex) {
            this.eventDispatcher.releaseEventSinkAfterFailure(eventSink, caex);
            throw caex;
        }
    }

    @Override // org.ehcache.core.spi.store.Store
    public void clear() throws StoreAccessException {
        try {
            backingMap().clear();
        } catch (RuntimeException re) {
            StorePassThroughException.handleRuntimeException(re);
        }
    }

    @Override // org.ehcache.core.spi.store.Store
    public StoreEventSource<K, V> getStoreEventSource() {
        return this.eventDispatcher;
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.Iterator<Cache.Entry<K, Store.ValueHolder<V>>> iterator() {
        return new Store.Iterator<Cache.Entry<K, Store.ValueHolder<V>>>() { // from class: org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore.22
            private final Iterator<Map.Entry<K, OffHeapValueHolder<V>>> mapIterator;

            {
                this.mapIterator = AbstractOffHeapStore.this.backingMap().entrySet().iterator();
            }

            @Override // org.ehcache.core.spi.store.Store.Iterator
            public boolean hasNext() {
                return this.mapIterator.hasNext();
            }

            @Override // org.ehcache.core.spi.store.Store.Iterator
            public Cache.Entry<K, Store.ValueHolder<V>> next() throws StoreAccessException {
                Map.Entry<K, OffHeapValueHolder<V>> next = this.mapIterator.next();
                final K key = next.getKey();
                final OffHeapValueHolder<V> value = next.getValue();
                return new Cache.Entry<K, Store.ValueHolder<V>>() { // from class: org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore.22.1
                    @Override // org.ehcache.Cache.Entry
                    public K getKey() {
                        return (K) key;
                    }

                    @Override // org.ehcache.Cache.Entry
                    public Store.ValueHolder<V> getValue() {
                        return value;
                    }
                };
            }
        };
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.ValueHolder<V> compute(K key, BiFunction<? super K, ? super V, ? extends V> mappingFunction) throws StoreAccessException {
        return compute(key, mappingFunction, REPLACE_EQUALS_TRUE);
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.ValueHolder<V> compute(final K key, final BiFunction<? super K, ? super V, ? extends V> mappingFunction, final NullaryFunction<Boolean> replaceEqual) throws StoreAccessException {
        this.computeObserver.begin();
        checkKey(key);
        final AtomicBoolean write = new AtomicBoolean(false);
        final AtomicReference<OffHeapValueHolder<V>> valueHeld = new AtomicReference<>();
        final StoreEventSink<K, V> eventSink = this.eventDispatcher.eventSink();
        BiFunction<K, OffHeapValueHolder<V>, OffHeapValueHolder<V>> computeFunction = new BiFunction<K, OffHeapValueHolder<V>, OffHeapValueHolder<V>>() { // from class: org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore.23
            @Override // org.ehcache.core.spi.function.BiFunction
            public /* bridge */ /* synthetic */ Object apply(Object x0, Object x1) {
                return apply((AnonymousClass23) x0, (OffHeapValueHolder) x1);
            }

            public OffHeapValueHolder<V> apply(K mappedKey, OffHeapValueHolder<V> mappedValue) {
                long now = AbstractOffHeapStore.this.timeSource.getTimeMillis();
                V existingValue = null;
                if (mappedValue == null || mappedValue.isExpired(now, TimeUnit.MILLISECONDS)) {
                    if (mappedValue != null) {
                        AbstractOffHeapStore.this.onExpiration(mappedKey, mappedValue, eventSink);
                    }
                    mappedValue = null;
                } else {
                    existingValue = mappedValue.value();
                }
                Object objApply = mappingFunction.apply(mappedKey, existingValue);
                if (objApply != null) {
                    if (!AbstractOffHeapStore.this.safeEquals(existingValue, objApply) || ((Boolean) replaceEqual.apply()).booleanValue()) {
                        AbstractOffHeapStore.this.checkValue(objApply);
                        write.set(true);
                        if (mappedValue != null) {
                            OffHeapValueHolder<V> valueHolder = AbstractOffHeapStore.this.newUpdatedValueHolder(key, objApply, mappedValue, now, eventSink);
                            if (valueHolder == null) {
                                valueHeld.set(new BasicOffHeapValueHolder(mappedValue.getId(), objApply, now, now));
                            }
                            return valueHolder;
                        }
                        return AbstractOffHeapStore.this.newCreateValueHolder(key, objApply, now, eventSink);
                    }
                    if (mappedValue != null) {
                        OffHeapValueHolder<V> valueHolder2 = AbstractOffHeapStore.this.setAccessTimeAndExpiryThenReturnMapping(mappedKey, mappedValue, now, eventSink);
                        if (valueHolder2 == null) {
                            valueHeld.set(mappedValue);
                        }
                        return valueHolder2;
                    }
                    return null;
                }
                if (mappedValue != null) {
                    write.set(true);
                    eventSink.removed(mappedKey, mappedValue);
                    return null;
                }
                return null;
            }
        };
        try {
            OffHeapValueHolder<V> result = computeWithRetry(key, computeFunction, false);
            if (result == null && valueHeld.get() != null) {
                result = valueHeld.get();
            }
            this.eventDispatcher.releaseEventSink(eventSink);
            if (result == null) {
                if (write.get()) {
                    this.computeObserver.end(StoreOperationOutcomes.ComputeOutcome.REMOVED);
                } else {
                    this.computeObserver.end(StoreOperationOutcomes.ComputeOutcome.MISS);
                }
            } else if (write.get()) {
                this.computeObserver.end(StoreOperationOutcomes.ComputeOutcome.PUT);
            } else {
                this.computeObserver.end(StoreOperationOutcomes.ComputeOutcome.HIT);
            }
            return result;
        } catch (RuntimeException re) {
            this.eventDispatcher.releaseEventSinkAfterFailure(eventSink, re);
            throw re;
        } catch (StoreAccessException caex) {
            this.eventDispatcher.releaseEventSinkAfterFailure(eventSink, caex);
            throw caex;
        }
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.ValueHolder<V> computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) throws StoreAccessException {
        return internalComputeIfAbsent(key, mappingFunction, false, false);
    }

    private Store.ValueHolder<V> internalComputeIfAbsent(K key, final Function<? super K, ? extends V> mappingFunction, boolean fault, final boolean delayedDeserialization) throws StoreAccessException {
        if (fault) {
            this.computeIfAbsentAndFaultObserver.begin();
        } else {
            this.computeIfAbsentObserver.begin();
        }
        checkKey(key);
        final AtomicBoolean write = new AtomicBoolean(false);
        final AtomicReference<OffHeapValueHolder<V>> valueHeld = new AtomicReference<>();
        final StoreEventSink<K, V> eventSink = this.eventDispatcher.eventSink();
        BiFunction<K, OffHeapValueHolder<V>, OffHeapValueHolder<V>> computeFunction = new BiFunction<K, OffHeapValueHolder<V>, OffHeapValueHolder<V>>() { // from class: org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore.24
            @Override // org.ehcache.core.spi.function.BiFunction
            public /* bridge */ /* synthetic */ Object apply(Object x0, Object x1) {
                return apply((AnonymousClass24) x0, (OffHeapValueHolder) x1);
            }

            public OffHeapValueHolder<V> apply(K mappedKey, OffHeapValueHolder<V> mappedValue) {
                long now = AbstractOffHeapStore.this.timeSource.getTimeMillis();
                if (mappedValue != null && !mappedValue.isExpired(now, TimeUnit.MILLISECONDS)) {
                    OffHeapValueHolder<V> valueHolder = AbstractOffHeapStore.this.setAccessTimeAndExpiryThenReturnMapping(mappedKey, mappedValue, now, eventSink);
                    if (valueHolder != null) {
                        if (delayedDeserialization) {
                            mappedValue.detach();
                        } else {
                            mappedValue.forceDeserialization();
                        }
                    } else {
                        valueHeld.set(mappedValue);
                    }
                    return valueHolder;
                }
                if (mappedValue != null) {
                    AbstractOffHeapStore.this.onExpiration(mappedKey, mappedValue, eventSink);
                }
                write.set(true);
                Object objApply = mappingFunction.apply(mappedKey);
                if (objApply != null) {
                    AbstractOffHeapStore.this.checkValue(objApply);
                    return AbstractOffHeapStore.this.newCreateValueHolder(mappedKey, objApply, now, eventSink);
                }
                return null;
            }
        };
        try {
            OffHeapValueHolder<V> computeResult = computeWithRetry(key, computeFunction, fault);
            if (computeResult == null && valueHeld.get() != null) {
                computeResult = valueHeld.get();
            }
            this.eventDispatcher.releaseEventSink(eventSink);
            if (write.get()) {
                if (computeResult != null) {
                    if (fault) {
                        this.computeIfAbsentAndFaultObserver.end(AuthoritativeTierOperationOutcomes.ComputeIfAbsentAndFaultOutcome.PUT);
                    } else {
                        this.computeIfAbsentObserver.end(StoreOperationOutcomes.ComputeIfAbsentOutcome.PUT);
                    }
                } else if (fault) {
                    this.computeIfAbsentAndFaultObserver.end(AuthoritativeTierOperationOutcomes.ComputeIfAbsentAndFaultOutcome.NOOP);
                } else {
                    this.computeIfAbsentObserver.end(StoreOperationOutcomes.ComputeIfAbsentOutcome.NOOP);
                }
            } else if (fault) {
                this.computeIfAbsentAndFaultObserver.end(AuthoritativeTierOperationOutcomes.ComputeIfAbsentAndFaultOutcome.HIT);
            } else {
                this.computeIfAbsentObserver.end(StoreOperationOutcomes.ComputeIfAbsentOutcome.HIT);
            }
            return computeResult;
        } catch (RuntimeException re) {
            this.eventDispatcher.releaseEventSinkAfterFailure(eventSink, re);
            throw re;
        } catch (StoreAccessException caex) {
            this.eventDispatcher.releaseEventSinkAfterFailure(eventSink, caex);
            throw caex;
        }
    }

    @Override // org.ehcache.core.spi.store.Store
    public Map<K, Store.ValueHolder<V>> bulkCompute(Set<? extends K> keys, Function<Iterable<? extends Map.Entry<? extends K, ? extends V>>, Iterable<? extends Map.Entry<? extends K, ? extends V>>> remappingFunction) throws StoreAccessException {
        return bulkCompute(keys, remappingFunction, REPLACE_EQUALS_TRUE);
    }

    @Override // org.ehcache.core.spi.store.Store
    public Map<K, Store.ValueHolder<V>> bulkCompute(Set<? extends K> keys, final Function<Iterable<? extends Map.Entry<? extends K, ? extends V>>, Iterable<? extends Map.Entry<? extends K, ? extends V>>> remappingFunction, NullaryFunction<Boolean> replaceEqual) throws StoreAccessException {
        Map<K, Store.ValueHolder<V>> result = new HashMap<>();
        for (K key : keys) {
            checkKey(key);
            BiFunction<K, V, V> biFunction = new BiFunction<K, V, V>() { // from class: org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore.25
                @Override // org.ehcache.core.spi.function.BiFunction
                public V apply(final K k, final V v) {
                    Map.Entry entry = (Map.Entry) ((Iterable) remappingFunction.apply(Collections.singleton(new Map.Entry<K, V>() { // from class: org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore.25.1
                        @Override // java.util.Map.Entry
                        public K getKey() {
                            return (K) k;
                        }

                        @Override // java.util.Map.Entry
                        public V getValue() {
                            return (V) v;
                        }

                        @Override // java.util.Map.Entry
                        public V setValue(V value) {
                            throw new UnsupportedOperationException();
                        }
                    }))).iterator().next();
                    if (entry != null) {
                        AbstractOffHeapStore.this.checkKey(entry.getKey());
                        return (V) entry.getValue();
                    }
                    return null;
                }
            };
            Store.ValueHolder<V> computed = compute(key, biFunction, replaceEqual);
            result.put(key, computed);
        }
        return result;
    }

    @Override // org.ehcache.core.spi.store.Store
    public Map<K, Store.ValueHolder<V>> bulkComputeIfAbsent(Set<? extends K> keys, final Function<Iterable<? extends K>, Iterable<? extends Map.Entry<? extends K, ? extends V>>> mappingFunction) throws StoreAccessException {
        Map<K, Store.ValueHolder<V>> result = new HashMap<>();
        for (K key : keys) {
            checkKey(key);
            Function<K, V> function = new Function<K, V>() { // from class: org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore.26
                @Override // org.ehcache.core.spi.function.Function
                public V apply(K k) {
                    Map.Entry entry = (Map.Entry) ((Iterable) mappingFunction.apply(Collections.singleton(k))).iterator().next();
                    if (entry != null) {
                        AbstractOffHeapStore.this.checkKey(entry.getKey());
                        return (V) entry.getValue();
                    }
                    return null;
                }
            };
            Store.ValueHolder<V> computed = computeIfAbsent(key, function);
            result.put(key, computed);
        }
        return result;
    }

    @Override // org.ehcache.core.spi.store.tiering.AuthoritativeTier
    public Store.ValueHolder<V> getAndFault(K key) throws StoreAccessException {
        this.getAndFaultObserver.begin();
        checkKey(key);
        Store.ValueHolder<V> mappedValue = null;
        final StoreEventSink<K, V> eventSink = this.eventDispatcher.eventSink();
        try {
            mappedValue = backingMap().computeIfPresentAndPin(key, new BiFunction<K, OffHeapValueHolder<V>, OffHeapValueHolder<V>>() { // from class: org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore.27
                @Override // org.ehcache.core.spi.function.BiFunction
                public /* bridge */ /* synthetic */ Object apply(Object x0, Object x1) {
                    return apply((AnonymousClass27) x0, (OffHeapValueHolder) x1);
                }

                public OffHeapValueHolder<V> apply(K mappedKey, OffHeapValueHolder<V> mappedValue2) {
                    if (mappedValue2.isExpired(AbstractOffHeapStore.this.timeSource.getTimeMillis(), TimeUnit.MILLISECONDS)) {
                        AbstractOffHeapStore.this.onExpiration(mappedKey, mappedValue2, eventSink);
                        return null;
                    }
                    mappedValue2.detach();
                    return mappedValue2;
                }
            });
            this.eventDispatcher.releaseEventSink(eventSink);
            if (mappedValue == null) {
                this.getAndFaultObserver.end(AuthoritativeTierOperationOutcomes.GetAndFaultOutcome.MISS);
            } else {
                this.getAndFaultObserver.end(AuthoritativeTierOperationOutcomes.GetAndFaultOutcome.HIT);
            }
        } catch (RuntimeException re) {
            this.eventDispatcher.releaseEventSinkAfterFailure(eventSink, re);
            StorePassThroughException.handleRuntimeException(re);
        }
        return mappedValue;
    }

    @Override // org.ehcache.core.spi.store.tiering.AuthoritativeTier
    public Store.ValueHolder<V> computeIfAbsentAndFault(K key, Function<? super K, ? extends V> mappingFunction) throws StoreAccessException {
        return internalComputeIfAbsent(key, mappingFunction, true, true);
    }

    @Override // org.ehcache.core.spi.store.tiering.AuthoritativeTier
    public boolean flush(K key, final Store.ValueHolder<V> valueFlushed) {
        this.flushObserver.begin();
        checkKey(key);
        final StoreEventSink<K, V> eventSink = this.eventDispatcher.eventSink();
        try {
            boolean result = backingMap().computeIfPinned(key, new BiFunction<K, OffHeapValueHolder<V>, OffHeapValueHolder<V>>() { // from class: org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore.28
                @Override // org.ehcache.core.spi.function.BiFunction
                public /* bridge */ /* synthetic */ Object apply(Object x0, Object x1) {
                    return apply((AnonymousClass28) x0, (OffHeapValueHolder) x1);
                }

                public OffHeapValueHolder<V> apply(K k, OffHeapValueHolder<V> valuePresent) {
                    if (valuePresent.getId() == valueFlushed.getId()) {
                        if (valueFlushed.isExpired(AbstractOffHeapStore.this.timeSource.getTimeMillis(), TimeUnit.MILLISECONDS)) {
                            AbstractOffHeapStore.this.onExpiration(k, valuePresent, eventSink);
                            return null;
                        }
                        valuePresent.updateMetadata(valueFlushed);
                        valuePresent.writeBack();
                    }
                    return valuePresent;
                }
            }, new Function<OffHeapValueHolder<V>, Boolean>() { // from class: org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore.29
                @Override // org.ehcache.core.spi.function.Function
                public Boolean apply(OffHeapValueHolder<V> valuePresent) {
                    return Boolean.valueOf(valuePresent.getId() == valueFlushed.getId());
                }
            });
            this.eventDispatcher.releaseEventSink(eventSink);
            if (result) {
                this.flushObserver.end(AuthoritativeTierOperationOutcomes.FlushOutcome.HIT);
                return true;
            }
            this.flushObserver.end(AuthoritativeTierOperationOutcomes.FlushOutcome.MISS);
            return false;
        } catch (RuntimeException re) {
            this.eventDispatcher.releaseEventSinkAfterFailure(eventSink, re);
            throw re;
        }
    }

    @Override // org.ehcache.core.spi.store.tiering.AuthoritativeTier
    public void setInvalidationValve(AuthoritativeTier.InvalidationValve valve) {
        this.valve = valve;
    }

    @Override // org.ehcache.core.spi.store.tiering.LowerCachingTier
    public void setInvalidationListener(CachingTier.InvalidationListener<K, V> invalidationListener) {
        this.invalidationListener = invalidationListener;
        this.mapEvictionListener.setInvalidationListener(invalidationListener);
    }

    @Override // org.ehcache.core.spi.store.tiering.LowerCachingTier
    public void invalidate(final K key) throws StoreAccessException {
        this.invalidateObserver.begin();
        final AtomicBoolean removed = new AtomicBoolean(false);
        try {
            backingMap().computeIfPresent((EhcacheOffHeapBackingMap<K, OffHeapValueHolder<V>>) key, (BiFunction<EhcacheOffHeapBackingMap<K, OffHeapValueHolder<V>>, OffHeapValueHolder<V>, OffHeapValueHolder<V>>) new BiFunction<K, OffHeapValueHolder<V>, OffHeapValueHolder<V>>() { // from class: org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore.30
                @Override // org.ehcache.core.spi.function.BiFunction
                public /* bridge */ /* synthetic */ Object apply(Object x0, Object x1) {
                    return apply((AnonymousClass30) x0, (OffHeapValueHolder) x1);
                }

                public OffHeapValueHolder<V> apply(K k, OffHeapValueHolder<V> present) {
                    removed.set(true);
                    AbstractOffHeapStore.this.notifyInvalidation(key, present);
                    return null;
                }
            });
            if (removed.get()) {
                this.invalidateObserver.end(LowerCachingTierOperationsOutcome.InvalidateOutcome.REMOVED);
            } else {
                this.invalidateObserver.end(LowerCachingTierOperationsOutcome.InvalidateOutcome.MISS);
            }
        } catch (RuntimeException re) {
            StorePassThroughException.handleRuntimeException(re);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.ehcache.core.spi.store.tiering.LowerCachingTier
    public void invalidateAll() throws StoreAccessException {
        this.invalidateAllObserver.begin();
        StoreAccessException exception = null;
        long errorCount = 0;
        Iterator i$ = backingMap().keySet().iterator();
        while (i$.hasNext()) {
            try {
                invalidate(i$.next());
            } catch (StoreAccessException e) {
                errorCount++;
                if (exception == null) {
                    exception = e;
                }
            }
        }
        if (exception != null) {
            this.invalidateAllObserver.end(LowerCachingTierOperationsOutcome.InvalidateAllOutcome.FAILURE);
            throw new StoreAccessException("invalidateAll failed - error count: " + errorCount, exception);
        }
        this.invalidateAllObserver.end(LowerCachingTierOperationsOutcome.InvalidateAllOutcome.SUCCESS);
    }

    @Override // org.ehcache.core.spi.store.tiering.LowerCachingTier
    public void invalidateAllWithHash(long hash) {
        this.invalidateAllWithHashObserver.begin();
        Map<K, OffHeapValueHolder<V>> removed = backingMap().removeAllWithHash((int) hash);
        for (Map.Entry<K, OffHeapValueHolder<V>> entry : removed.entrySet()) {
            notifyInvalidation(entry.getKey(), entry.getValue());
        }
        this.invalidateAllWithHashObserver.end(LowerCachingTierOperationsOutcome.InvalidateAllWithHashOutcome.SUCCESS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyInvalidation(K key, Store.ValueHolder<V> p) {
        CachingTier.InvalidationListener<K, V> invalidationListener = this.invalidationListener;
        if (invalidationListener != null) {
            invalidationListener.onInvalidation(key, p);
        }
    }

    @Override // org.ehcache.core.spi.store.tiering.LowerCachingTier
    public Store.ValueHolder<V> getAndRemove(final K key) throws StoreAccessException {
        this.getAndRemoveObserver.begin();
        checkKey(key);
        final AtomicReference<Store.ValueHolder<V>> valueHolderAtomicReference = new AtomicReference<>();
        BiFunction<K, OffHeapValueHolder<V>, OffHeapValueHolder<V>> computeFunction = new BiFunction<K, OffHeapValueHolder<V>, OffHeapValueHolder<V>>() { // from class: org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore.31
            @Override // org.ehcache.core.spi.function.BiFunction
            public /* bridge */ /* synthetic */ Object apply(Object x0, Object x1) {
                return apply((AnonymousClass31) x0, (OffHeapValueHolder) x1);
            }

            public OffHeapValueHolder<V> apply(K mappedKey, OffHeapValueHolder<V> mappedValue) {
                long now = AbstractOffHeapStore.this.timeSource.getTimeMillis();
                if (mappedValue == null || mappedValue.isExpired(now, TimeUnit.MILLISECONDS)) {
                    if (mappedValue != null) {
                        AbstractOffHeapStore.this.onExpirationInCachingTier(mappedValue, key);
                        return null;
                    }
                    return null;
                }
                mappedValue.detach();
                valueHolderAtomicReference.set(mappedValue);
                return null;
            }
        };
        try {
            backingMap().compute(key, computeFunction, false);
            Store.ValueHolder<V> result = valueHolderAtomicReference.get();
            if (result == null) {
                this.getAndRemoveObserver.end(LowerCachingTierOperationsOutcome.GetAndRemoveOutcome.MISS);
            } else {
                this.getAndRemoveObserver.end(LowerCachingTierOperationsOutcome.GetAndRemoveOutcome.HIT_REMOVED);
            }
            return result;
        } catch (RuntimeException re) {
            StorePassThroughException.handleRuntimeException(re);
            return null;
        }
    }

    @Override // org.ehcache.core.spi.store.tiering.LowerCachingTier
    public Store.ValueHolder<V> installMapping(final K key, final Function<K, Store.ValueHolder<V>> source) throws StoreAccessException {
        this.installMappingObserver.begin();
        BiFunction<K, OffHeapValueHolder<V>, OffHeapValueHolder<V>> computeFunction = new BiFunction<K, OffHeapValueHolder<V>, OffHeapValueHolder<V>>() { // from class: org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore.32
            @Override // org.ehcache.core.spi.function.BiFunction
            public /* bridge */ /* synthetic */ Object apply(Object x0, Object x1) {
                return apply((AnonymousClass32) x0, (OffHeapValueHolder) x1);
            }

            public OffHeapValueHolder<V> apply(K k, OffHeapValueHolder<V> offHeapValueHolder) {
                if (offHeapValueHolder != null) {
                    throw new AssertionError();
                }
                Store.ValueHolder<V> valueHolder = (Store.ValueHolder) source.apply(k);
                if (valueHolder != null) {
                    if (valueHolder.isExpired(AbstractOffHeapStore.this.timeSource.getTimeMillis(), TimeUnit.MILLISECONDS)) {
                        AbstractOffHeapStore.this.onExpirationInCachingTier(valueHolder, key);
                        return null;
                    }
                    return AbstractOffHeapStore.this.newTransferValueHolder(valueHolder);
                }
                return null;
            }
        };
        try {
            OffHeapValueHolder<V> computeResult = computeWithRetry(key, computeFunction, false);
            if (computeResult != null) {
                this.installMappingObserver.end(LowerCachingTierOperationsOutcome.InstallMappingOutcome.PUT);
            } else {
                this.installMappingObserver.end(LowerCachingTierOperationsOutcome.InstallMappingOutcome.NOOP);
            }
            return computeResult;
        } catch (RuntimeException re) {
            StorePassThroughException.handleRuntimeException(re);
            return null;
        }
    }

    private OffHeapValueHolder<V> computeWithRetry(K key, BiFunction<K, OffHeapValueHolder<V>, OffHeapValueHolder<V>> computeFunction, boolean fault) throws StoreAccessException {
        OffHeapValueHolder<V> computeResult = null;
        try {
            computeResult = backingMap().compute(key, computeFunction, fault);
        } catch (OversizeMappingException e) {
            try {
                try {
                    try {
                        evictionAdvisor().setSwitchedOn(false);
                        invokeValve();
                        computeResult = backingMap().compute(key, computeFunction, fault);
                        evictionAdvisor().setSwitchedOn(true);
                    } catch (OversizeMappingException e2) {
                        throw new StoreAccessException("The element with key '" + key + "' is too large to be stored in this offheap store.", e2);
                    }
                } catch (RuntimeException e3) {
                    StorePassThroughException.handleRuntimeException(e3);
                    evictionAdvisor().setSwitchedOn(true);
                }
            } catch (Throwable th) {
                evictionAdvisor().setSwitchedOn(true);
                throw th;
            }
        } catch (RuntimeException re) {
            StorePassThroughException.handleRuntimeException(re);
        }
        return computeResult;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean safeEquals(V existingValue, V computedValue) {
        return existingValue == computedValue || (existingValue != null && existingValue.equals(computedValue));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public OffHeapValueHolder<V> setAccessTimeAndExpiryThenReturnMapping(K key, OffHeapValueHolder<V> valueHolder, long now, StoreEventSink<K, V> eventSink) {
        Duration duration = Duration.ZERO;
        try {
            duration = this.expiry.getExpiryForAccess(key, valueHolder);
        } catch (RuntimeException re) {
            LOG.error("Expiry computation caused an exception - Expiry duration will be 0 ", (Throwable) re);
        }
        if (Duration.ZERO.equals(duration)) {
            onExpiration(key, valueHolder, eventSink);
            return null;
        }
        valueHolder.accessed(now, duration);
        valueHolder.writeBack();
        return valueHolder;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public OffHeapValueHolder<V> newUpdatedValueHolder(K key, V value, OffHeapValueHolder<V> existing, long now, StoreEventSink<K, V> eventSink) {
        eventSink.updated(key, existing, value);
        Duration duration = Duration.ZERO;
        try {
            duration = this.expiry.getExpiryForUpdate(key, existing, value);
        } catch (RuntimeException re) {
            LOG.error("Expiry computation caused an exception - Expiry duration will be 0 ", (Throwable) re);
        }
        if (Duration.ZERO.equals(duration)) {
            eventSink.expired(key, ValueSuppliers.supplierOf(value));
            return null;
        }
        if (duration == null) {
            return new BasicOffHeapValueHolder(backingMap().nextIdFor(key), value, now, existing.expirationTime(OffHeapValueHolder.TIME_UNIT));
        }
        if (duration.isInfinite()) {
            return new BasicOffHeapValueHolder(backingMap().nextIdFor(key), value, now, -1L);
        }
        return new BasicOffHeapValueHolder(backingMap().nextIdFor(key), value, now, safeExpireTime(now, duration));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public OffHeapValueHolder<V> newCreateValueHolder(K key, V value, long now, StoreEventSink<K, V> eventSink) {
        Duration duration = Duration.ZERO;
        try {
            duration = this.expiry.getExpiryForCreation(key, value);
        } catch (RuntimeException re) {
            LOG.error("Expiry computation caused an exception - Expiry duration will be 0 ", (Throwable) re);
        }
        if (Duration.ZERO.equals(duration)) {
            return null;
        }
        eventSink.created(key, value);
        if (duration.isInfinite()) {
            return new BasicOffHeapValueHolder(backingMap().nextIdFor(key), value, now, -1L);
        }
        return new BasicOffHeapValueHolder(backingMap().nextIdFor(key), value, now, safeExpireTime(now, duration));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public OffHeapValueHolder<V> newTransferValueHolder(Store.ValueHolder<V> valueHolder) {
        if ((valueHolder instanceof BinaryValueHolder) && ((BinaryValueHolder) valueHolder).isBinaryValueAvailable()) {
            return new BinaryOffHeapValueHolder(valueHolder.getId(), valueHolder.value(), ((BinaryValueHolder) valueHolder).getBinaryValue(), valueHolder.creationTime(OffHeapValueHolder.TIME_UNIT), valueHolder.expirationTime(OffHeapValueHolder.TIME_UNIT), valueHolder.lastAccessTime(OffHeapValueHolder.TIME_UNIT), valueHolder.hits());
        }
        return new BasicOffHeapValueHolder(valueHolder.getId(), valueHolder.value(), valueHolder.creationTime(OffHeapValueHolder.TIME_UNIT), valueHolder.expirationTime(OffHeapValueHolder.TIME_UNIT), valueHolder.lastAccessTime(OffHeapValueHolder.TIME_UNIT), valueHolder.hits());
    }

    private void invokeValve() throws StoreAccessException {
        AuthoritativeTier.InvalidationValve valve = this.valve;
        if (valve != null) {
            valve.invalidateAll();
        }
    }

    private static long safeExpireTime(long now, Duration duration) {
        long millis = OffHeapValueHolder.TIME_UNIT.convert(duration.getLength(), duration.getTimeUnit());
        if (millis == Long.MAX_VALUE) {
            return Long.MAX_VALUE;
        }
        long result = now + millis;
        if (result < 0) {
            return Long.MAX_VALUE;
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkKey(K keyObject) {
        if (keyObject == null) {
            throw new NullPointerException();
        }
        if (!this.keyType.isAssignableFrom(keyObject.getClass())) {
            throw new ClassCastException("Invalid key type, expected : " + this.keyType.getName() + " but was : " + keyObject.getClass().getName());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkValue(V valueObject) {
        if (valueObject == null) {
            throw new NullPointerException();
        }
        if (!this.valueType.isAssignableFrom(valueObject.getClass())) {
            throw new ClassCastException("Invalid value type, expected : " + this.valueType.getName() + " but was : " + valueObject.getClass().getName());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onExpirationInCachingTier(Store.ValueHolder<V> mappedValue, K key) {
        this.expirationObserver.begin();
        this.invalidationListener.onInvalidation(key, mappedValue);
        this.expirationObserver.end(StoreOperationOutcomes.ExpirationOutcome.SUCCESS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onExpiration(K mappedKey, Store.ValueHolder<V> mappedValue, StoreEventSink<K, V> eventSink) {
        this.expirationObserver.begin();
        eventSink.expired(mappedKey, mappedValue);
        this.invalidationListener.onInvalidation(mappedKey, mappedValue);
        this.expirationObserver.end(StoreOperationOutcomes.ExpirationOutcome.SUCCESS);
    }

    protected static <K, V> SwitchableEvictionAdvisor<K, OffHeapValueHolder<V>> wrap(EvictionAdvisor<? super K, ? super V> delegate) {
        return new OffHeapEvictionAdvisorWrapper(delegate);
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/offheap/AbstractOffHeapStore$OffHeapEvictionAdvisorWrapper.class */
    private static class OffHeapEvictionAdvisorWrapper<K, V> implements SwitchableEvictionAdvisor<K, OffHeapValueHolder<V>> {
        private final EvictionAdvisor<? super K, ? super V> delegate;
        private volatile boolean adviceEnabled;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // org.ehcache.config.EvictionAdvisor
        public /* bridge */ /* synthetic */ boolean adviseAgainstEviction(Object obj, Object x1) {
            return adviseAgainstEviction((OffHeapEvictionAdvisorWrapper<K, V>) obj, (OffHeapValueHolder) x1);
        }

        private OffHeapEvictionAdvisorWrapper(EvictionAdvisor<? super K, ? super V> delegate) {
            this.delegate = delegate;
        }

        public boolean adviseAgainstEviction(K key, OffHeapValueHolder<V> value) {
            try {
                return this.delegate.adviseAgainstEviction(key, value.value());
            } catch (Exception e) {
                AbstractOffHeapStore.LOG.error("Exception raised while running eviction advisor - Eviction will assume entry is NOT advised against eviction", (Throwable) e);
                return false;
            }
        }

        @Override // org.ehcache.impl.internal.store.offheap.SwitchableEvictionAdvisor
        public boolean isSwitchedOn() {
            return this.adviceEnabled;
        }

        @Override // org.ehcache.impl.internal.store.offheap.SwitchableEvictionAdvisor
        public void setSwitchedOn(boolean switchedOn) {
            this.adviceEnabled = switchedOn;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/offheap/AbstractOffHeapStore$BackingMapEvictionListener.class */
    protected static class BackingMapEvictionListener<K, V> implements EhcacheSegmentFactory.EhcacheSegment.EvictionListener<K, OffHeapValueHolder<V>> {
        private final StoreEventDispatcher<K, V> eventDispatcher;
        private final OperationObserver<StoreOperationOutcomes.EvictionOutcome> evictionObserver;
        private volatile CachingTier.InvalidationListener<K, V> invalidationListener;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // org.ehcache.impl.internal.store.offheap.factories.EhcacheSegmentFactory.EhcacheSegment.EvictionListener
        public /* bridge */ /* synthetic */ void onEviction(Object obj, Object x1) {
            onEviction((BackingMapEvictionListener<K, V>) obj, (OffHeapValueHolder) x1);
        }

        private BackingMapEvictionListener(StoreEventDispatcher<K, V> eventDispatcher, OperationObserver<StoreOperationOutcomes.EvictionOutcome> evictionObserver) {
            this.eventDispatcher = eventDispatcher;
            this.evictionObserver = evictionObserver;
            CachingTier.InvalidationListener<K, V> nullInvalidationListener = AbstractOffHeapStore.NULL_INVALIDATION_LISTENER;
            this.invalidationListener = nullInvalidationListener;
        }

        public void setInvalidationListener(CachingTier.InvalidationListener<K, V> invalidationListener) {
            if (invalidationListener == null) {
                throw new NullPointerException("invalidation listener cannot be null");
            }
            this.invalidationListener = invalidationListener;
        }

        public void onEviction(K key, OffHeapValueHolder<V> value) {
            this.evictionObserver.begin();
            StoreEventSink<K, V> eventSink = this.eventDispatcher.eventSink();
            try {
                eventSink.evicted(key, value);
                this.eventDispatcher.releaseEventSink(eventSink);
            } catch (RuntimeException re) {
                this.eventDispatcher.releaseEventSinkAfterFailure(eventSink, re);
            }
            this.invalidationListener.onInvalidation(key, value);
            this.evictionObserver.end(StoreOperationOutcomes.EvictionOutcome.SUCCESS);
        }
    }
}
