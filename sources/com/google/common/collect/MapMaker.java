package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Ascii;
import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.base.Ticker;
import com.google.common.collect.MapMakerInternalMap;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
/* loaded from: guava-18.0.jar:com/google/common/collect/MapMaker.class */
public final class MapMaker extends GenericMapMaker<Object, Object> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int DEFAULT_CONCURRENCY_LEVEL = 4;
    private static final int DEFAULT_EXPIRATION_NANOS = 0;
    static final int UNSET_INT = -1;
    boolean useCustomMap;
    MapMakerInternalMap.Strength keyStrength;
    MapMakerInternalMap.Strength valueStrength;
    RemovalCause nullRemovalCause;
    Equivalence<Object> keyEquivalence;
    Ticker ticker;
    int initialCapacity = -1;
    int concurrencyLevel = -1;
    int maximumSize = -1;
    long expireAfterWriteNanos = -1;
    long expireAfterAccessNanos = -1;

    /* loaded from: guava-18.0.jar:com/google/common/collect/MapMaker$RemovalCause.class */
    enum RemovalCause {
        EXPLICIT { // from class: com.google.common.collect.MapMaker.RemovalCause.1
            @Override // com.google.common.collect.MapMaker.RemovalCause
            boolean wasEvicted() {
                return false;
            }
        },
        REPLACED { // from class: com.google.common.collect.MapMaker.RemovalCause.2
            @Override // com.google.common.collect.MapMaker.RemovalCause
            boolean wasEvicted() {
                return false;
            }
        },
        COLLECTED { // from class: com.google.common.collect.MapMaker.RemovalCause.3
            @Override // com.google.common.collect.MapMaker.RemovalCause
            boolean wasEvicted() {
                return true;
            }
        },
        EXPIRED { // from class: com.google.common.collect.MapMaker.RemovalCause.4
            @Override // com.google.common.collect.MapMaker.RemovalCause
            boolean wasEvicted() {
                return true;
            }
        },
        SIZE { // from class: com.google.common.collect.MapMaker.RemovalCause.5
            @Override // com.google.common.collect.MapMaker.RemovalCause
            boolean wasEvicted() {
                return true;
            }
        };

        abstract boolean wasEvicted();
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/MapMaker$RemovalListener.class */
    interface RemovalListener<K, V> {
        void onRemoval(RemovalNotification<K, V> removalNotification);
    }

    @Override // com.google.common.collect.GenericMapMaker
    /* renamed from: keyEquivalence, reason: avoid collision after fix types in other method */
    /* bridge */ /* synthetic */ GenericMapMaker<Object, Object> keyEquivalence2(Equivalence x0) {
        return keyEquivalence((Equivalence<Object>) x0);
    }

    @Override // com.google.common.collect.GenericMapMaker
    @GwtIncompatible("To be supported")
    GenericMapMaker<Object, Object> keyEquivalence(Equivalence<Object> equivalence) {
        Preconditions.checkState(this.keyEquivalence == null, "key equivalence was already set to %s", this.keyEquivalence);
        this.keyEquivalence = (Equivalence) Preconditions.checkNotNull(equivalence);
        this.useCustomMap = true;
        return this;
    }

    Equivalence<Object> getKeyEquivalence() {
        return (Equivalence) MoreObjects.firstNonNull(this.keyEquivalence, getKeyStrength().defaultEquivalence());
    }

    @Override // com.google.common.collect.GenericMapMaker
    /* renamed from: initialCapacity, reason: merged with bridge method [inline-methods] */
    public GenericMapMaker<Object, Object> initialCapacity2(int initialCapacity) {
        Preconditions.checkState(this.initialCapacity == -1, "initial capacity was already set to %s", Integer.valueOf(this.initialCapacity));
        Preconditions.checkArgument(initialCapacity >= 0);
        this.initialCapacity = initialCapacity;
        return this;
    }

    int getInitialCapacity() {
        if (this.initialCapacity == -1) {
            return 16;
        }
        return this.initialCapacity;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.GenericMapMaker
    @Deprecated
    /* renamed from: maximumSize, reason: merged with bridge method [inline-methods] */
    public GenericMapMaker<Object, Object> maximumSize2(int size) {
        Preconditions.checkState(this.maximumSize == -1, "maximum size was already set to %s", Integer.valueOf(this.maximumSize));
        Preconditions.checkArgument(size >= 0, "maximum size must not be negative");
        this.maximumSize = size;
        this.useCustomMap = true;
        if (this.maximumSize == 0) {
            this.nullRemovalCause = RemovalCause.SIZE;
        }
        return this;
    }

    @Override // com.google.common.collect.GenericMapMaker
    /* renamed from: concurrencyLevel, reason: merged with bridge method [inline-methods] */
    public GenericMapMaker<Object, Object> concurrencyLevel2(int concurrencyLevel) {
        Preconditions.checkState(this.concurrencyLevel == -1, "concurrency level was already set to %s", Integer.valueOf(this.concurrencyLevel));
        Preconditions.checkArgument(concurrencyLevel > 0);
        this.concurrencyLevel = concurrencyLevel;
        return this;
    }

    int getConcurrencyLevel() {
        if (this.concurrencyLevel == -1) {
            return 4;
        }
        return this.concurrencyLevel;
    }

    @Override // com.google.common.collect.GenericMapMaker
    @GwtIncompatible("java.lang.ref.WeakReference")
    /* renamed from: weakKeys, reason: merged with bridge method [inline-methods] */
    public GenericMapMaker<Object, Object> weakKeys2() {
        return setKeyStrength(MapMakerInternalMap.Strength.WEAK);
    }

    MapMaker setKeyStrength(MapMakerInternalMap.Strength strength) {
        Preconditions.checkState(this.keyStrength == null, "Key strength was already set to %s", this.keyStrength);
        this.keyStrength = (MapMakerInternalMap.Strength) Preconditions.checkNotNull(strength);
        Preconditions.checkArgument(this.keyStrength != MapMakerInternalMap.Strength.SOFT, "Soft keys are not supported");
        if (strength != MapMakerInternalMap.Strength.STRONG) {
            this.useCustomMap = true;
        }
        return this;
    }

    MapMakerInternalMap.Strength getKeyStrength() {
        return (MapMakerInternalMap.Strength) MoreObjects.firstNonNull(this.keyStrength, MapMakerInternalMap.Strength.STRONG);
    }

    @Override // com.google.common.collect.GenericMapMaker
    @GwtIncompatible("java.lang.ref.WeakReference")
    /* renamed from: weakValues, reason: merged with bridge method [inline-methods] */
    public GenericMapMaker<Object, Object> weakValues2() {
        return setValueStrength(MapMakerInternalMap.Strength.WEAK);
    }

    @Override // com.google.common.collect.GenericMapMaker
    @GwtIncompatible("java.lang.ref.SoftReference")
    @Deprecated
    /* renamed from: softValues, reason: merged with bridge method [inline-methods] */
    public GenericMapMaker<Object, Object> softValues2() {
        return setValueStrength(MapMakerInternalMap.Strength.SOFT);
    }

    MapMaker setValueStrength(MapMakerInternalMap.Strength strength) {
        Preconditions.checkState(this.valueStrength == null, "Value strength was already set to %s", this.valueStrength);
        this.valueStrength = (MapMakerInternalMap.Strength) Preconditions.checkNotNull(strength);
        if (strength != MapMakerInternalMap.Strength.STRONG) {
            this.useCustomMap = true;
        }
        return this;
    }

    MapMakerInternalMap.Strength getValueStrength() {
        return (MapMakerInternalMap.Strength) MoreObjects.firstNonNull(this.valueStrength, MapMakerInternalMap.Strength.STRONG);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.GenericMapMaker
    @Deprecated
    /* renamed from: expireAfterWrite, reason: merged with bridge method [inline-methods] */
    public GenericMapMaker<Object, Object> expireAfterWrite2(long duration, TimeUnit unit) {
        checkExpiration(duration, unit);
        this.expireAfterWriteNanos = unit.toNanos(duration);
        if (duration == 0 && this.nullRemovalCause == null) {
            this.nullRemovalCause = RemovalCause.EXPIRED;
        }
        this.useCustomMap = true;
        return this;
    }

    private void checkExpiration(long duration, TimeUnit unit) {
        Preconditions.checkState(this.expireAfterWriteNanos == -1, "expireAfterWrite was already set to %s ns", Long.valueOf(this.expireAfterWriteNanos));
        Preconditions.checkState(this.expireAfterAccessNanos == -1, "expireAfterAccess was already set to %s ns", Long.valueOf(this.expireAfterAccessNanos));
        Preconditions.checkArgument(duration >= 0, "duration cannot be negative: %s %s", Long.valueOf(duration), unit);
    }

    long getExpireAfterWriteNanos() {
        if (this.expireAfterWriteNanos == -1) {
            return 0L;
        }
        return this.expireAfterWriteNanos;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.GenericMapMaker
    @GwtIncompatible("To be supported")
    @Deprecated
    /* renamed from: expireAfterAccess, reason: merged with bridge method [inline-methods] */
    public GenericMapMaker<Object, Object> expireAfterAccess2(long duration, TimeUnit unit) {
        checkExpiration(duration, unit);
        this.expireAfterAccessNanos = unit.toNanos(duration);
        if (duration == 0 && this.nullRemovalCause == null) {
            this.nullRemovalCause = RemovalCause.EXPIRED;
        }
        this.useCustomMap = true;
        return this;
    }

    long getExpireAfterAccessNanos() {
        if (this.expireAfterAccessNanos == -1) {
            return 0L;
        }
        return this.expireAfterAccessNanos;
    }

    Ticker getTicker() {
        return (Ticker) MoreObjects.firstNonNull(this.ticker, Ticker.systemTicker());
    }

    @GwtIncompatible("To be supported")
    @Deprecated
    <K, V> GenericMapMaker<K, V> removalListener(RemovalListener<K, V> listener) {
        Preconditions.checkState(this.removalListener == null);
        this.removalListener = (RemovalListener) Preconditions.checkNotNull(listener);
        this.useCustomMap = true;
        return this;
    }

    @Override // com.google.common.collect.GenericMapMaker
    public <K, V> ConcurrentMap<K, V> makeMap() {
        if (!this.useCustomMap) {
            return new ConcurrentHashMap(getInitialCapacity(), 0.75f, getConcurrencyLevel());
        }
        return this.nullRemovalCause == null ? new MapMakerInternalMap<>(this) : new NullConcurrentMap<>(this);
    }

    @Override // com.google.common.collect.GenericMapMaker
    @GwtIncompatible("MapMakerInternalMap")
    <K, V> MapMakerInternalMap<K, V> makeCustomMap() {
        return new MapMakerInternalMap<>(this);
    }

    @Override // com.google.common.collect.GenericMapMaker
    @Deprecated
    <K, V> ConcurrentMap<K, V> makeComputingMap(Function<? super K, ? extends V> computingFunction) {
        return this.nullRemovalCause == null ? new ComputingMapAdapter<>(this, computingFunction) : new NullComputingConcurrentMap<>(this, computingFunction);
    }

    public String toString() {
        MoreObjects.ToStringHelper s = MoreObjects.toStringHelper(this);
        if (this.initialCapacity != -1) {
            s.add("initialCapacity", this.initialCapacity);
        }
        if (this.concurrencyLevel != -1) {
            s.add("concurrencyLevel", this.concurrencyLevel);
        }
        if (this.maximumSize != -1) {
            s.add("maximumSize", this.maximumSize);
        }
        if (this.expireAfterWriteNanos != -1) {
            s.add("expireAfterWrite", new StringBuilder(22).append(this.expireAfterWriteNanos).append("ns").toString());
        }
        if (this.expireAfterAccessNanos != -1) {
            s.add("expireAfterAccess", new StringBuilder(22).append(this.expireAfterAccessNanos).append("ns").toString());
        }
        if (this.keyStrength != null) {
            s.add("keyStrength", Ascii.toLowerCase(this.keyStrength.toString()));
        }
        if (this.valueStrength != null) {
            s.add("valueStrength", Ascii.toLowerCase(this.valueStrength.toString()));
        }
        if (this.keyEquivalence != null) {
            s.addValue("keyEquivalence");
        }
        if (this.removalListener != null) {
            s.addValue("removalListener");
        }
        return s.toString();
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/MapMaker$RemovalNotification.class */
    static final class RemovalNotification<K, V> extends ImmutableEntry<K, V> {
        private static final long serialVersionUID = 0;
        private final RemovalCause cause;

        RemovalNotification(@Nullable K key, @Nullable V value, RemovalCause cause) {
            super(key, value);
            this.cause = cause;
        }

        public RemovalCause getCause() {
            return this.cause;
        }

        public boolean wasEvicted() {
            return this.cause.wasEvicted();
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/MapMaker$NullConcurrentMap.class */
    static class NullConcurrentMap<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V>, Serializable {
        private static final long serialVersionUID = 0;
        private final RemovalListener<K, V> removalListener;
        private final RemovalCause removalCause;

        NullConcurrentMap(MapMaker mapMaker) {
            this.removalListener = mapMaker.getRemovalListener();
            this.removalCause = mapMaker.nullRemovalCause;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(@Nullable Object key) {
            return false;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsValue(@Nullable Object value) {
            return false;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public V get(@Nullable Object key) {
            return null;
        }

        void notifyRemoval(K key, V value) {
            RemovalNotification<K, V> notification = new RemovalNotification<>(key, value, this.removalCause);
            this.removalListener.onRemoval(notification);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public V put(K key, V value) {
            Preconditions.checkNotNull(key);
            Preconditions.checkNotNull(value);
            notifyRemoval(key, value);
            return null;
        }

        @Override // java.util.Map, java.util.concurrent.ConcurrentMap
        public V putIfAbsent(K key, V value) {
            return put(key, value);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public V remove(@Nullable Object key) {
            return null;
        }

        @Override // java.util.Map, java.util.concurrent.ConcurrentMap
        public boolean remove(@Nullable Object key, @Nullable Object value) {
            return false;
        }

        @Override // java.util.Map, java.util.concurrent.ConcurrentMap
        public V replace(K key, V value) {
            Preconditions.checkNotNull(key);
            Preconditions.checkNotNull(value);
            return null;
        }

        @Override // java.util.Map, java.util.concurrent.ConcurrentMap
        public boolean replace(K key, @Nullable V oldValue, V newValue) {
            Preconditions.checkNotNull(key);
            Preconditions.checkNotNull(newValue);
            return false;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<Map.Entry<K, V>> entrySet() {
            return Collections.emptySet();
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/MapMaker$NullComputingConcurrentMap.class */
    static final class NullComputingConcurrentMap<K, V> extends NullConcurrentMap<K, V> {
        private static final long serialVersionUID = 0;
        final Function<? super K, ? extends V> computingFunction;

        NullComputingConcurrentMap(MapMaker mapMaker, Function<? super K, ? extends V> computingFunction) {
            super(mapMaker);
            this.computingFunction = (Function) Preconditions.checkNotNull(computingFunction);
        }

        @Override // com.google.common.collect.MapMaker.NullConcurrentMap, java.util.AbstractMap, java.util.Map
        public V get(Object obj) {
            V value = compute(obj);
            Preconditions.checkNotNull(value, "%s returned null for key %s.", this.computingFunction, obj);
            notifyRemoval(obj, value);
            return value;
        }

        private V compute(K key) {
            Preconditions.checkNotNull(key);
            try {
                return this.computingFunction.apply(key);
            } catch (ComputationException e) {
                throw e;
            } catch (Throwable t) {
                throw new ComputationException(t);
            }
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/MapMaker$ComputingMapAdapter.class */
    static final class ComputingMapAdapter<K, V> extends ComputingConcurrentHashMap<K, V> implements Serializable {
        private static final long serialVersionUID = 0;

        ComputingMapAdapter(MapMaker mapMaker, Function<? super K, ? extends V> computingFunction) {
            super(mapMaker, computingFunction);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.MapMakerInternalMap, java.util.AbstractMap, java.util.Map
        public V get(Object obj) throws Throwable {
            try {
                V value = getOrCompute(obj);
                if (value == null) {
                    String strValueOf = String.valueOf(String.valueOf(this.computingFunction));
                    String strValueOf2 = String.valueOf(String.valueOf(obj));
                    throw new NullPointerException(new StringBuilder(24 + strValueOf.length() + strValueOf2.length()).append(strValueOf).append(" returned null for key ").append(strValueOf2).append(".").toString());
                }
                return value;
            } catch (ExecutionException e) {
                Throwable cause = e.getCause();
                Throwables.propagateIfInstanceOf(cause, ComputationException.class);
                throw new ComputationException(cause);
            }
        }
    }
}
