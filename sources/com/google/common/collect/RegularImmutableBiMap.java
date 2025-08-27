package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableMapEntry;
import java.io.Serializable;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true, emulated = true)
/* loaded from: guava-18.0.jar:com/google/common/collect/RegularImmutableBiMap.class */
class RegularImmutableBiMap<K, V> extends ImmutableBiMap<K, V> {
    static final double MAX_LOAD_FACTOR = 1.2d;
    private final transient ImmutableMapEntry<K, V>[] keyTable;
    private final transient ImmutableMapEntry<K, V>[] valueTable;
    private final transient ImmutableMapEntry<K, V>[] entries;
    private final transient int mask;
    private final transient int hashCode;
    private transient ImmutableBiMap<V, K> inverse;

    RegularImmutableBiMap(ImmutableMapEntry.TerminalEntry<?, ?>... entriesToAdd) {
        this(entriesToAdd.length, entriesToAdd);
    }

    RegularImmutableBiMap(int n, ImmutableMapEntry.TerminalEntry<?, ?>[] entriesToAdd) {
        int tableSize = Hashing.closedTableSize(n, MAX_LOAD_FACTOR);
        this.mask = tableSize - 1;
        ImmutableMapEntry<K, V>[] keyTable = createEntryArray(tableSize);
        ImmutableMapEntry<K, V>[] valueTable = createEntryArray(tableSize);
        ImmutableMapEntry<K, V>[] entries = createEntryArray(n);
        int hashCode = 0;
        for (int i = 0; i < n; i++) {
            ImmutableMapEntry.TerminalEntry<?, ?> terminalEntry = entriesToAdd[i];
            Object key = terminalEntry.getKey();
            Object value = terminalEntry.getValue();
            int keyHash = key.hashCode();
            int valueHash = value.hashCode();
            int keyBucket = Hashing.smear(keyHash) & this.mask;
            int valueBucket = Hashing.smear(valueHash) & this.mask;
            ImmutableMapEntry<K, V> nextInKeyBucket = keyTable[keyBucket];
            ImmutableMapEntry<K, V> nextInKeyBucket2 = nextInKeyBucket;
            while (true) {
                ImmutableMapEntry<K, V> keyEntry = nextInKeyBucket2;
                if (keyEntry == null) {
                    break;
                }
                checkNoConflict(!key.equals(keyEntry.getKey()), "key", terminalEntry, keyEntry);
                nextInKeyBucket2 = keyEntry.getNextInKeyBucket();
            }
            ImmutableMapEntry<K, V> nextInValueBucket = valueTable[valueBucket];
            ImmutableMapEntry<K, V> nextInValueBucket2 = nextInValueBucket;
            while (true) {
                ImmutableMapEntry<K, V> valueEntry = nextInValueBucket2;
                if (valueEntry == null) {
                    break;
                }
                checkNoConflict(!value.equals(valueEntry.getValue()), "value", terminalEntry, valueEntry);
                nextInValueBucket2 = valueEntry.getNextInValueBucket();
            }
            ImmutableMapEntry<K, V> newEntry = (nextInKeyBucket == null && nextInValueBucket == null) ? terminalEntry : new NonTerminalBiMapEntry<>(terminalEntry, nextInKeyBucket, nextInValueBucket);
            keyTable[keyBucket] = newEntry;
            valueTable[valueBucket] = newEntry;
            entries[i] = newEntry;
            hashCode += keyHash ^ valueHash;
        }
        this.keyTable = keyTable;
        this.valueTable = valueTable;
        this.entries = entries;
        this.hashCode = hashCode;
    }

    RegularImmutableBiMap(Map.Entry<?, ?>[] entriesToAdd) {
        int n = entriesToAdd.length;
        int tableSize = Hashing.closedTableSize(n, MAX_LOAD_FACTOR);
        this.mask = tableSize - 1;
        ImmutableMapEntry<K, V>[] keyTable = createEntryArray(tableSize);
        ImmutableMapEntry<K, V>[] valueTable = createEntryArray(tableSize);
        ImmutableMapEntry<K, V>[] entries = createEntryArray(n);
        int hashCode = 0;
        for (int i = 0; i < n; i++) {
            Map.Entry<?, ?> entry = entriesToAdd[i];
            Object key = entry.getKey();
            Object value = entry.getValue();
            CollectPreconditions.checkEntryNotNull(key, value);
            int keyHash = key.hashCode();
            int valueHash = value.hashCode();
            int keyBucket = Hashing.smear(keyHash) & this.mask;
            int valueBucket = Hashing.smear(valueHash) & this.mask;
            ImmutableMapEntry<K, V> nextInKeyBucket = keyTable[keyBucket];
            ImmutableMapEntry<K, V> nextInKeyBucket2 = nextInKeyBucket;
            while (true) {
                ImmutableMapEntry<K, V> keyEntry = nextInKeyBucket2;
                if (keyEntry == null) {
                    break;
                }
                checkNoConflict(!key.equals(keyEntry.getKey()), "key", entry, keyEntry);
                nextInKeyBucket2 = keyEntry.getNextInKeyBucket();
            }
            ImmutableMapEntry<K, V> nextInValueBucket = valueTable[valueBucket];
            ImmutableMapEntry<K, V> nextInValueBucket2 = nextInValueBucket;
            while (true) {
                ImmutableMapEntry<K, V> valueEntry = nextInValueBucket2;
                if (valueEntry == null) {
                    break;
                }
                checkNoConflict(!value.equals(valueEntry.getValue()), "value", entry, valueEntry);
                nextInValueBucket2 = valueEntry.getNextInValueBucket();
            }
            ImmutableMapEntry<K, V> newEntry = (nextInKeyBucket == null && nextInValueBucket == null) ? new ImmutableMapEntry.TerminalEntry<>(key, value) : new NonTerminalBiMapEntry<>(key, value, nextInKeyBucket, nextInValueBucket);
            keyTable[keyBucket] = newEntry;
            valueTable[valueBucket] = newEntry;
            entries[i] = newEntry;
            hashCode += keyHash ^ valueHash;
        }
        this.keyTable = keyTable;
        this.valueTable = valueTable;
        this.entries = entries;
        this.hashCode = hashCode;
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/RegularImmutableBiMap$NonTerminalBiMapEntry.class */
    private static final class NonTerminalBiMapEntry<K, V> extends ImmutableMapEntry<K, V> {

        @Nullable
        private final ImmutableMapEntry<K, V> nextInKeyBucket;

        @Nullable
        private final ImmutableMapEntry<K, V> nextInValueBucket;

        NonTerminalBiMapEntry(K key, V value, @Nullable ImmutableMapEntry<K, V> nextInKeyBucket, @Nullable ImmutableMapEntry<K, V> nextInValueBucket) {
            super(key, value);
            this.nextInKeyBucket = nextInKeyBucket;
            this.nextInValueBucket = nextInValueBucket;
        }

        NonTerminalBiMapEntry(ImmutableMapEntry<K, V> contents, @Nullable ImmutableMapEntry<K, V> nextInKeyBucket, @Nullable ImmutableMapEntry<K, V> nextInValueBucket) {
            super(contents);
            this.nextInKeyBucket = nextInKeyBucket;
            this.nextInValueBucket = nextInValueBucket;
        }

        @Override // com.google.common.collect.ImmutableMapEntry
        @Nullable
        ImmutableMapEntry<K, V> getNextInKeyBucket() {
            return this.nextInKeyBucket;
        }

        @Override // com.google.common.collect.ImmutableMapEntry
        @Nullable
        ImmutableMapEntry<K, V> getNextInValueBucket() {
            return this.nextInValueBucket;
        }
    }

    private static <K, V> ImmutableMapEntry<K, V>[] createEntryArray(int length) {
        return new ImmutableMapEntry[length];
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    @Nullable
    public V get(@Nullable Object key) {
        if (key == null) {
            return null;
        }
        int bucket = Hashing.smear(key.hashCode()) & this.mask;
        ImmutableMapEntry<K, V> nextInKeyBucket = this.keyTable[bucket];
        while (true) {
            ImmutableMapEntry<K, V> entry = nextInKeyBucket;
            if (entry != null) {
                if (!key.equals(entry.getKey())) {
                    nextInKeyBucket = entry.getNextInKeyBucket();
                } else {
                    return entry.getValue();
                }
            } else {
                return null;
            }
        }
    }

    @Override // com.google.common.collect.ImmutableMap
    ImmutableSet<Map.Entry<K, V>> createEntrySet() {
        return new ImmutableMapEntrySet<K, V>() { // from class: com.google.common.collect.RegularImmutableBiMap.1
            @Override // com.google.common.collect.ImmutableMapEntrySet
            ImmutableMap<K, V> map() {
                return RegularImmutableBiMap.this;
            }

            @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
                return asList().iterator();
            }

            @Override // com.google.common.collect.ImmutableCollection
            ImmutableList<Map.Entry<K, V>> createAsList() {
                return new RegularImmutableAsList(this, RegularImmutableBiMap.this.entries);
            }

            @Override // com.google.common.collect.ImmutableSet
            boolean isHashCodeFast() {
                return true;
            }

            @Override // com.google.common.collect.ImmutableSet, java.util.Collection, java.util.Set
            public int hashCode() {
                return RegularImmutableBiMap.this.hashCode;
            }
        };
    }

    @Override // com.google.common.collect.ImmutableMap
    boolean isPartialView() {
        return false;
    }

    @Override // java.util.Map
    public int size() {
        return this.entries.length;
    }

    @Override // com.google.common.collect.ImmutableBiMap, com.google.common.collect.BiMap
    public ImmutableBiMap<V, K> inverse() {
        ImmutableBiMap<V, K> result = this.inverse;
        if (result != null) {
            return result;
        }
        Inverse inverse = new Inverse();
        this.inverse = inverse;
        return inverse;
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/RegularImmutableBiMap$Inverse.class */
    private final class Inverse extends ImmutableBiMap<V, K> {
        private Inverse() {
        }

        @Override // java.util.Map
        public int size() {
            return inverse().size();
        }

        @Override // com.google.common.collect.ImmutableBiMap, com.google.common.collect.BiMap
        public ImmutableBiMap<K, V> inverse() {
            return RegularImmutableBiMap.this;
        }

        @Override // com.google.common.collect.ImmutableMap, java.util.Map
        public K get(@Nullable Object value) {
            if (value != null) {
                int bucket = Hashing.smear(value.hashCode()) & RegularImmutableBiMap.this.mask;
                ImmutableMapEntry<K, V> nextInValueBucket = RegularImmutableBiMap.this.valueTable[bucket];
                while (true) {
                    ImmutableMapEntry<K, V> entry = nextInValueBucket;
                    if (entry != null) {
                        if (!value.equals(entry.getValue())) {
                            nextInValueBucket = entry.getNextInValueBucket();
                        } else {
                            return entry.getKey();
                        }
                    } else {
                        return null;
                    }
                }
            } else {
                return null;
            }
        }

        @Override // com.google.common.collect.ImmutableMap
        ImmutableSet<Map.Entry<V, K>> createEntrySet() {
            return new InverseEntrySet();
        }

        /* loaded from: guava-18.0.jar:com/google/common/collect/RegularImmutableBiMap$Inverse$InverseEntrySet.class */
        final class InverseEntrySet extends ImmutableMapEntrySet<V, K> {
            InverseEntrySet() {
            }

            @Override // com.google.common.collect.ImmutableMapEntrySet
            ImmutableMap<V, K> map() {
                return Inverse.this;
            }

            @Override // com.google.common.collect.ImmutableSet
            boolean isHashCodeFast() {
                return true;
            }

            @Override // com.google.common.collect.ImmutableSet, java.util.Collection, java.util.Set
            public int hashCode() {
                return RegularImmutableBiMap.this.hashCode;
            }

            @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public UnmodifiableIterator<Map.Entry<V, K>> iterator() {
                return asList().iterator();
            }

            @Override // com.google.common.collect.ImmutableCollection
            ImmutableList<Map.Entry<V, K>> createAsList() {
                return new ImmutableAsList<Map.Entry<V, K>>() { // from class: com.google.common.collect.RegularImmutableBiMap.Inverse.InverseEntrySet.1
                    @Override // java.util.List
                    public Map.Entry<V, K> get(int index) {
                        Map.Entry<K, V> entry = RegularImmutableBiMap.this.entries[index];
                        return Maps.immutableEntry(entry.getValue(), entry.getKey());
                    }

                    @Override // com.google.common.collect.ImmutableAsList
                    ImmutableCollection<Map.Entry<V, K>> delegateCollection() {
                        return InverseEntrySet.this;
                    }
                };
            }
        }

        @Override // com.google.common.collect.ImmutableMap
        boolean isPartialView() {
            return false;
        }

        @Override // com.google.common.collect.ImmutableBiMap, com.google.common.collect.ImmutableMap
        Object writeReplace() {
            return new InverseSerializedForm(RegularImmutableBiMap.this);
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/RegularImmutableBiMap$InverseSerializedForm.class */
    private static class InverseSerializedForm<K, V> implements Serializable {
        private final ImmutableBiMap<K, V> forward;
        private static final long serialVersionUID = 1;

        InverseSerializedForm(ImmutableBiMap<K, V> forward) {
            this.forward = forward;
        }

        Object readResolve() {
            return this.forward.inverse();
        }
    }
}
