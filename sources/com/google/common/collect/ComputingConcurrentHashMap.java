package com.google.common.collect;

import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.MapMaker;
import com.google.common.collect.MapMakerInternalMap;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.ReferenceQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;

/* loaded from: guava-18.0.jar:com/google/common/collect/ComputingConcurrentHashMap.class */
class ComputingConcurrentHashMap<K, V> extends MapMakerInternalMap<K, V> {
    final Function<? super K, ? extends V> computingFunction;
    private static final long serialVersionUID = 4;

    ComputingConcurrentHashMap(MapMaker builder, Function<? super K, ? extends V> computingFunction) {
        super(builder);
        this.computingFunction = (Function) Preconditions.checkNotNull(computingFunction);
    }

    @Override // com.google.common.collect.MapMakerInternalMap
    MapMakerInternalMap.Segment<K, V> createSegment(int initialCapacity, int maxSegmentSize) {
        return new ComputingSegment(this, initialCapacity, maxSegmentSize);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.MapMakerInternalMap
    public ComputingSegment<K, V> segmentFor(int hash) {
        return (ComputingSegment) super.segmentFor(hash);
    }

    V getOrCompute(K key) throws ExecutionException {
        int hash = hash(Preconditions.checkNotNull(key));
        return segmentFor(hash).getOrCompute(key, hash, this.computingFunction);
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment.class */
    static final class ComputingSegment<K, V> extends MapMakerInternalMap.Segment<K, V> {
        ComputingSegment(MapMakerInternalMap<K, V> map, int initialCapacity, int maxSegmentSize) {
            super(map, initialCapacity, maxSegmentSize);
        }

        /* JADX WARN: Code restructure failed: missing block: B:23:0x00a4, code lost:
        
            r0 = r10.getValueReference();
         */
        /* JADX WARN: Code restructure failed: missing block: B:24:0x00b4, code lost:
        
            if (r0.isComputingReference() == false) goto L26;
         */
        /* JADX WARN: Code restructure failed: missing block: B:25:0x00b7, code lost:
        
            r11 = false;
         */
        /* JADX WARN: Code restructure failed: missing block: B:26:0x00bd, code lost:
        
            r0 = r10.getValueReference().get();
         */
        /* JADX WARN: Code restructure failed: missing block: B:27:0x00cd, code lost:
        
            if (r0 != null) goto L29;
         */
        /* JADX WARN: Code restructure failed: missing block: B:28:0x00d0, code lost:
        
            enqueueNotification(r0, r8, r0, com.google.common.collect.MapMaker.RemovalCause.COLLECTED);
         */
        /* JADX WARN: Code restructure failed: missing block: B:30:0x00e6, code lost:
        
            if (r6.map.expires() == false) goto L74;
         */
        /* JADX WARN: Code restructure failed: missing block: B:32:0x00f2, code lost:
        
            if (r6.map.isExpired(r10) == false) goto L75;
         */
        /* JADX WARN: Code restructure failed: missing block: B:33:0x00f5, code lost:
        
            enqueueNotification(r0, r8, r0, com.google.common.collect.MapMaker.RemovalCause.EXPIRED);
         */
        /* JADX WARN: Code restructure failed: missing block: B:34:0x0104, code lost:
        
            recordLockedRead(r10);
         */
        /* JADX WARN: Code restructure failed: missing block: B:35:0x010f, code lost:
        
            unlock();
            postWriteCleanup();
         */
        /* JADX WARN: Code restructure failed: missing block: B:36:0x0117, code lost:
        
            postReadCleanup();
         */
        /* JADX WARN: Code restructure failed: missing block: B:37:0x011c, code lost:
        
            return r0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:38:0x011d, code lost:
        
            r6.evictionQueue.remove(r10);
            r6.expirationQueue.remove(r10);
            r6.count = r0;
         */
        /* JADX WARN: Finally extract failed */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        V getOrCompute(K r7, int r8, com.google.common.base.Function<? super K, ? extends V> r9) throws java.util.concurrent.ExecutionException {
            /*
                Method dump skipped, instructions count: 501
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.ComputingConcurrentHashMap.ComputingSegment.getOrCompute(java.lang.Object, int, com.google.common.base.Function):java.lang.Object");
        }

        V compute(K key, int hash, MapMakerInternalMap.ReferenceEntry<K, V> e, ComputingValueReference<K, V> computingValueReference) throws ExecutionException {
            V value = null;
            System.nanoTime();
            long end = 0;
            try {
                synchronized (e) {
                    value = computingValueReference.compute(key, hash);
                    end = System.nanoTime();
                }
                if (value != null) {
                    V oldValue = put(key, hash, value, true);
                    if (oldValue != null) {
                        enqueueNotification(key, hash, value, MapMaker.RemovalCause.REPLACED);
                    }
                }
                if (end == 0) {
                    System.nanoTime();
                }
                if (value == null) {
                    clearValue(key, hash, computingValueReference);
                }
                return value;
            } catch (Throwable th) {
                if (end == 0) {
                    System.nanoTime();
                }
                if (value == null) {
                    clearValue(key, hash, computingValueReference);
                }
                throw th;
            }
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/ComputingConcurrentHashMap$ComputationExceptionReference.class */
    private static final class ComputationExceptionReference<K, V> implements MapMakerInternalMap.ValueReference<K, V> {
        final Throwable t;

        ComputationExceptionReference(Throwable t) {
            this.t = t;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.ValueReference
        public V get() {
            return null;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.ValueReference
        public MapMakerInternalMap.ReferenceEntry<K, V> getEntry() {
            return null;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.ValueReference
        public MapMakerInternalMap.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, MapMakerInternalMap.ReferenceEntry<K, V> entry) {
            return this;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.ValueReference
        public boolean isComputingReference() {
            return false;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.ValueReference
        public V waitForValue() throws ExecutionException {
            throw new ExecutionException(this.t);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.ValueReference
        public void clear(MapMakerInternalMap.ValueReference<K, V> newValue) {
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/ComputingConcurrentHashMap$ComputedReference.class */
    private static final class ComputedReference<K, V> implements MapMakerInternalMap.ValueReference<K, V> {
        final V value;

        ComputedReference(@Nullable V value) {
            this.value = value;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.ValueReference
        public V get() {
            return this.value;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.ValueReference
        public MapMakerInternalMap.ReferenceEntry<K, V> getEntry() {
            return null;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.ValueReference
        public MapMakerInternalMap.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, MapMakerInternalMap.ReferenceEntry<K, V> entry) {
            return this;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.ValueReference
        public boolean isComputingReference() {
            return false;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.ValueReference
        public V waitForValue() {
            return get();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.ValueReference
        public void clear(MapMakerInternalMap.ValueReference<K, V> newValue) {
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/ComputingConcurrentHashMap$ComputingValueReference.class */
    private static final class ComputingValueReference<K, V> implements MapMakerInternalMap.ValueReference<K, V> {
        final Function<? super K, ? extends V> computingFunction;

        @GuardedBy("ComputingValueReference.this")
        volatile MapMakerInternalMap.ValueReference<K, V> computedReference = MapMakerInternalMap.unset();

        public ComputingValueReference(Function<? super K, ? extends V> computingFunction) {
            this.computingFunction = computingFunction;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.ValueReference
        public V get() {
            return null;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.ValueReference
        public MapMakerInternalMap.ReferenceEntry<K, V> getEntry() {
            return null;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.ValueReference
        public MapMakerInternalMap.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, @Nullable V value, MapMakerInternalMap.ReferenceEntry<K, V> entry) {
            return this;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.ValueReference
        public boolean isComputingReference() {
            return true;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.ValueReference
        public V waitForValue() throws ExecutionException {
            if (this.computedReference == MapMakerInternalMap.UNSET) {
                boolean interrupted = false;
                try {
                    synchronized (this) {
                        while (this.computedReference == MapMakerInternalMap.UNSET) {
                            try {
                                wait();
                            } catch (InterruptedException e) {
                                interrupted = true;
                            }
                        }
                    }
                } finally {
                    if (interrupted) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            return this.computedReference.waitForValue();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.ValueReference
        public void clear(MapMakerInternalMap.ValueReference<K, V> newValue) {
            setValueReference(newValue);
        }

        V compute(K key, int hash) throws ExecutionException {
            try {
                V value = this.computingFunction.apply(key);
                setValueReference(new ComputedReference(value));
                return value;
            } catch (Throwable t) {
                setValueReference(new ComputationExceptionReference(t));
                throw new ExecutionException(t);
            }
        }

        void setValueReference(MapMakerInternalMap.ValueReference<K, V> valueReference) {
            synchronized (this) {
                if (this.computedReference == MapMakerInternalMap.UNSET) {
                    this.computedReference = valueReference;
                    notifyAll();
                }
            }
        }
    }

    @Override // com.google.common.collect.MapMakerInternalMap
    Object writeReplace() {
        return new ComputingSerializationProxy(this.keyStrength, this.valueStrength, this.keyEquivalence, this.valueEquivalence, this.expireAfterWriteNanos, this.expireAfterAccessNanos, this.maximumSize, this.concurrencyLevel, this.removalListener, this, this.computingFunction);
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/ComputingConcurrentHashMap$ComputingSerializationProxy.class */
    static final class ComputingSerializationProxy<K, V> extends MapMakerInternalMap.AbstractSerializationProxy<K, V> {
        final Function<? super K, ? extends V> computingFunction;
        private static final long serialVersionUID = 4;

        ComputingSerializationProxy(MapMakerInternalMap.Strength keyStrength, MapMakerInternalMap.Strength valueStrength, Equivalence<Object> keyEquivalence, Equivalence<Object> valueEquivalence, long expireAfterWriteNanos, long expireAfterAccessNanos, int maximumSize, int concurrencyLevel, MapMaker.RemovalListener<? super K, ? super V> removalListener, ConcurrentMap<K, V> delegate, Function<? super K, ? extends V> computingFunction) {
            super(keyStrength, valueStrength, keyEquivalence, valueEquivalence, expireAfterWriteNanos, expireAfterAccessNanos, maximumSize, concurrencyLevel, removalListener, delegate);
            this.computingFunction = computingFunction;
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();
            writeMapTo(out);
        }

        private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
            in.defaultReadObject();
            MapMaker mapMaker = readMapMaker(in);
            this.delegate = mapMaker.makeComputingMap(this.computingFunction);
            readEntries(in);
        }

        Object readResolve() {
            return this.delegate;
        }
    }
}
