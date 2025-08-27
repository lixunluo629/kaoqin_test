package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.collect.MapMaker;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/* JADX INFO: Access modifiers changed from: package-private */
@Beta
@GwtCompatible(emulated = true)
@Deprecated
/* loaded from: guava-18.0.jar:com/google/common/collect/GenericMapMaker.class */
public abstract class GenericMapMaker<K0, V0> {

    @GwtIncompatible("To be supported")
    MapMaker.RemovalListener<K0, V0> removalListener;

    @GwtIncompatible("To be supported")
    abstract GenericMapMaker<K0, V0> keyEquivalence(Equivalence<Object> equivalence);

    /* renamed from: initialCapacity */
    public abstract GenericMapMaker<K0, V0> initialCapacity2(int i);

    /* renamed from: maximumSize */
    abstract GenericMapMaker<K0, V0> maximumSize2(int i);

    /* renamed from: concurrencyLevel */
    public abstract GenericMapMaker<K0, V0> concurrencyLevel2(int i);

    @GwtIncompatible("java.lang.ref.WeakReference")
    /* renamed from: weakKeys */
    public abstract GenericMapMaker<K0, V0> weakKeys2();

    @GwtIncompatible("java.lang.ref.WeakReference")
    /* renamed from: weakValues */
    public abstract GenericMapMaker<K0, V0> weakValues2();

    @GwtIncompatible("java.lang.ref.SoftReference")
    @Deprecated
    /* renamed from: softValues */
    public abstract GenericMapMaker<K0, V0> softValues2();

    /* renamed from: expireAfterWrite */
    abstract GenericMapMaker<K0, V0> expireAfterWrite2(long j, TimeUnit timeUnit);

    @GwtIncompatible("To be supported")
    /* renamed from: expireAfterAccess */
    abstract GenericMapMaker<K0, V0> expireAfterAccess2(long j, TimeUnit timeUnit);

    public abstract <K extends K0, V extends V0> ConcurrentMap<K, V> makeMap();

    @GwtIncompatible("MapMakerInternalMap")
    abstract <K, V> MapMakerInternalMap<K, V> makeCustomMap();

    @Deprecated
    abstract <K extends K0, V extends V0> ConcurrentMap<K, V> makeComputingMap(Function<? super K, ? extends V> function);

    @GwtIncompatible("To be supported")
    /* loaded from: guava-18.0.jar:com/google/common/collect/GenericMapMaker$NullListener.class */
    enum NullListener implements MapMaker.RemovalListener<Object, Object> {
        INSTANCE;

        @Override // com.google.common.collect.MapMaker.RemovalListener
        public void onRemoval(MapMaker.RemovalNotification<Object, Object> notification) {
        }
    }

    GenericMapMaker() {
    }

    @GwtIncompatible("To be supported")
    <K extends K0, V extends V0> MapMaker.RemovalListener<K, V> getRemovalListener() {
        return (MapMaker.RemovalListener) MoreObjects.firstNonNull(this.removalListener, NullListener.INSTANCE);
    }
}
