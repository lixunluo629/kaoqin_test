package org.ehcache.impl.internal.store.heap;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import ch.qos.logback.core.pattern.parser.Parser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.ehcache.Cache;
import org.ehcache.ValueSupplier;
import org.ehcache.config.Eviction;
import org.ehcache.config.EvictionAdvisor;
import org.ehcache.config.ResourcePools;
import org.ehcache.config.ResourceType;
import org.ehcache.config.SizedResourcePool;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.core.CacheConfigurationChangeEvent;
import org.ehcache.core.CacheConfigurationChangeListener;
import org.ehcache.core.CacheConfigurationProperty;
import org.ehcache.core.collections.ConcurrentWeakIdentityHashMap;
import org.ehcache.core.events.NullStoreEventDispatcher;
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
import org.ehcache.core.spi.store.heap.LimitExceededException;
import org.ehcache.core.spi.store.heap.SizeOfEngine;
import org.ehcache.core.spi.store.heap.SizeOfEngineProvider;
import org.ehcache.core.spi.store.tiering.CachingTier;
import org.ehcache.core.spi.store.tiering.HigherCachingTier;
import org.ehcache.core.spi.time.TimeSource;
import org.ehcache.core.spi.time.TimeSourceService;
import org.ehcache.core.statistics.CachingTierOperationOutcomes;
import org.ehcache.core.statistics.HigherCachingTierOperationOutcomes;
import org.ehcache.core.statistics.StoreOperationOutcomes;
import org.ehcache.core.statistics.TierOperationOutcomes;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expiry;
import org.ehcache.impl.copy.IdentityCopier;
import org.ehcache.impl.copy.SerializingCopier;
import org.ehcache.impl.internal.events.ScopedStoreEventDispatcher;
import org.ehcache.impl.internal.sizeof.NoopSizeOfEngine;
import org.ehcache.impl.internal.store.BinaryValueHolder;
import org.ehcache.impl.internal.store.heap.holders.CopiedOnHeapValueHolder;
import org.ehcache.impl.internal.store.heap.holders.OnHeapValueHolder;
import org.ehcache.impl.internal.store.heap.holders.SerializedOnHeapValueHolder;
import org.ehcache.impl.serialization.TransientStateRepository;
import org.ehcache.sizeof.annotations.IgnoreSizeOf;
import org.ehcache.spi.copy.Copier;
import org.ehcache.spi.copy.CopyProvider;
import org.ehcache.spi.serialization.Serializer;
import org.ehcache.spi.serialization.StatefulSerializer;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceConfiguration;
import org.ehcache.spi.service.ServiceDependencies;
import org.ehcache.spi.service.ServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.offheapstore.util.FindbugsSuppressWarnings;
import org.terracotta.statistics.MappedOperationStatistic;
import org.terracotta.statistics.StatisticBuilder;
import org.terracotta.statistics.StatisticsManager;
import org.terracotta.statistics.observer.OperationObserver;
import redis.clients.jedis.Protocol;

/*  JADX ERROR: NullPointerException in pass: ClassModifier
    java.lang.NullPointerException
    */
/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/heap/OnHeapStore.class */
public class OnHeapStore<K, V> implements Store<K, V>, HigherCachingTier<K, V> {
    private static final String STATISTICS_TAG = "OnHeap";
    private static final int ATTEMPT_RATIO = 4;
    private static final int EVICTION_RATIO = 2;
    static final int SAMPLE_SIZE = 8;
    private volatile Backend<K, V> map;
    private final Class<K> keyType;
    private final Class<V> valueType;
    private final Copier<V> valueCopier;
    private final SizeOfEngine sizeOfEngine;
    private final boolean byteSized;
    private volatile long capacity;
    private final EvictionAdvisor<? super K, ? super V> evictionAdvisor;
    private final Expiry<? super K, ? super V> expiry;
    private final TimeSource timeSource;
    private final StoreEventDispatcher<K, V> storeEventDispatcher;
    private volatile CachingTier.InvalidationListener<K, V> invalidationListener = (CachingTier.InvalidationListener<K, V>) NULL_INVALIDATION_LISTENER;
    private CacheConfigurationChangeListener cacheConfigurationChangeListener = new CacheConfigurationChangeListener() { // from class: org.ehcache.impl.internal.store.heap.OnHeapStore.4
        /* JADX WARN: Failed to check method for inline after forced processorg.ehcache.impl.internal.store.heap.OnHeapStore.access$102(org.ehcache.impl.internal.store.heap.OnHeapStore, long):long */
        @Override // org.ehcache.core.CacheConfigurationChangeListener
        public void cacheConfigurationChange(CacheConfigurationChangeEvent event) {
            if (event.getProperty().equals(CacheConfigurationProperty.UPDATE_SIZE)) {
                ResourcePools updatedPools = (ResourcePools) event.getNewValue();
                ResourcePools configuredPools = (ResourcePools) event.getOldValue();
                if (((SizedResourcePool) updatedPools.getPoolForResource(ResourceType.Core.HEAP)).getSize() != ((SizedResourcePool) configuredPools.getPoolForResource(ResourceType.Core.HEAP)).getSize()) {
                    OnHeapStore.LOG.info("Updating size to: {}", Long.valueOf(((SizedResourcePool) updatedPools.getPoolForResource(ResourceType.Core.HEAP)).getSize()));
                    SizedResourcePool pool = (SizedResourcePool) updatedPools.getPoolForResource(ResourceType.Core.HEAP);
                    if (pool.getUnit() instanceof MemoryUnit) {
                        OnHeapStore.access$102(OnHeapStore.this, ((MemoryUnit) pool.getUnit()).toBytes(pool.getSize()));
                    } else {
                        OnHeapStore.access$102(OnHeapStore.this, pool.getSize());
                    }
                }
            }
        }
    };
    private final OperationObserver<StoreOperationOutcomes.GetOutcome> getObserver;
    private final OperationObserver<StoreOperationOutcomes.PutOutcome> putObserver;
    private final OperationObserver<StoreOperationOutcomes.RemoveOutcome> removeObserver;
    private final OperationObserver<StoreOperationOutcomes.PutIfAbsentOutcome> putIfAbsentObserver;
    private final OperationObserver<StoreOperationOutcomes.ConditionalRemoveOutcome> conditionalRemoveObserver;
    private final OperationObserver<StoreOperationOutcomes.ReplaceOutcome> replaceObserver;
    private final OperationObserver<StoreOperationOutcomes.ConditionalReplaceOutcome> conditionalReplaceObserver;
    private final OperationObserver<StoreOperationOutcomes.ComputeOutcome> computeObserver;
    private final OperationObserver<StoreOperationOutcomes.ComputeIfAbsentOutcome> computeIfAbsentObserver;
    private final OperationObserver<StoreOperationOutcomes.EvictionOutcome> evictionObserver;
    private final OperationObserver<StoreOperationOutcomes.ExpirationOutcome> expirationObserver;
    private final OperationObserver<CachingTierOperationOutcomes.GetOrComputeIfAbsentOutcome> getOrComputeIfAbsentObserver;
    private final OperationObserver<CachingTierOperationOutcomes.InvalidateOutcome> invalidateObserver;
    private final OperationObserver<CachingTierOperationOutcomes.InvalidateAllOutcome> invalidateAllObserver;
    private final OperationObserver<CachingTierOperationOutcomes.InvalidateAllWithHashOutcome> invalidateAllWithHashObserver;
    private final OperationObserver<HigherCachingTierOperationOutcomes.SilentInvalidateOutcome> silentInvalidateObserver;
    private final OperationObserver<HigherCachingTierOperationOutcomes.SilentInvalidateAllOutcome> silentInvalidateAllObserver;
    private final OperationObserver<HigherCachingTierOperationOutcomes.SilentInvalidateAllWithHashOutcome> silentInvalidateAllWithHashObserver;
    private static final Logger LOG = LoggerFactory.getLogger((Class<?>) OnHeapStore.class);
    private static final EvictionAdvisor<Object, OnHeapValueHolder<?>> EVICTION_ADVISOR = new EvictionAdvisor<Object, OnHeapValueHolder<?>>() { // from class: org.ehcache.impl.internal.store.heap.OnHeapStore.1
        @Override // org.ehcache.config.EvictionAdvisor
        public boolean adviseAgainstEviction(Object key, OnHeapValueHolder<?> value) {
            return value.evictionAdvice();
        }
    };
    private static final Comparator<Store.ValueHolder<?>> EVICTION_PRIORITIZER = new Comparator<Store.ValueHolder<?>>() { // from class: org.ehcache.impl.internal.store.heap.OnHeapStore.2
        @Override // java.util.Comparator
        public int compare(Store.ValueHolder<?> t, Store.ValueHolder<?> u) {
            if (t instanceof Fault) {
                return -1;
            }
            if (u instanceof Fault) {
                return 1;
            }
            return Long.signum(u.lastAccessTime(TimeUnit.NANOSECONDS) - t.lastAccessTime(TimeUnit.NANOSECONDS));
        }
    };
    private static final CachingTier.InvalidationListener<?, ?> NULL_INVALIDATION_LISTENER = new CachingTier.InvalidationListener<Object, Object>() { // from class: org.ehcache.impl.internal.store.heap.OnHeapStore.3
        @Override // org.ehcache.core.spi.store.tiering.CachingTier.InvalidationListener
        public void onInvalidation(Object key, Store.ValueHolder<Object> valueHolder) {
        }
    };
    private static final NullaryFunction<Boolean> REPLACE_EQUALS_TRUE = new NullaryFunction<Boolean>() { // from class: org.ehcache.impl.internal.store.heap.OnHeapStore.5
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.ehcache.core.spi.function.NullaryFunction
        public Boolean apply() {
            return Boolean.TRUE;
        }
    };

    /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:112)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ long access$102(org.ehcache.impl.internal.store.heap.OnHeapStore r6, long r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.capacity = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: org.ehcache.impl.internal.store.heap.OnHeapStore.access$102(org.ehcache.impl.internal.store.heap.OnHeapStore, long):long");
    }

    static {
    }

    public OnHeapStore(Store.Configuration<K, V> configuration, TimeSource timeSource, Copier<K> copier, Copier<V> copier2, SizeOfEngine sizeOfEngine, StoreEventDispatcher<K, V> storeEventDispatcher) {
        if (copier == null) {
            throw new NullPointerException("keyCopier must not be null");
        }
        if (copier2 == null) {
            throw new NullPointerException("valueCopier must not be null");
        }
        SizedResourcePool sizedResourcePool = (SizedResourcePool) configuration.getResourcePools().getPoolForResource(ResourceType.Core.HEAP);
        if (sizedResourcePool == null) {
            throw new IllegalArgumentException("OnHeap store must be configured with a resource of type 'heap'");
        }
        if (timeSource == null) {
            throw new NullPointerException("timeSource must not be null");
        }
        if (sizeOfEngine == null) {
            throw new NullPointerException("sizeOfEngine must not be null");
        }
        this.sizeOfEngine = sizeOfEngine;
        this.byteSized = !(this.sizeOfEngine instanceof NoopSizeOfEngine);
        this.capacity = this.byteSized ? ((MemoryUnit) sizedResourcePool.getUnit()).toBytes(sizedResourcePool.getSize()) : sizedResourcePool.getSize();
        this.timeSource = timeSource;
        if (configuration.getEvictionAdvisor() == null) {
            this.evictionAdvisor = Eviction.noAdvice();
        } else {
            this.evictionAdvisor = configuration.getEvictionAdvisor();
        }
        this.keyType = configuration.getKeyType();
        this.valueType = configuration.getValueType();
        this.expiry = configuration.getExpiry();
        this.valueCopier = copier2;
        this.storeEventDispatcher = storeEventDispatcher;
        if (copier instanceof IdentityCopier) {
            this.map = new SimpleBackend(this.byteSized);
        } else {
            this.map = new KeyCopyBackend(this.byteSized, copier);
        }
        this.getObserver = StatisticBuilder.operation(StoreOperationOutcomes.GetOutcome.class).named(BeanUtil.PREFIX_GETTER_GET).of(this).tag(STATISTICS_TAG).build();
        this.putObserver = StatisticBuilder.operation(StoreOperationOutcomes.PutOutcome.class).named("put").of(this).tag(STATISTICS_TAG).build();
        this.removeObserver = StatisticBuilder.operation(StoreOperationOutcomes.RemoveOutcome.class).named(Protocol.SENTINEL_REMOVE).of(this).tag(STATISTICS_TAG).build();
        this.putIfAbsentObserver = StatisticBuilder.operation(StoreOperationOutcomes.PutIfAbsentOutcome.class).named("putIfAbsent").of(this).tag(STATISTICS_TAG).build();
        this.conditionalRemoveObserver = StatisticBuilder.operation(StoreOperationOutcomes.ConditionalRemoveOutcome.class).named("conditionalRemove").of(this).tag(STATISTICS_TAG).build();
        this.replaceObserver = StatisticBuilder.operation(StoreOperationOutcomes.ReplaceOutcome.class).named(Parser.REPLACE_CONVERTER_WORD).of(this).tag(STATISTICS_TAG).build();
        this.conditionalReplaceObserver = StatisticBuilder.operation(StoreOperationOutcomes.ConditionalReplaceOutcome.class).named("conditionalReplace").of(this).tag(STATISTICS_TAG).build();
        this.computeObserver = StatisticBuilder.operation(StoreOperationOutcomes.ComputeOutcome.class).named("compute").of(this).tag(STATISTICS_TAG).build();
        this.computeIfAbsentObserver = StatisticBuilder.operation(StoreOperationOutcomes.ComputeIfAbsentOutcome.class).named("computeIfAbsent").of(this).tag(STATISTICS_TAG).build();
        this.evictionObserver = StatisticBuilder.operation(StoreOperationOutcomes.EvictionOutcome.class).named("eviction").of(this).tag(STATISTICS_TAG).build();
        this.expirationObserver = StatisticBuilder.operation(StoreOperationOutcomes.ExpirationOutcome.class).named("expiration").of(this).tag(STATISTICS_TAG).build();
        this.getOrComputeIfAbsentObserver = StatisticBuilder.operation(CachingTierOperationOutcomes.GetOrComputeIfAbsentOutcome.class).named("getOrComputeIfAbsent").of(this).tag(STATISTICS_TAG).build();
        this.invalidateObserver = StatisticBuilder.operation(CachingTierOperationOutcomes.InvalidateOutcome.class).named("invalidate").of(this).tag(STATISTICS_TAG).build();
        this.invalidateAllObserver = StatisticBuilder.operation(CachingTierOperationOutcomes.InvalidateAllOutcome.class).named("invalidateAll").of(this).tag(STATISTICS_TAG).build();
        this.invalidateAllWithHashObserver = StatisticBuilder.operation(CachingTierOperationOutcomes.InvalidateAllWithHashOutcome.class).named("invalidateAllWithHash").of(this).tag(STATISTICS_TAG).build();
        this.silentInvalidateObserver = StatisticBuilder.operation(HigherCachingTierOperationOutcomes.SilentInvalidateOutcome.class).named("silentInvalidate").of(this).tag(STATISTICS_TAG).build();
        this.silentInvalidateAllObserver = StatisticBuilder.operation(HigherCachingTierOperationOutcomes.SilentInvalidateAllOutcome.class).named("silentInvalidateAll").of(this).tag(STATISTICS_TAG).build();
        this.silentInvalidateAllWithHashObserver = StatisticBuilder.operation(HigherCachingTierOperationOutcomes.SilentInvalidateAllWithHashOutcome.class).named("silentInvalidateAllWithHash").of(this).tag(STATISTICS_TAG).build();
        HashSet hashSet = new HashSet(Arrays.asList(STATISTICS_TAG, "tier"));
        Map mapSingletonMap = Collections.singletonMap("discriminator", STATISTICS_TAG);
        StatisticsManager.createPassThroughStatistic(this, "mappings", hashSet, mapSingletonMap, new Callable<Number>() { // from class: org.ehcache.impl.internal.store.heap.OnHeapStore.6
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Number call() throws Exception {
                return Long.valueOf(OnHeapStore.this.map.mappingCount());
            }
        });
        StatisticsManager.createPassThroughStatistic(this, "occupiedMemory", hashSet, mapSingletonMap, new Callable<Number>() { // from class: org.ehcache.impl.internal.store.heap.OnHeapStore.7
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Number call() throws Exception {
                if (OnHeapStore.this.byteSized) {
                    return Long.valueOf(OnHeapStore.this.map.byteSize());
                }
                return -1L;
            }
        });
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.ValueHolder<V> get(K key) throws StoreAccessException {
        checkKey(key);
        return internalGet(key, true);
    }

    private OnHeapValueHolder<V> internalGet(K key, boolean updateAccess) throws StoreAccessException {
        this.getObserver.begin();
        try {
            OnHeapValueHolder<V> mapping = getQuiet(key);
            if (mapping == null) {
                this.getObserver.end(StoreOperationOutcomes.GetOutcome.MISS);
                return null;
            }
            if (updateAccess) {
                setAccessTimeAndExpiryThenReturnMappingOutsideLock(key, mapping, this.timeSource.getTimeMillis());
            }
            this.getObserver.end(StoreOperationOutcomes.GetOutcome.HIT);
            return mapping;
        } catch (RuntimeException re) {
            StorePassThroughException.handleRuntimeException(re);
            return null;
        }
    }

    private OnHeapValueHolder<V> getQuiet(K key) throws StoreAccessException {
        try {
            OnHeapValueHolder<V> mapping = this.map.get(key);
            if (mapping == null) {
                return null;
            }
            if (mapping.isExpired(this.timeSource.getTimeMillis(), TimeUnit.MILLISECONDS)) {
                expireMappingUnderLock(key, mapping);
                return null;
            }
            return mapping;
        } catch (RuntimeException re) {
            StorePassThroughException.handleRuntimeException(re);
            return null;
        }
    }

    @Override // org.ehcache.core.spi.store.Store
    public boolean containsKey(K key) throws StoreAccessException {
        checkKey(key);
        return getQuiet(key) != null;
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.PutStatus put(final K key, final V value) throws StoreAccessException {
        this.putObserver.begin();
        checkKey(key);
        checkValue(value);
        final long now = this.timeSource.getTimeMillis();
        final AtomicReference<StoreOperationOutcomes.PutOutcome> statOutcome = new AtomicReference<>(StoreOperationOutcomes.PutOutcome.NOOP);
        final StoreEventSink<K, V> eventSink = this.storeEventDispatcher.eventSink();
        try {
            this.map.compute(key, new BiFunction<K, OnHeapValueHolder<V>, OnHeapValueHolder<V>>() { // from class: org.ehcache.impl.internal.store.heap.OnHeapStore.8
                @Override // org.ehcache.core.spi.function.BiFunction
                public /* bridge */ /* synthetic */ Object apply(Object x0, Object x1) {
                    return apply((AnonymousClass8) x0, (OnHeapValueHolder) x1);
                }

                public OnHeapValueHolder<V> apply(K mappedKey, OnHeapValueHolder<V> mappedValue) {
                    if (mappedValue != null && mappedValue.isExpired(now, TimeUnit.MILLISECONDS)) {
                        OnHeapStore.this.updateUsageInBytesIfRequired(-mappedValue.size());
                        mappedValue = null;
                    }
                    if (mappedValue == null) {
                        OnHeapValueHolder<V> newValue = OnHeapStore.this.newCreateValueHolder(key, value, now, eventSink);
                        if (newValue != null) {
                            OnHeapStore.this.updateUsageInBytesIfRequired(newValue.size());
                            statOutcome.set(StoreOperationOutcomes.PutOutcome.PUT);
                        }
                        return newValue;
                    }
                    OnHeapValueHolder<V> newValue2 = OnHeapStore.this.newUpdateValueHolder(key, mappedValue, value, now, eventSink);
                    if (newValue2 != null) {
                        OnHeapStore.this.updateUsageInBytesIfRequired(newValue2.size() - mappedValue.size());
                    } else {
                        OnHeapStore.this.updateUsageInBytesIfRequired(-mappedValue.size());
                    }
                    statOutcome.set(StoreOperationOutcomes.PutOutcome.REPLACED);
                    return newValue2;
                }
            });
            this.storeEventDispatcher.releaseEventSink(eventSink);
            enforceCapacity();
            StoreOperationOutcomes.PutOutcome outcome = statOutcome.get();
            this.putObserver.end(outcome);
            switch (outcome) {
                case REPLACED:
                    return Store.PutStatus.UPDATE;
                case PUT:
                    return Store.PutStatus.PUT;
                case NOOP:
                    return Store.PutStatus.NOOP;
                default:
                    throw new AssertionError("Unknown enum value " + outcome);
            }
        } catch (RuntimeException re) {
            this.storeEventDispatcher.releaseEventSinkAfterFailure(eventSink, re);
            StorePassThroughException.handleRuntimeException(re);
            return Store.PutStatus.NOOP;
        }
    }

    @Override // org.ehcache.core.spi.store.Store
    public boolean remove(K key) throws StoreAccessException {
        this.removeObserver.begin();
        checkKey(key);
        final StoreEventSink<K, V> eventSink = this.storeEventDispatcher.eventSink();
        final long now = this.timeSource.getTimeMillis();
        try {
            final AtomicReference<StoreOperationOutcomes.RemoveOutcome> statisticOutcome = new AtomicReference<>(StoreOperationOutcomes.RemoveOutcome.MISS);
            this.map.computeIfPresent(key, new BiFunction<K, OnHeapValueHolder<V>, OnHeapValueHolder<V>>() { // from class: org.ehcache.impl.internal.store.heap.OnHeapStore.9
                @Override // org.ehcache.core.spi.function.BiFunction
                public /* bridge */ /* synthetic */ Object apply(Object x0, Object x1) {
                    return apply((AnonymousClass9) x0, (OnHeapValueHolder) x1);
                }

                public OnHeapValueHolder<V> apply(K mappedKey, OnHeapValueHolder<V> mappedValue) {
                    OnHeapStore.this.updateUsageInBytesIfRequired(-mappedValue.size());
                    if (mappedValue.isExpired(now, TimeUnit.MILLISECONDS)) {
                        OnHeapStore.this.fireOnExpirationEvent(mappedKey, mappedValue, eventSink);
                        return null;
                    }
                    statisticOutcome.set(StoreOperationOutcomes.RemoveOutcome.REMOVED);
                    eventSink.removed(mappedKey, mappedValue);
                    return null;
                }
            });
            this.storeEventDispatcher.releaseEventSink(eventSink);
            StoreOperationOutcomes.RemoveOutcome outcome = statisticOutcome.get();
            this.removeObserver.end(outcome);
            switch (outcome) {
                case REMOVED:
                    return true;
                case MISS:
                    return false;
                default:
                    throw new AssertionError("Unknown enum value " + outcome);
            }
        } catch (RuntimeException re) {
            this.storeEventDispatcher.releaseEventSinkAfterFailure(eventSink, re);
            StorePassThroughException.handleRuntimeException(re);
            return false;
        }
        this.storeEventDispatcher.releaseEventSinkAfterFailure(eventSink, re);
        StorePassThroughException.handleRuntimeException(re);
        return false;
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.ValueHolder<V> putIfAbsent(K key, V value) throws StoreAccessException {
        return putIfAbsent(key, value, false);
    }

    private OnHeapValueHolder<V> putIfAbsent(final K key, final V value, boolean returnCurrentMapping) throws StoreAccessException {
        this.putIfAbsentObserver.begin();
        checkKey(key);
        checkValue(value);
        final AtomicReference<OnHeapValueHolder<V>> returnValue = new AtomicReference<>(null);
        final AtomicBoolean entryActuallyAdded = new AtomicBoolean();
        final long now = this.timeSource.getTimeMillis();
        final StoreEventSink<K, V> eventSink = this.storeEventDispatcher.eventSink();
        try {
            OnHeapValueHolder<V> inCache = this.map.compute(key, new BiFunction<K, OnHeapValueHolder<V>, OnHeapValueHolder<V>>() { // from class: org.ehcache.impl.internal.store.heap.OnHeapStore.10
                @Override // org.ehcache.core.spi.function.BiFunction
                public /* bridge */ /* synthetic */ Object apply(Object x0, Object x1) {
                    return apply((AnonymousClass10) x0, (OnHeapValueHolder) x1);
                }

                public OnHeapValueHolder<V> apply(K mappedKey, OnHeapValueHolder<V> mappedValue) {
                    if (mappedValue == null || mappedValue.isExpired(now, TimeUnit.MILLISECONDS)) {
                        if (mappedValue != null) {
                            OnHeapStore.this.updateUsageInBytesIfRequired(-mappedValue.size());
                            OnHeapStore.this.fireOnExpirationEvent(mappedKey, mappedValue, eventSink);
                        }
                        OnHeapValueHolder<V> holder = OnHeapStore.this.newCreateValueHolder(key, value, now, eventSink);
                        if (holder != null) {
                            OnHeapStore.this.updateUsageInBytesIfRequired(holder.size());
                        }
                        entryActuallyAdded.set(holder != null);
                        return holder;
                    }
                    returnValue.set(mappedValue);
                    OnHeapValueHolder<V> holder2 = OnHeapStore.this.setAccessTimeAndExpiryThenReturnMappingUnderLock(key, mappedValue, now, eventSink);
                    if (holder2 == null) {
                        OnHeapStore.this.updateUsageInBytesIfRequired(-mappedValue.size());
                    }
                    return holder2;
                }
            });
            this.storeEventDispatcher.releaseEventSink(eventSink);
            if (entryActuallyAdded.get()) {
                enforceCapacity();
                this.putIfAbsentObserver.end(StoreOperationOutcomes.PutIfAbsentOutcome.PUT);
            } else {
                this.putIfAbsentObserver.end(StoreOperationOutcomes.PutIfAbsentOutcome.HIT);
            }
            if (returnCurrentMapping) {
                return inCache;
            }
        } catch (RuntimeException re) {
            this.storeEventDispatcher.releaseEventSinkAfterFailure(eventSink, re);
            StorePassThroughException.handleRuntimeException(re);
        }
        return returnValue.get();
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.RemoveStatus remove(final K key, final V value) throws StoreAccessException {
        this.conditionalRemoveObserver.begin();
        checkKey(key);
        checkValue(value);
        final AtomicReference<Store.RemoveStatus> outcome = new AtomicReference<>(Store.RemoveStatus.KEY_MISSING);
        final StoreEventSink<K, V> eventSink = this.storeEventDispatcher.eventSink();
        try {
            this.map.computeIfPresent(key, new BiFunction<K, OnHeapValueHolder<V>, OnHeapValueHolder<V>>() { // from class: org.ehcache.impl.internal.store.heap.OnHeapStore.11
                @Override // org.ehcache.core.spi.function.BiFunction
                public /* bridge */ /* synthetic */ Object apply(Object x0, Object x1) {
                    return apply((AnonymousClass11) x0, (OnHeapValueHolder) x1);
                }

                public OnHeapValueHolder<V> apply(K mappedKey, OnHeapValueHolder<V> mappedValue) {
                    long now = OnHeapStore.this.timeSource.getTimeMillis();
                    if (mappedValue.isExpired(now, TimeUnit.MILLISECONDS)) {
                        OnHeapStore.this.updateUsageInBytesIfRequired(-mappedValue.size());
                        OnHeapStore.this.fireOnExpirationEvent(mappedKey, mappedValue, eventSink);
                        return null;
                    }
                    if (value.equals(mappedValue.value())) {
                        OnHeapStore.this.updateUsageInBytesIfRequired(-mappedValue.size());
                        eventSink.removed(mappedKey, mappedValue);
                        outcome.set(Store.RemoveStatus.REMOVED);
                        return null;
                    }
                    outcome.set(Store.RemoveStatus.KEY_PRESENT);
                    OnHeapValueHolder<V> holder = OnHeapStore.this.setAccessTimeAndExpiryThenReturnMappingUnderLock(key, mappedValue, now, eventSink);
                    if (holder == null) {
                        OnHeapStore.this.updateUsageInBytesIfRequired(-mappedValue.size());
                    }
                    return holder;
                }
            });
            this.storeEventDispatcher.releaseEventSink(eventSink);
            Store.RemoveStatus removeStatus = outcome.get();
            switch (removeStatus) {
                case REMOVED:
                    this.conditionalRemoveObserver.end(StoreOperationOutcomes.ConditionalRemoveOutcome.REMOVED);
                    break;
                case KEY_MISSING:
                case KEY_PRESENT:
                    this.conditionalRemoveObserver.end(StoreOperationOutcomes.ConditionalRemoveOutcome.MISS);
                    break;
            }
            return removeStatus;
        } catch (RuntimeException re) {
            this.storeEventDispatcher.releaseEventSinkAfterFailure(eventSink, re);
            StorePassThroughException.handleRuntimeException(re);
            return Store.RemoveStatus.KEY_MISSING;
        }
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.ValueHolder<V> replace(final K key, final V value) throws StoreAccessException {
        this.replaceObserver.begin();
        checkKey(key);
        checkValue(value);
        final AtomicReference<OnHeapValueHolder<V>> returnValue = new AtomicReference<>(null);
        final StoreEventSink<K, V> eventSink = this.storeEventDispatcher.eventSink();
        try {
            this.map.computeIfPresent(key, new BiFunction<K, OnHeapValueHolder<V>, OnHeapValueHolder<V>>() { // from class: org.ehcache.impl.internal.store.heap.OnHeapStore.12
                @Override // org.ehcache.core.spi.function.BiFunction
                public /* bridge */ /* synthetic */ Object apply(Object x0, Object x1) {
                    return apply((AnonymousClass12) x0, (OnHeapValueHolder) x1);
                }

                public OnHeapValueHolder<V> apply(K mappedKey, OnHeapValueHolder<V> mappedValue) {
                    long now = OnHeapStore.this.timeSource.getTimeMillis();
                    if (mappedValue.isExpired(now, TimeUnit.MILLISECONDS)) {
                        OnHeapStore.this.updateUsageInBytesIfRequired(-mappedValue.size());
                        OnHeapStore.this.fireOnExpirationEvent(mappedKey, mappedValue, eventSink);
                        return null;
                    }
                    returnValue.set(mappedValue);
                    OnHeapValueHolder<V> holder = OnHeapStore.this.newUpdateValueHolder(key, mappedValue, value, now, eventSink);
                    if (holder != null) {
                        OnHeapStore.this.updateUsageInBytesIfRequired(holder.size() - mappedValue.size());
                    } else {
                        OnHeapStore.this.updateUsageInBytesIfRequired(-mappedValue.size());
                    }
                    return holder;
                }
            });
            OnHeapValueHolder<V> valueHolder = returnValue.get();
            this.storeEventDispatcher.releaseEventSink(eventSink);
            enforceCapacity();
            if (valueHolder != null) {
                this.replaceObserver.end(StoreOperationOutcomes.ReplaceOutcome.REPLACED);
            } else {
                this.replaceObserver.end(StoreOperationOutcomes.ReplaceOutcome.MISS);
            }
        } catch (RuntimeException re) {
            this.storeEventDispatcher.releaseEventSinkAfterFailure(eventSink, re);
            StorePassThroughException.handleRuntimeException(re);
        }
        return returnValue.get();
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.ReplaceStatus replace(final K key, final V oldValue, final V newValue) throws StoreAccessException {
        this.conditionalReplaceObserver.begin();
        checkKey(key);
        checkValue(oldValue);
        checkValue(newValue);
        final StoreEventSink<K, V> eventSink = this.storeEventDispatcher.eventSink();
        final AtomicReference<Store.ReplaceStatus> outcome = new AtomicReference<>(Store.ReplaceStatus.MISS_NOT_PRESENT);
        try {
            this.map.computeIfPresent(key, new BiFunction<K, OnHeapValueHolder<V>, OnHeapValueHolder<V>>() { // from class: org.ehcache.impl.internal.store.heap.OnHeapStore.13
                @Override // org.ehcache.core.spi.function.BiFunction
                public /* bridge */ /* synthetic */ Object apply(Object x0, Object x1) {
                    return apply((AnonymousClass13) x0, (OnHeapValueHolder) x1);
                }

                public OnHeapValueHolder<V> apply(K mappedKey, OnHeapValueHolder<V> mappedValue) {
                    long now = OnHeapStore.this.timeSource.getTimeMillis();
                    V existingValue = mappedValue.value();
                    if (mappedValue.isExpired(now, TimeUnit.MILLISECONDS)) {
                        OnHeapStore.this.fireOnExpirationEvent(mappedKey, mappedValue, eventSink);
                        OnHeapStore.this.updateUsageInBytesIfRequired(-mappedValue.size());
                        return null;
                    }
                    if (oldValue.equals(existingValue)) {
                        outcome.set(Store.ReplaceStatus.HIT);
                        OnHeapValueHolder<V> holder = OnHeapStore.this.newUpdateValueHolder(key, mappedValue, newValue, now, eventSink);
                        if (holder != null) {
                            OnHeapStore.this.updateUsageInBytesIfRequired(holder.size() - mappedValue.size());
                        } else {
                            OnHeapStore.this.updateUsageInBytesIfRequired(-mappedValue.size());
                        }
                        return holder;
                    }
                    outcome.set(Store.ReplaceStatus.MISS_PRESENT);
                    OnHeapValueHolder<V> holder2 = OnHeapStore.this.setAccessTimeAndExpiryThenReturnMappingUnderLock(key, mappedValue, now, eventSink);
                    if (holder2 == null) {
                        OnHeapStore.this.updateUsageInBytesIfRequired(-mappedValue.size());
                    }
                    return holder2;
                }
            });
            this.storeEventDispatcher.releaseEventSink(eventSink);
            enforceCapacity();
            Store.ReplaceStatus replaceStatus = outcome.get();
            switch (replaceStatus) {
                case HIT:
                    this.conditionalReplaceObserver.end(StoreOperationOutcomes.ConditionalReplaceOutcome.REPLACED);
                    break;
                case MISS_PRESENT:
                case MISS_NOT_PRESENT:
                    this.conditionalReplaceObserver.end(StoreOperationOutcomes.ConditionalReplaceOutcome.MISS);
                    break;
                default:
                    throw new AssertionError("Unknown enum value " + replaceStatus);
            }
            return replaceStatus;
        } catch (RuntimeException re) {
            this.storeEventDispatcher.releaseEventSinkAfterFailure(eventSink, re);
            StorePassThroughException.handleRuntimeException(re);
            return Store.ReplaceStatus.MISS_NOT_PRESENT;
        }
    }

    @Override // org.ehcache.core.spi.store.Store
    public void clear() {
        this.map = this.map.clear();
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.Iterator<Cache.Entry<K, Store.ValueHolder<V>>> iterator() {
        return new Store.Iterator<Cache.Entry<K, Store.ValueHolder<V>>>() { // from class: org.ehcache.impl.internal.store.heap.OnHeapStore.14
            private final Iterator<Map.Entry<K, OnHeapValueHolder<V>>> it;

            {
                this.it = OnHeapStore.this.map.entrySetIterator();
            }

            @Override // org.ehcache.core.spi.store.Store.Iterator
            public boolean hasNext() {
                return this.it.hasNext();
            }

            @Override // org.ehcache.core.spi.store.Store.Iterator
            public Cache.Entry<K, Store.ValueHolder<V>> next() throws StoreAccessException {
                Map.Entry<K, OnHeapValueHolder<V>> next = this.it.next();
                final K key = next.getKey();
                final OnHeapValueHolder<V> value = next.getValue();
                return new Cache.Entry<K, Store.ValueHolder<V>>() { // from class: org.ehcache.impl.internal.store.heap.OnHeapStore.14.1
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

    @Override // org.ehcache.core.spi.store.tiering.CachingTier
    public Store.ValueHolder<V> getOrComputeIfAbsent(final K key, final Function<K, Store.ValueHolder<V>> source) throws StoreAccessException {
        try {
            this.getOrComputeIfAbsentObserver.begin();
            Backend<K, V> backEnd = this.map;
            OnHeapValueHolder<V> cachedValue = backEnd.get(key);
            long now = this.timeSource.getTimeMillis();
            if (cachedValue == null) {
                Fault<V> fault = new Fault<>(new NullaryFunction<Store.ValueHolder<V>>() { // from class: org.ehcache.impl.internal.store.heap.OnHeapStore.15
                    @Override // org.ehcache.core.spi.function.NullaryFunction
                    public Store.ValueHolder<V> apply() {
                        return (Store.ValueHolder) source.apply(key);
                    }
                });
                cachedValue = backEnd.putIfAbsent(key, fault);
                if (cachedValue == null) {
                    return resolveFault(key, backEnd, now, fault);
                }
            }
            if (!(cachedValue instanceof Fault)) {
                if (cachedValue.isExpired(now, TimeUnit.MILLISECONDS)) {
                    expireMappingUnderLock(key, cachedValue);
                    Fault<V> fault2 = new Fault<>(new NullaryFunction<Store.ValueHolder<V>>() { // from class: org.ehcache.impl.internal.store.heap.OnHeapStore.16
                        @Override // org.ehcache.core.spi.function.NullaryFunction
                        public Store.ValueHolder<V> apply() {
                            return (Store.ValueHolder) source.apply(key);
                        }
                    });
                    cachedValue = backEnd.putIfAbsent(key, fault2);
                    if (cachedValue == null) {
                        return resolveFault(key, backEnd, now, fault2);
                    }
                } else {
                    setAccessTimeAndExpiryThenReturnMappingOutsideLock(key, cachedValue, now);
                }
            }
            this.getOrComputeIfAbsentObserver.end(CachingTierOperationOutcomes.GetOrComputeIfAbsentOutcome.HIT);
            return getValue(cachedValue);
        } catch (RuntimeException re) {
            StorePassThroughException.handleRuntimeException(re);
            return null;
        }
    }

    private Store.ValueHolder<V> resolveFault(final K key, Backend<K, V> backEnd, long now, Fault<V> fault) throws StoreAccessException {
        try {
            Store.ValueHolder<V> value = fault.get();
            if (value != null) {
                OnHeapValueHolder<V> newValue = importValueFromLowerTier(key, value, now, backEnd, fault);
                if (newValue == null) {
                    backEnd.remove(key, fault);
                    this.getOrComputeIfAbsentObserver.end(CachingTierOperationOutcomes.GetOrComputeIfAbsentOutcome.FAULT_FAILED);
                    return value;
                }
                if (backEnd.replace(key, fault, newValue)) {
                    this.getOrComputeIfAbsentObserver.end(CachingTierOperationOutcomes.GetOrComputeIfAbsentOutcome.FAULTED);
                    updateUsageInBytesIfRequired(newValue.size());
                    enforceCapacity();
                    return newValue;
                }
                final AtomicReference<Store.ValueHolder<V>> invalidatedValue = new AtomicReference<>();
                backEnd.computeIfPresent(key, new BiFunction<K, OnHeapValueHolder<V>, OnHeapValueHolder<V>>() { // from class: org.ehcache.impl.internal.store.heap.OnHeapStore.17
                    @Override // org.ehcache.core.spi.function.BiFunction
                    public /* bridge */ /* synthetic */ Object apply(Object x0, Object x1) {
                        return apply((AnonymousClass17) x0, (OnHeapValueHolder) x1);
                    }

                    public OnHeapValueHolder<V> apply(K mappedKey, OnHeapValueHolder<V> mappedValue) {
                        OnHeapStore.this.notifyInvalidation(key, mappedValue);
                        invalidatedValue.set(mappedValue);
                        OnHeapStore.this.updateUsageInBytesIfRequired(mappedValue.size());
                        return null;
                    }
                });
                Store.ValueHolder<V> p = getValue(invalidatedValue.get());
                if (p != null) {
                    if (p.isExpired(now, TimeUnit.MILLISECONDS)) {
                        this.getOrComputeIfAbsentObserver.end(CachingTierOperationOutcomes.GetOrComputeIfAbsentOutcome.FAULT_FAILED_MISS);
                        return null;
                    }
                    this.getOrComputeIfAbsentObserver.end(CachingTierOperationOutcomes.GetOrComputeIfAbsentOutcome.FAULT_FAILED);
                    return p;
                }
                this.getOrComputeIfAbsentObserver.end(CachingTierOperationOutcomes.GetOrComputeIfAbsentOutcome.FAULT_FAILED);
                return newValue;
            }
            backEnd.remove(key, fault);
            this.getOrComputeIfAbsentObserver.end(CachingTierOperationOutcomes.GetOrComputeIfAbsentOutcome.MISS);
            return null;
        } catch (Throwable e) {
            backEnd.remove(key, fault);
            throw new StoreAccessException(e);
        }
    }

    private void invalidateInGetOrComputeIfAbsent(Backend<K, V> map, final K key, final Store.ValueHolder<V> value, final Fault<V> fault, final long now, final Duration expiration) {
        map.computeIfPresent(key, new BiFunction<K, OnHeapValueHolder<V>, OnHeapValueHolder<V>>() { // from class: org.ehcache.impl.internal.store.heap.OnHeapStore.18
            @Override // org.ehcache.core.spi.function.BiFunction
            public /* bridge */ /* synthetic */ Object apply(Object x0, Object x1) {
                return apply((AnonymousClass18) x0, (OnHeapValueHolder) x1);
            }

            /* JADX WARN: Multi-variable type inference failed */
            public OnHeapValueHolder<V> apply(K mappedKey, OnHeapValueHolder<V> mappedValue) {
                if (mappedValue.equals(fault)) {
                    try {
                        OnHeapStore.this.invalidationListener.onInvalidation(key, OnHeapStore.this.cloneValueHolder(key, value, now, expiration, false));
                        return null;
                    } catch (LimitExceededException e) {
                        throw new AssertionError("Sizing is not expected to happen.");
                    }
                }
                return mappedValue;
            }
        });
    }

    @Override // org.ehcache.core.spi.store.tiering.CachingTier
    public void invalidate(final K key) throws StoreAccessException {
        this.invalidateObserver.begin();
        checkKey(key);
        try {
            final AtomicReference<CachingTierOperationOutcomes.InvalidateOutcome> outcome = new AtomicReference<>(CachingTierOperationOutcomes.InvalidateOutcome.MISS);
            this.map.computeIfPresent(key, new BiFunction<K, OnHeapValueHolder<V>, OnHeapValueHolder<V>>() { // from class: org.ehcache.impl.internal.store.heap.OnHeapStore.19
                @Override // org.ehcache.core.spi.function.BiFunction
                public /* bridge */ /* synthetic */ Object apply(Object x0, Object x1) {
                    return apply((AnonymousClass19) x0, (OnHeapValueHolder) x1);
                }

                public OnHeapValueHolder<V> apply(K k, OnHeapValueHolder<V> present) {
                    if (!(present instanceof Fault)) {
                        OnHeapStore.this.notifyInvalidation(key, present);
                        outcome.set(CachingTierOperationOutcomes.InvalidateOutcome.REMOVED);
                    }
                    OnHeapStore.this.updateUsageInBytesIfRequired(-present.size());
                    return null;
                }
            });
            this.invalidateObserver.end(outcome.get());
        } catch (RuntimeException re) {
            StorePassThroughException.handleRuntimeException(re);
        }
    }

    @Override // org.ehcache.core.spi.store.tiering.HigherCachingTier
    public void silentInvalidate(K key, final Function<Store.ValueHolder<V>, Void> function) throws StoreAccessException {
        this.silentInvalidateObserver.begin();
        checkKey(key);
        try {
            final AtomicReference<HigherCachingTierOperationOutcomes.SilentInvalidateOutcome> outcome = new AtomicReference<>(HigherCachingTierOperationOutcomes.SilentInvalidateOutcome.MISS);
            this.map.compute(key, new BiFunction<K, OnHeapValueHolder<V>, OnHeapValueHolder<V>>() { // from class: org.ehcache.impl.internal.store.heap.OnHeapStore.20
                @Override // org.ehcache.core.spi.function.BiFunction
                public /* bridge */ /* synthetic */ Object apply(Object x0, Object x1) {
                    return apply((AnonymousClass20) x0, (OnHeapValueHolder) x1);
                }

                public OnHeapValueHolder<V> apply(K mappedKey, OnHeapValueHolder<V> mappedValue) {
                    long size = 0;
                    OnHeapValueHolder<V> holderToPass = null;
                    if (mappedValue != null) {
                        size = mappedValue.size();
                        if (!(mappedValue instanceof Fault)) {
                            holderToPass = mappedValue;
                            outcome.set(HigherCachingTierOperationOutcomes.SilentInvalidateOutcome.REMOVED);
                        }
                    }
                    function.apply(holderToPass);
                    OnHeapStore.this.updateUsageInBytesIfRequired(-size);
                    return null;
                }
            });
            this.silentInvalidateObserver.end(outcome.get());
        } catch (RuntimeException re) {
            StorePassThroughException.handleRuntimeException(re);
        }
    }

    @Override // org.ehcache.core.spi.store.tiering.CachingTier
    public void invalidateAll() throws StoreAccessException {
        this.invalidateAllObserver.begin();
        long errorCount = 0;
        StoreAccessException firstException = null;
        for (K key : this.map.keySet()) {
            try {
                invalidate(key);
            } catch (StoreAccessException cae) {
                errorCount++;
                if (firstException == null) {
                    firstException = cae;
                }
            }
        }
        if (firstException != null) {
            this.invalidateAllObserver.end(CachingTierOperationOutcomes.InvalidateAllOutcome.FAILURE);
            throw new StoreAccessException("Error(s) during invalidation - count is " + errorCount, firstException);
        }
        clear();
        this.invalidateAllObserver.end(CachingTierOperationOutcomes.InvalidateAllOutcome.SUCCESS);
    }

    @Override // org.ehcache.core.spi.store.tiering.HigherCachingTier
    public void silentInvalidateAll(final BiFunction<K, Store.ValueHolder<V>, Void> biFunction) throws StoreAccessException {
        this.silentInvalidateAllObserver.begin();
        StoreAccessException exception = null;
        long errorCount = 0;
        for (final K k : this.map.keySet()) {
            try {
                silentInvalidate(k, new Function<Store.ValueHolder<V>, Void>() { // from class: org.ehcache.impl.internal.store.heap.OnHeapStore.21
                    @Override // org.ehcache.core.spi.function.Function
                    public Void apply(Store.ValueHolder<V> mappedValue) {
                        biFunction.apply(k, mappedValue);
                        return null;
                    }
                });
            } catch (StoreAccessException e) {
                errorCount++;
                if (exception == null) {
                    exception = e;
                }
            }
        }
        if (exception != null) {
            this.silentInvalidateAllObserver.end(HigherCachingTierOperationOutcomes.SilentInvalidateAllOutcome.FAILURE);
            throw new StoreAccessException("silentInvalidateAll failed - error count: " + errorCount, exception);
        }
        this.silentInvalidateAllObserver.end(HigherCachingTierOperationOutcomes.SilentInvalidateAllOutcome.SUCCESS);
    }

    @Override // org.ehcache.core.spi.store.tiering.HigherCachingTier
    public void silentInvalidateAllWithHash(long hash, BiFunction<K, Store.ValueHolder<V>, Void> biFunction) throws StoreAccessException {
        this.silentInvalidateAllWithHashObserver.begin();
        Map<K, OnHeapValueHolder<V>> removed = this.map.removeAllWithHash((int) hash);
        for (Map.Entry<K, OnHeapValueHolder<V>> entry : removed.entrySet()) {
            biFunction.apply(entry.getKey(), entry.getValue());
        }
        this.silentInvalidateAllWithHashObserver.end(HigherCachingTierOperationOutcomes.SilentInvalidateAllWithHashOutcome.SUCCESS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyInvalidation(K key, Store.ValueHolder<V> p) {
        CachingTier.InvalidationListener<K, V> invalidationListener = this.invalidationListener;
        if (invalidationListener != null) {
            invalidationListener.onInvalidation(key, p);
        }
    }

    @Override // org.ehcache.core.spi.store.tiering.CachingTier
    public void setInvalidationListener(final CachingTier.InvalidationListener<K, V> providedInvalidationListener) {
        this.invalidationListener = new CachingTier.InvalidationListener<K, V>() { // from class: org.ehcache.impl.internal.store.heap.OnHeapStore.22
            @Override // org.ehcache.core.spi.store.tiering.CachingTier.InvalidationListener
            public void onInvalidation(K key, Store.ValueHolder<V> valueHolder) {
                if (!(valueHolder instanceof Fault)) {
                    providedInvalidationListener.onInvalidation(key, valueHolder);
                }
            }
        };
    }

    @Override // org.ehcache.core.spi.store.tiering.CachingTier
    public void invalidateAllWithHash(long hash) throws StoreAccessException {
        this.invalidateAllWithHashObserver.begin();
        Map<K, OnHeapValueHolder<V>> removed = this.map.removeAllWithHash((int) hash);
        for (Map.Entry<K, OnHeapValueHolder<V>> entry : removed.entrySet()) {
            notifyInvalidation(entry.getKey(), entry.getValue());
        }
        LOG.debug("CLIENT: onheap store removed all with hash {}", Long.valueOf(hash));
        this.invalidateAllWithHashObserver.end(CachingTierOperationOutcomes.InvalidateAllWithHashOutcome.SUCCESS);
    }

    private Store.ValueHolder<V> getValue(Store.ValueHolder<V> cachedValue) {
        if (!(cachedValue instanceof Fault)) {
            return cachedValue;
        }
        return ((Fault) cachedValue).get();
    }

    private long getSizeOfKeyValuePairs(K key, OnHeapValueHolder<V> holder) throws LimitExceededException {
        return this.sizeOfEngine.sizeof(key, holder);
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/heap/OnHeapStore$Fault.class */
    private static class Fault<V> extends OnHeapValueHolder<V> {
        private static final int FAULT_ID = -1;

        @IgnoreSizeOf
        private final NullaryFunction<Store.ValueHolder<V>> source;
        private Store.ValueHolder<V> value;
        private Throwable throwable;
        private boolean complete;

        public Fault(NullaryFunction<Store.ValueHolder<V>> source) {
            super(-1L, 0L, true);
            this.source = source;
        }

        private void complete(Store.ValueHolder<V> value) {
            synchronized (this) {
                this.value = value;
                this.complete = true;
                notifyAll();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Store.ValueHolder<V> get() {
            synchronized (this) {
                if (!this.complete) {
                    try {
                        complete(this.source.apply());
                    } catch (Throwable e) {
                        fail(e);
                    }
                }
            }
            return throwOrReturn();
        }

        @Override // org.ehcache.core.spi.store.AbstractValueHolder, org.ehcache.core.spi.store.Store.ValueHolder
        public long getId() {
            throw new UnsupportedOperationException("You should NOT call that?!");
        }

        private Store.ValueHolder<V> throwOrReturn() {
            if (this.throwable != null) {
                if (this.throwable instanceof RuntimeException) {
                    throw ((RuntimeException) this.throwable);
                }
                throw new RuntimeException("Faulting from repository failed", this.throwable);
            }
            return this.value;
        }

        private void fail(Throwable t) {
            synchronized (this) {
                this.throwable = t;
                this.complete = true;
                notifyAll();
            }
            throwOrReturn();
        }

        @Override // org.ehcache.ValueSupplier
        public V value() {
            throw new UnsupportedOperationException();
        }

        @Override // org.ehcache.core.spi.store.AbstractValueHolder, org.ehcache.core.spi.store.Store.ValueHolder
        public long creationTime(TimeUnit unit) {
            throw new UnsupportedOperationException();
        }

        @Override // org.ehcache.core.spi.store.AbstractValueHolder
        public void setExpirationTime(long expirationTime, TimeUnit unit) {
            throw new UnsupportedOperationException();
        }

        @Override // org.ehcache.core.spi.store.AbstractValueHolder, org.ehcache.core.spi.store.Store.ValueHolder
        public long expirationTime(TimeUnit unit) {
            throw new UnsupportedOperationException();
        }

        @Override // org.ehcache.core.spi.store.AbstractValueHolder, org.ehcache.core.spi.store.Store.ValueHolder
        public boolean isExpired(long expirationTime, TimeUnit unit) {
            throw new UnsupportedOperationException();
        }

        @Override // org.ehcache.core.spi.store.AbstractValueHolder, org.ehcache.core.spi.store.Store.ValueHolder
        public long lastAccessTime(TimeUnit unit) {
            return Long.MAX_VALUE;
        }

        @Override // org.ehcache.core.spi.store.AbstractValueHolder
        public void setLastAccessTime(long lastAccessTime, TimeUnit unit) {
            throw new UnsupportedOperationException();
        }

        @Override // org.ehcache.impl.internal.store.heap.holders.OnHeapValueHolder
        public void setSize(long size) {
            throw new UnsupportedOperationException("Faults should not be sized");
        }

        @Override // org.ehcache.impl.internal.store.heap.holders.OnHeapValueHolder
        public long size() {
            return 0L;
        }

        @Override // org.ehcache.core.spi.store.AbstractValueHolder
        public String toString() {
            return "[Fault : " + (this.complete ? this.throwable == null ? String.valueOf(this.value) : this.throwable.getMessage() : "???") + "]";
        }

        @Override // org.ehcache.impl.internal.store.heap.holders.OnHeapValueHolder, org.ehcache.core.spi.store.AbstractValueHolder
        public boolean equals(Object obj) {
            return obj == this;
        }
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.ValueHolder<V> compute(K key, BiFunction<? super K, ? super V, ? extends V> mappingFunction) throws StoreAccessException {
        return compute(key, mappingFunction, REPLACE_EQUALS_TRUE);
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.ValueHolder<V> compute(final K key, final BiFunction<? super K, ? super V, ? extends V> mappingFunction, final NullaryFunction<Boolean> replaceEqual) throws StoreAccessException {
        this.computeObserver.begin();
        checkKey(key);
        final long now = this.timeSource.getTimeMillis();
        final StoreEventSink<K, V> eventSink = this.storeEventDispatcher.eventSink();
        try {
            final AtomicReference<OnHeapValueHolder<V>> valueHeld = new AtomicReference<>();
            final AtomicReference<StoreOperationOutcomes.ComputeOutcome> outcome = new AtomicReference<>(StoreOperationOutcomes.ComputeOutcome.MISS);
            OnHeapValueHolder<V> computeResult = this.map.compute(key, new BiFunction<K, OnHeapValueHolder<V>, OnHeapValueHolder<V>>() { // from class: org.ehcache.impl.internal.store.heap.OnHeapStore.23
                @Override // org.ehcache.core.spi.function.BiFunction
                public /* bridge */ /* synthetic */ Object apply(Object x0, Object x1) {
                    return apply((AnonymousClass23) x0, (OnHeapValueHolder) x1);
                }

                public OnHeapValueHolder<V> apply(K mappedKey, OnHeapValueHolder<V> mappedValue) {
                    long sizeDelta = 0;
                    if (mappedValue != null && mappedValue.isExpired(now, TimeUnit.MILLISECONDS)) {
                        OnHeapStore.this.fireOnExpirationEvent(mappedKey, mappedValue, eventSink);
                        sizeDelta = 0 - mappedValue.size();
                        mappedValue = null;
                    }
                    V existingValue = mappedValue == null ? null : mappedValue.value();
                    Object objApply = mappingFunction.apply(mappedKey, existingValue);
                    if (objApply != null) {
                        if (!OnHeapStore.eq(existingValue, objApply) || ((Boolean) replaceEqual.apply()).booleanValue() || mappedValue == null) {
                            OnHeapStore.this.checkValue(objApply);
                            if (mappedValue == null) {
                                OnHeapValueHolder<V> holder = OnHeapStore.this.newCreateValueHolder(key, objApply, now, eventSink);
                                if (holder != null) {
                                    outcome.set(StoreOperationOutcomes.ComputeOutcome.PUT);
                                    sizeDelta += holder.size();
                                }
                                OnHeapStore.this.updateUsageInBytesIfRequired(sizeDelta);
                                return holder;
                            }
                            outcome.set(StoreOperationOutcomes.ComputeOutcome.PUT);
                            long expirationTime = mappedValue.expirationTime(OnHeapValueHolder.TIME_UNIT);
                            OnHeapValueHolder<V> valueHolder = OnHeapStore.this.newUpdateValueHolder(key, mappedValue, objApply, now, eventSink);
                            long sizeDelta2 = sizeDelta - mappedValue.size();
                            if (valueHolder == null) {
                                try {
                                    valueHeld.set(OnHeapStore.this.makeValue(key, objApply, now, expirationTime, OnHeapStore.this.valueCopier, false));
                                } catch (LimitExceededException e) {
                                }
                            } else {
                                sizeDelta2 += valueHolder.size();
                            }
                            OnHeapStore.this.updateUsageInBytesIfRequired(sizeDelta2);
                            return valueHolder;
                        }
                        OnHeapValueHolder<V> holder2 = OnHeapStore.this.setAccessTimeAndExpiryThenReturnMappingUnderLock(key, mappedValue, now, eventSink);
                        outcome.set(StoreOperationOutcomes.ComputeOutcome.HIT);
                        if (holder2 == null) {
                            valueHeld.set(mappedValue);
                            OnHeapStore.this.updateUsageInBytesIfRequired(-mappedValue.size());
                        }
                        return holder2;
                    }
                    if (existingValue != null) {
                        eventSink.removed(mappedKey, mappedValue);
                        outcome.set(StoreOperationOutcomes.ComputeOutcome.REMOVED);
                        OnHeapStore.this.updateUsageInBytesIfRequired(-mappedValue.size());
                        return null;
                    }
                    return null;
                }
            });
            if (computeResult == null && valueHeld.get() != null) {
                computeResult = valueHeld.get();
            }
            this.storeEventDispatcher.releaseEventSink(eventSink);
            enforceCapacity();
            this.computeObserver.end(outcome.get());
            return computeResult;
        } catch (RuntimeException re) {
            this.storeEventDispatcher.releaseEventSinkAfterFailure(eventSink, re);
            StorePassThroughException.handleRuntimeException(re);
            return null;
        }
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.ValueHolder<V> computeIfAbsent(final K key, final Function<? super K, ? extends V> mappingFunction) throws StoreAccessException {
        this.computeIfAbsentObserver.begin();
        checkKey(key);
        final StoreEventSink<K, V> eventSink = this.storeEventDispatcher.eventSink();
        try {
            final long now = this.timeSource.getTimeMillis();
            final AtomicReference<OnHeapValueHolder<V>> previousValue = new AtomicReference<>();
            final AtomicReference<StoreOperationOutcomes.ComputeIfAbsentOutcome> outcome = new AtomicReference<>(StoreOperationOutcomes.ComputeIfAbsentOutcome.NOOP);
            OnHeapValueHolder<V> computeResult = this.map.compute(key, new BiFunction<K, OnHeapValueHolder<V>, OnHeapValueHolder<V>>() { // from class: org.ehcache.impl.internal.store.heap.OnHeapStore.24
                @Override // org.ehcache.core.spi.function.BiFunction
                public /* bridge */ /* synthetic */ Object apply(Object x0, Object x1) {
                    return apply((AnonymousClass24) x0, (OnHeapValueHolder) x1);
                }

                public OnHeapValueHolder<V> apply(K mappedKey, OnHeapValueHolder<V> mappedValue) {
                    if (mappedValue == null || mappedValue.isExpired(now, TimeUnit.MILLISECONDS)) {
                        if (mappedValue != null) {
                            OnHeapStore.this.updateUsageInBytesIfRequired(-mappedValue.size());
                            OnHeapStore.this.fireOnExpirationEvent(mappedKey, mappedValue, eventSink);
                        }
                        Object objApply = mappingFunction.apply(mappedKey);
                        if (objApply != null) {
                            OnHeapStore.this.checkValue(objApply);
                            OnHeapValueHolder<V> holder = OnHeapStore.this.newCreateValueHolder(key, objApply, now, eventSink);
                            if (holder != null) {
                                outcome.set(StoreOperationOutcomes.ComputeIfAbsentOutcome.PUT);
                                OnHeapStore.this.updateUsageInBytesIfRequired(holder.size());
                            }
                            return holder;
                        }
                        return null;
                    }
                    previousValue.set(mappedValue);
                    outcome.set(StoreOperationOutcomes.ComputeIfAbsentOutcome.HIT);
                    OnHeapValueHolder<V> holder2 = OnHeapStore.this.setAccessTimeAndExpiryThenReturnMappingUnderLock(key, mappedValue, now, eventSink);
                    if (holder2 == null) {
                        OnHeapStore.this.updateUsageInBytesIfRequired(-mappedValue.size());
                    }
                    return holder2;
                }
            });
            OnHeapValueHolder<V> previousValueHolder = previousValue.get();
            this.storeEventDispatcher.releaseEventSink(eventSink);
            if (computeResult != null) {
                enforceCapacity();
            }
            this.computeIfAbsentObserver.end(outcome.get());
            if (computeResult == null && previousValueHolder != null) {
                return previousValueHolder;
            }
            return computeResult;
        } catch (RuntimeException re) {
            this.storeEventDispatcher.releaseEventSinkAfterFailure(eventSink, re);
            StorePassThroughException.handleRuntimeException(re);
            return null;
        }
    }

    @Override // org.ehcache.core.spi.store.Store
    public Map<K, Store.ValueHolder<V>> bulkComputeIfAbsent(Set<? extends K> keys, final Function<Iterable<? extends K>, Iterable<? extends Map.Entry<? extends K, ? extends V>>> mappingFunction) throws StoreAccessException {
        Map<K, Store.ValueHolder<V>> result = new HashMap<>();
        for (K key : keys) {
            Store.ValueHolder<V> newValue = computeIfAbsent(key, new Function<K, V>() { // from class: org.ehcache.impl.internal.store.heap.OnHeapStore.25
                @Override // org.ehcache.core.spi.function.Function
                public V apply(K k) {
                    Iterable<? extends Map.Entry<? extends K, ? extends V>> entries = (Iterable) mappingFunction.apply(Collections.singleton(k));
                    Iterator<? extends Map.Entry<? extends K, ? extends V>> iterator = entries.iterator();
                    Map.Entry<? extends K, ? extends V> next = iterator.next();
                    K computedKey = next.getKey();
                    V computedValue = next.getValue();
                    OnHeapStore.this.checkKey(computedKey);
                    if (computedValue != null) {
                        OnHeapStore.this.checkValue(computedValue);
                        return computedValue;
                    }
                    return null;
                }
            });
            result.put(key, newValue);
        }
        return result;
    }

    @Override // org.ehcache.core.spi.store.ConfigurationChangeSupport
    public List<CacheConfigurationChangeListener> getConfigurationChangeListeners() {
        List<CacheConfigurationChangeListener> configurationChangeListenerList = new ArrayList<>();
        configurationChangeListenerList.add(this.cacheConfigurationChangeListener);
        return configurationChangeListenerList;
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
            Store.ValueHolder<V> newValue = compute(key, new BiFunction<K, V, V>() { // from class: org.ehcache.impl.internal.store.heap.OnHeapStore.26
                @Override // org.ehcache.core.spi.function.BiFunction
                public V apply(K k, V oldValue) {
                    Set<Map.Entry<K, V>> entrySet = Collections.singletonMap(k, oldValue).entrySet();
                    Iterable<? extends Map.Entry<? extends K, ? extends V>> entries = (Iterable) remappingFunction.apply(entrySet);
                    Iterator<? extends Map.Entry<? extends K, ? extends V>> iterator = entries.iterator();
                    Map.Entry<? extends K, ? extends V> next = iterator.next();
                    K key2 = next.getKey();
                    V value = next.getValue();
                    OnHeapStore.this.checkKey(key2);
                    if (value != null) {
                        OnHeapStore.this.checkValue(value);
                    }
                    return value;
                }
            }, replaceEqual);
            result.put(key, newValue);
        }
        return result;
    }

    @Override // org.ehcache.core.spi.store.Store
    public StoreEventSource<K, V> getStoreEventSource() {
        return this.storeEventDispatcher;
    }

    private OnHeapValueHolder<V> setAccessTimeAndExpiryThenReturnMappingOutsideLock(K key, OnHeapValueHolder<V> valueHolder, long now) {
        Duration duration;
        try {
            duration = this.expiry.getExpiryForAccess(key, valueHolder);
        } catch (RuntimeException re) {
            LOG.error("Expiry computation caused an exception - Expiry duration will be 0 ", (Throwable) re);
            duration = Duration.ZERO;
        }
        valueHolder.accessed(now, duration);
        if (Duration.ZERO.equals(duration)) {
            expireMappingUnderLock(key, valueHolder);
            return null;
        }
        return valueHolder;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public OnHeapValueHolder<V> setAccessTimeAndExpiryThenReturnMappingUnderLock(K key, OnHeapValueHolder<V> valueHolder, long now, StoreEventSink<K, V> eventSink) {
        Duration duration = Duration.ZERO;
        try {
            duration = this.expiry.getExpiryForAccess(key, valueHolder);
        } catch (RuntimeException re) {
            LOG.error("Expiry computation caused an exception - Expiry duration will be 0 ", (Throwable) re);
        }
        valueHolder.accessed(now, duration);
        if (Duration.ZERO.equals(duration)) {
            fireOnExpirationEvent(key, valueHolder, eventSink);
            return null;
        }
        return valueHolder;
    }

    private void expireMappingUnderLock(final K key, final Store.ValueHolder<V> value) {
        final StoreEventSink<K, V> eventSink = this.storeEventDispatcher.eventSink();
        try {
            this.map.computeIfPresent(key, new BiFunction<K, OnHeapValueHolder<V>, OnHeapValueHolder<V>>() { // from class: org.ehcache.impl.internal.store.heap.OnHeapStore.27
                @Override // org.ehcache.core.spi.function.BiFunction
                public /* bridge */ /* synthetic */ Object apply(Object x0, Object x1) {
                    return apply((AnonymousClass27) x0, (OnHeapValueHolder) x1);
                }

                public OnHeapValueHolder<V> apply(K mappedKey, OnHeapValueHolder<V> mappedValue) {
                    if (mappedValue.equals(value)) {
                        OnHeapStore.this.fireOnExpirationEvent(key, value, eventSink);
                        OnHeapStore.this.updateUsageInBytesIfRequired(-mappedValue.size());
                        return null;
                    }
                    return mappedValue;
                }
            });
            this.storeEventDispatcher.releaseEventSink(eventSink);
        } catch (RuntimeException re) {
            this.storeEventDispatcher.releaseEventSinkAfterFailure(eventSink, re);
            throw re;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public OnHeapValueHolder<V> newUpdateValueHolder(K key, OnHeapValueHolder<V> oldValue, V newValue, long now, StoreEventSink<K, V> eventSink) {
        long expirationTime;
        if (oldValue == null) {
            throw new NullPointerException();
        }
        if (newValue == null) {
            throw new NullPointerException();
        }
        Duration duration = Duration.ZERO;
        try {
            duration = this.expiry.getExpiryForUpdate(key, oldValue, newValue);
        } catch (RuntimeException re) {
            LOG.error("Expiry computation caused an exception - Expiry duration will be 0 ", (Throwable) re);
        }
        if (Duration.ZERO.equals(duration)) {
            eventSink.updated(key, oldValue, newValue);
            eventSink.expired(key, ValueSuppliers.supplierOf(newValue));
            return null;
        }
        if (duration == null) {
            expirationTime = oldValue.expirationTime(OnHeapValueHolder.TIME_UNIT);
        } else if (duration.isInfinite()) {
            expirationTime = -1;
        } else {
            expirationTime = safeExpireTime(now, duration);
        }
        OnHeapValueHolder<V> holder = null;
        try {
            holder = makeValue(key, newValue, now, expirationTime, this.valueCopier);
            eventSink.updated(key, oldValue, newValue);
        } catch (LimitExceededException e) {
            LOG.warn(e.getMessage());
            eventSink.removed(key, oldValue);
        }
        return holder;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public OnHeapValueHolder<V> newCreateValueHolder(K key, V value, long now, StoreEventSink<K, V> eventSink) {
        if (value == null) {
            throw new NullPointerException();
        }
        try {
            Duration duration = this.expiry.getExpiryForCreation(key, value);
            if (Duration.ZERO.equals(duration)) {
                return null;
            }
            long expirationTime = duration.isInfinite() ? -1L : safeExpireTime(now, duration);
            OnHeapValueHolder<V> holder = null;
            try {
                holder = makeValue(key, value, now, expirationTime, this.valueCopier);
                eventSink.created(key, value);
            } catch (LimitExceededException e) {
                LOG.warn(e.getMessage());
            }
            return holder;
        } catch (RuntimeException re) {
            LOG.error("Expiry computation caused an exception - Expiry duration will be 0 ", (Throwable) re);
            return null;
        }
    }

    private OnHeapValueHolder<V> importValueFromLowerTier(K key, Store.ValueHolder<V> valueHolder, long now, Backend<K, V> backEnd, Fault<V> fault) {
        Duration expiration = Duration.ZERO;
        try {
            expiration = this.expiry.getExpiryForAccess(key, valueHolder);
        } catch (RuntimeException re) {
            LOG.error("Expiry computation caused an exception - Expiry duration will be 0 ", (Throwable) re);
        }
        if (Duration.ZERO.equals(expiration)) {
            invalidateInGetOrComputeIfAbsent(backEnd, key, valueHolder, fault, now, Duration.ZERO);
            this.getOrComputeIfAbsentObserver.end(CachingTierOperationOutcomes.GetOrComputeIfAbsentOutcome.FAULT_FAILED);
            return null;
        }
        try {
            return cloneValueHolder(key, valueHolder, now, expiration, true);
        } catch (LimitExceededException e) {
            LOG.warn(e.getMessage());
            invalidateInGetOrComputeIfAbsent(backEnd, key, valueHolder, fault, now, expiration);
            this.getOrComputeIfAbsentObserver.end(CachingTierOperationOutcomes.GetOrComputeIfAbsentOutcome.FAULT_FAILED);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public OnHeapValueHolder<V> cloneValueHolder(K key, Store.ValueHolder<V> valueHolder, long now, Duration expiration, boolean sizingEnabled) throws LimitExceededException {
        OnHeapValueHolder<V> clonedValueHolder;
        V realValue = valueHolder.value();
        boolean evictionAdvice = checkEvictionAdvice(key, realValue);
        if (this.valueCopier instanceof SerializingCopier) {
            if ((valueHolder instanceof BinaryValueHolder) && ((BinaryValueHolder) valueHolder).isBinaryValueAvailable()) {
                clonedValueHolder = new SerializedOnHeapValueHolder((Store.ValueHolder) valueHolder, ((BinaryValueHolder) valueHolder).getBinaryValue(), evictionAdvice, ((SerializingCopier) this.valueCopier).getSerializer(), now, expiration);
            } else {
                clonedValueHolder = new SerializedOnHeapValueHolder(valueHolder, realValue, evictionAdvice, ((SerializingCopier) this.valueCopier).getSerializer(), now, expiration);
            }
        } else {
            clonedValueHolder = new CopiedOnHeapValueHolder(valueHolder, realValue, evictionAdvice, this.valueCopier, now, expiration);
        }
        if (sizingEnabled) {
            clonedValueHolder.setSize(getSizeOfKeyValuePairs(key, clonedValueHolder));
        }
        return clonedValueHolder;
    }

    private OnHeapValueHolder<V> makeValue(K key, V value, long creationTime, long expirationTime, Copier<V> valueCopier) throws LimitExceededException {
        return makeValue(key, value, creationTime, expirationTime, valueCopier, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public OnHeapValueHolder<V> makeValue(K key, V value, long creationTime, long expirationTime, Copier<V> valueCopier, boolean size) throws LimitExceededException {
        OnHeapValueHolder<V> valueHolder;
        boolean evictionAdvice = checkEvictionAdvice(key, value);
        if (valueCopier instanceof SerializingCopier) {
            valueHolder = new SerializedOnHeapValueHolder(value, creationTime, expirationTime, evictionAdvice, ((SerializingCopier) valueCopier).getSerializer());
        } else {
            valueHolder = new CopiedOnHeapValueHolder(value, creationTime, expirationTime, evictionAdvice, valueCopier);
        }
        if (size) {
            valueHolder.setSize(getSizeOfKeyValuePairs(key, valueHolder));
        }
        return valueHolder;
    }

    private boolean checkEvictionAdvice(K key, V value) {
        try {
            return this.evictionAdvisor.adviseAgainstEviction(key, value);
        } catch (Exception e) {
            LOG.error("Exception raised while running eviction advisor - Eviction will assume entry is NOT advised against eviction", (Throwable) e);
            return false;
        }
    }

    private static long safeExpireTime(long now, Duration duration) {
        long millis = OnHeapValueHolder.TIME_UNIT.convert(duration.getLength(), duration.getTimeUnit());
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
    public void updateUsageInBytesIfRequired(long delta) {
        this.map.updateUsageInBytesIfRequired(delta);
    }

    protected long byteSized() {
        return this.map.byteSize();
    }

    @FindbugsSuppressWarnings({"QF_QUESTIONABLE_FOR_LOOP"})
    protected void enforceCapacity() {
        StoreEventSink<K, V> eventSink = this.storeEventDispatcher.eventSink();
        int evicted = 0;
        for (int attempts = 0; attempts < 4 && evicted < 2; attempts++) {
            try {
                if (this.capacity >= this.map.naturalSize()) {
                    break;
                }
                if (evict(eventSink)) {
                    evicted++;
                }
            } catch (RuntimeException re) {
                this.storeEventDispatcher.releaseEventSinkAfterFailure(eventSink, re);
                throw re;
            }
        }
        this.storeEventDispatcher.releaseEventSink(eventSink);
    }

    boolean evict(final StoreEventSink<K, V> eventSink) {
        this.evictionObserver.begin();
        Random random = new Random();
        Map.Entry<K, OnHeapValueHolder<V>> candidate = this.map.getEvictionCandidate(random, 8, EVICTION_PRIORITIZER, EVICTION_ADVISOR);
        if (candidate == null) {
            candidate = this.map.getEvictionCandidate(random, 8, EVICTION_PRIORITIZER, Eviction.noAdvice());
        }
        if (candidate == null) {
            return false;
        }
        final Map.Entry<K, OnHeapValueHolder<V>> evictionCandidate = candidate;
        final AtomicBoolean removed = new AtomicBoolean(false);
        this.map.computeIfPresent(evictionCandidate.getKey(), new BiFunction<K, OnHeapValueHolder<V>, OnHeapValueHolder<V>>() { // from class: org.ehcache.impl.internal.store.heap.OnHeapStore.28
            @Override // org.ehcache.core.spi.function.BiFunction
            public /* bridge */ /* synthetic */ Object apply(Object x0, Object x1) {
                return apply((AnonymousClass28) x0, (OnHeapValueHolder) x1);
            }

            /* JADX WARN: Multi-variable type inference failed */
            public OnHeapValueHolder<V> apply(K mappedKey, OnHeapValueHolder<V> mappedValue) {
                if (mappedValue.equals(evictionCandidate.getValue())) {
                    removed.set(true);
                    if (!(evictionCandidate.getValue() instanceof Fault)) {
                        eventSink.evicted(evictionCandidate.getKey(), (ValueSupplier) evictionCandidate.getValue());
                        OnHeapStore.this.invalidationListener.onInvalidation(mappedKey, (Store.ValueHolder) evictionCandidate.getValue());
                    }
                    OnHeapStore.this.updateUsageInBytesIfRequired(-mappedValue.size());
                    return null;
                }
                return mappedValue;
            }
        });
        if (removed.get()) {
            this.evictionObserver.end(StoreOperationOutcomes.EvictionOutcome.SUCCESS);
            return true;
        }
        this.evictionObserver.end(StoreOperationOutcomes.EvictionOutcome.FAILURE);
        return false;
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
    public void fireOnExpirationEvent(K mappedKey, Store.ValueHolder<V> mappedValue, StoreEventSink<K, V> eventSink) {
        this.expirationObserver.begin();
        this.expirationObserver.end(StoreOperationOutcomes.ExpirationOutcome.SUCCESS);
        eventSink.expired(mappedKey, mappedValue);
        this.invalidationListener.onInvalidation(mappedKey, mappedValue);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean eq(Object o1, Object o2) {
        return o1 == o2 || (o1 != null && o1.equals(o2));
    }

    @ServiceDependencies({TimeSourceService.class, CopyProvider.class, SizeOfEngineProvider.class})
    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/heap/OnHeapStore$Provider.class */
    public static class Provider implements Store.Provider, CachingTier.Provider, HigherCachingTier.Provider {
        private volatile ServiceProvider<Service> serviceProvider;
        private final Map<Store<?, ?>, List<Copier>> createdStores = new ConcurrentWeakIdentityHashMap();
        private final Map<OnHeapStore<?, ?>, Collection<MappedOperationStatistic<?, ?>>> tierOperationStatistics = new ConcurrentWeakIdentityHashMap();

        @Override // org.ehcache.core.spi.store.Store.Provider
        public /* bridge */ /* synthetic */ Store createStore(Store.Configuration x0, ServiceConfiguration[] x1) {
            return createStore(x0, (ServiceConfiguration<?>[]) x1);
        }

        @Override // org.ehcache.core.spi.store.Store.Provider
        public int rank(Set<ResourceType<?>> resourceTypes, Collection<ServiceConfiguration<?>> serviceConfigs) {
            return resourceTypes.equals(Collections.singleton(ResourceType.Core.HEAP)) ? 1 : 0;
        }

        @Override // org.ehcache.core.spi.store.tiering.CachingTier.Provider
        public int rankCachingTier(Set<ResourceType<?>> resourceTypes, Collection<ServiceConfiguration<?>> serviceConfigs) {
            return rank(resourceTypes, serviceConfigs);
        }

        @Override // org.ehcache.core.spi.store.Store.Provider
        public <K, V> OnHeapStore<K, V> createStore(Store.Configuration<K, V> storeConfig, ServiceConfiguration<?>... serviceConfigs) {
            OnHeapStore<K, V> store = createStoreInternal(storeConfig, new ScopedStoreEventDispatcher(storeConfig.getDispatcherConcurrency()), serviceConfigs);
            Collection<MappedOperationStatistic<?, ?>> tieredOps = new ArrayList<>();
            MappedOperationStatistic<StoreOperationOutcomes.GetOutcome, TierOperationOutcomes.GetOutcome> get = new MappedOperationStatistic<>(store, TierOperationOutcomes.GET_TRANSLATION, BeanUtil.PREFIX_GETTER_GET, ResourceType.Core.HEAP.getTierHeight(), BeanUtil.PREFIX_GETTER_GET, OnHeapStore.STATISTICS_TAG);
            StatisticsManager.associate(get).withParent(store);
            tieredOps.add(get);
            MappedOperationStatistic<StoreOperationOutcomes.EvictionOutcome, TierOperationOutcomes.EvictionOutcome> evict = new MappedOperationStatistic<>(store, TierOperationOutcomes.EVICTION_TRANSLATION, "eviction", ResourceType.Core.HEAP.getTierHeight(), "eviction", OnHeapStore.STATISTICS_TAG);
            StatisticsManager.associate(evict).withParent(store);
            tieredOps.add(evict);
            this.tierOperationStatistics.put(store, tieredOps);
            return store;
        }

        public <K, V> OnHeapStore<K, V> createStoreInternal(Store.Configuration<K, V> storeConfig, StoreEventDispatcher<K, V> eventDispatcher, ServiceConfiguration<?>... serviceConfigs) {
            TimeSource timeSource = ((TimeSourceService) this.serviceProvider.getService(TimeSourceService.class)).getTimeSource();
            CopyProvider copyProvider = (CopyProvider) this.serviceProvider.getService(CopyProvider.class);
            Copier keyCopier = copyProvider.createKeyCopier(storeConfig.getKeyType(), storeConfig.getKeySerializer(), serviceConfigs);
            Copier valueCopier = copyProvider.createValueCopier(storeConfig.getValueType(), storeConfig.getValueSerializer(), serviceConfigs);
            List<Copier> copiers = new ArrayList<>();
            copiers.add(keyCopier);
            copiers.add(valueCopier);
            SizeOfEngineProvider sizeOfEngineProvider = (SizeOfEngineProvider) this.serviceProvider.getService(SizeOfEngineProvider.class);
            SizeOfEngine sizeOfEngine = sizeOfEngineProvider.createSizeOfEngine(((SizedResourcePool) storeConfig.getResourcePools().getPoolForResource(ResourceType.Core.HEAP)).getUnit(), serviceConfigs);
            OnHeapStore<K, V> onHeapStore = new OnHeapStore<>(storeConfig, timeSource, keyCopier, valueCopier, sizeOfEngine, eventDispatcher);
            this.createdStores.put(onHeapStore, copiers);
            return onHeapStore;
        }

        @Override // org.ehcache.core.spi.store.Store.Provider
        public void releaseStore(Store<?, ?> resource) {
            List<Copier> copiers = this.createdStores.remove(resource);
            if (copiers == null) {
                throw new IllegalArgumentException("Given store is not managed by this provider : " + resource);
            }
            OnHeapStore onHeapStore = (OnHeapStore) resource;
            close(onHeapStore);
            StatisticsManager.nodeFor(onHeapStore).clean();
            this.tierOperationStatistics.remove(onHeapStore);
            CopyProvider copyProvider = (CopyProvider) this.serviceProvider.getService(CopyProvider.class);
            for (Copier<?> copier : copiers) {
                try {
                    copyProvider.releaseCopier(copier);
                } catch (Exception e) {
                    throw new IllegalStateException("Exception while releasing Copier instance.", e);
                }
            }
        }

        static void close(OnHeapStore onHeapStore) {
            onHeapStore.clear();
        }

        @Override // org.ehcache.core.spi.store.Store.Provider
        public void initStore(Store<?, ?> resource) {
            checkResource(resource);
            List<Copier> copiers = this.createdStores.get(resource);
            for (Copier copier : copiers) {
                if (copier instanceof SerializingCopier) {
                    Serializer serializer = ((SerializingCopier) copier).getSerializer();
                    if (serializer instanceof StatefulSerializer) {
                        ((StatefulSerializer) serializer).init(new TransientStateRepository());
                    }
                }
            }
        }

        private void checkResource(Object resource) {
            if (!this.createdStores.containsKey(resource)) {
                throw new IllegalArgumentException("Given store is not managed by this provider : " + resource);
            }
        }

        @Override // org.ehcache.spi.service.Service
        public void start(ServiceProvider<Service> serviceProvider) {
            this.serviceProvider = serviceProvider;
        }

        @Override // org.ehcache.spi.service.Service
        public void stop() {
            this.serviceProvider = null;
            this.createdStores.clear();
        }

        @Override // org.ehcache.core.spi.store.tiering.CachingTier.Provider
        public <K, V> CachingTier<K, V> createCachingTier(Store.Configuration<K, V> storeConfig, ServiceConfiguration<?>... serviceConfigs) {
            OnHeapStore<K, V> cachingTier = createStoreInternal(storeConfig, NullStoreEventDispatcher.nullStoreEventDispatcher(), serviceConfigs);
            Collection<MappedOperationStatistic<?, ?>> tieredOps = new ArrayList<>();
            MappedOperationStatistic<CachingTierOperationOutcomes.GetOrComputeIfAbsentOutcome, TierOperationOutcomes.GetOutcome> get = new MappedOperationStatistic<>(cachingTier, TierOperationOutcomes.GET_OR_COMPUTEIFABSENT_TRANSLATION, BeanUtil.PREFIX_GETTER_GET, ResourceType.Core.HEAP.getTierHeight(), "getOrComputeIfAbsent", OnHeapStore.STATISTICS_TAG);
            StatisticsManager.associate(get).withParent(cachingTier);
            tieredOps.add(get);
            MappedOperationStatistic<StoreOperationOutcomes.EvictionOutcome, TierOperationOutcomes.EvictionOutcome> evict = new MappedOperationStatistic<>(cachingTier, TierOperationOutcomes.EVICTION_TRANSLATION, "eviction", ResourceType.Core.HEAP.getTierHeight(), "eviction", OnHeapStore.STATISTICS_TAG);
            StatisticsManager.associate(evict).withParent(cachingTier);
            tieredOps.add(evict);
            this.tierOperationStatistics.put(cachingTier, tieredOps);
            return cachingTier;
        }

        @Override // org.ehcache.core.spi.store.tiering.CachingTier.Provider
        public void releaseCachingTier(CachingTier<?, ?> resource) {
            checkResource(resource);
            try {
                resource.invalidateAll();
            } catch (StoreAccessException e) {
                OnHeapStore.LOG.warn("Invalidation failure while releasing caching tier", (Throwable) e);
            }
            releaseStore((Store) resource);
        }

        @Override // org.ehcache.core.spi.store.tiering.CachingTier.Provider
        public void initCachingTier(CachingTier<?, ?> resource) {
            checkResource(resource);
        }

        @Override // org.ehcache.core.spi.store.tiering.HigherCachingTier.Provider
        public <K, V> HigherCachingTier<K, V> createHigherCachingTier(Store.Configuration<K, V> storeConfig, ServiceConfiguration<?>... serviceConfigs) {
            OnHeapStore<K, V> higherCachingTier = createStoreInternal(storeConfig, new ScopedStoreEventDispatcher(storeConfig.getDispatcherConcurrency()), serviceConfigs);
            Collection<MappedOperationStatistic<?, ?>> tieredOps = new ArrayList<>();
            MappedOperationStatistic<CachingTierOperationOutcomes.GetOrComputeIfAbsentOutcome, TierOperationOutcomes.GetOutcome> get = new MappedOperationStatistic<>(higherCachingTier, TierOperationOutcomes.GET_OR_COMPUTEIFABSENT_TRANSLATION, BeanUtil.PREFIX_GETTER_GET, ResourceType.Core.HEAP.getTierHeight(), "getOrComputeIfAbsent", OnHeapStore.STATISTICS_TAG);
            StatisticsManager.associate(get).withParent(higherCachingTier);
            tieredOps.add(get);
            MappedOperationStatistic<StoreOperationOutcomes.EvictionOutcome, TierOperationOutcomes.EvictionOutcome> evict = new MappedOperationStatistic<>(higherCachingTier, TierOperationOutcomes.EVICTION_TRANSLATION, "eviction", ResourceType.Core.HEAP.getTierHeight(), "eviction", OnHeapStore.STATISTICS_TAG);
            StatisticsManager.associate(evict).withParent(higherCachingTier);
            tieredOps.add(evict);
            this.tierOperationStatistics.put(higherCachingTier, tieredOps);
            return higherCachingTier;
        }

        @Override // org.ehcache.core.spi.store.tiering.HigherCachingTier.Provider
        public void releaseHigherCachingTier(HigherCachingTier<?, ?> resource) {
            releaseCachingTier(resource);
        }

        @Override // org.ehcache.core.spi.store.tiering.HigherCachingTier.Provider
        public void initHigherCachingTier(HigherCachingTier<?, ?> resource) {
            checkResource(resource);
        }
    }
}
