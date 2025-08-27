package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.MapMakerInternalMap;
import java.util.concurrent.ConcurrentMap;

@Beta
/* loaded from: guava-18.0.jar:com/google/common/collect/Interners.class */
public final class Interners {
    private Interners() {
    }

    public static <E> Interner<E> newStrongInterner() {
        final ConcurrentMap<E, E> map = new MapMaker().makeMap();
        return new Interner<E>() { // from class: com.google.common.collect.Interners.1
            @Override // com.google.common.collect.Interner
            public E intern(E e) {
                E e2 = (E) map.putIfAbsent(Preconditions.checkNotNull(e), e);
                return e2 == null ? e : e2;
            }
        };
    }

    @GwtIncompatible("java.lang.ref.WeakReference")
    public static <E> Interner<E> newWeakInterner() {
        return new WeakInterner();
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/Interners$WeakInterner.class */
    private static class WeakInterner<E> implements Interner<E> {
        private final MapMakerInternalMap<E, Dummy> map;

        /* loaded from: guava-18.0.jar:com/google/common/collect/Interners$WeakInterner$Dummy.class */
        private enum Dummy {
            VALUE
        }

        private WeakInterner() {
            this.map = (MapMakerInternalMap<E, Dummy>) new MapMaker().weakKeys2().keyEquivalence(Equivalence.equals()).makeCustomMap();
        }

        @Override // com.google.common.collect.Interner
        public E intern(E sample) {
            Dummy sneaky;
            E canonical;
            do {
                MapMakerInternalMap.ReferenceEntry<E, Dummy> entry = this.map.getEntry(sample);
                if (entry != null && (canonical = entry.getKey()) != null) {
                    return canonical;
                }
                sneaky = this.map.putIfAbsent(sample, Dummy.VALUE);
            } while (sneaky != null);
            return sample;
        }
    }

    public static <E> Function<E, E> asFunction(Interner<E> interner) {
        return new InternerFunction((Interner) Preconditions.checkNotNull(interner));
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/Interners$InternerFunction.class */
    private static class InternerFunction<E> implements Function<E, E> {
        private final Interner<E> interner;

        public InternerFunction(Interner<E> interner) {
            this.interner = interner;
        }

        @Override // com.google.common.base.Function
        public E apply(E input) {
            return this.interner.intern(input);
        }

        public int hashCode() {
            return this.interner.hashCode();
        }

        @Override // com.google.common.base.Function
        public boolean equals(Object other) {
            if (other instanceof InternerFunction) {
                InternerFunction<?> that = (InternerFunction) other;
                return this.interner.equals(that.interner);
            }
            return false;
        }
    }
}
