package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableMapEntry;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true, emulated = true)
/* loaded from: guava-18.0.jar:com/google/common/collect/RegularImmutableMap.class */
final class RegularImmutableMap<K, V> extends ImmutableMap<K, V> {
    private final transient ImmutableMapEntry<K, V>[] entries;
    private final transient ImmutableMapEntry<K, V>[] table;
    private final transient int mask;
    private static final double MAX_LOAD_FACTOR = 1.2d;
    private static final long serialVersionUID = 0;

    RegularImmutableMap(ImmutableMapEntry.TerminalEntry<?, ?>... theEntries) {
        this(theEntries.length, theEntries);
    }

    /* JADX WARN: Multi-variable type inference failed */
    RegularImmutableMap(int size, ImmutableMapEntry.TerminalEntry<?, ?>[] theEntries) {
        this.entries = createEntryArray(size);
        int tableSize = Hashing.closedTableSize(size, MAX_LOAD_FACTOR);
        this.table = createEntryArray(tableSize);
        this.mask = tableSize - 1;
        for (int entryIndex = 0; entryIndex < size; entryIndex++) {
            ImmutableMapEntry.TerminalEntry<?, ?> terminalEntry = theEntries[entryIndex];
            Object key = terminalEntry.getKey();
            int tableIndex = Hashing.smear(key.hashCode()) & this.mask;
            ImmutableMapEntry<K, V> existing = this.table[tableIndex];
            ImmutableMapEntry<K, V> newEntry = existing == null ? terminalEntry : new NonTerminalMapEntry<>(terminalEntry, existing);
            this.table[tableIndex] = newEntry;
            this.entries[entryIndex] = newEntry;
            checkNoConflictInBucket(key, newEntry, existing);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    RegularImmutableMap(Map.Entry<?, ?>[] theEntries) {
        int size = theEntries.length;
        this.entries = createEntryArray(size);
        int tableSize = Hashing.closedTableSize(size, MAX_LOAD_FACTOR);
        this.table = createEntryArray(tableSize);
        this.mask = tableSize - 1;
        for (int entryIndex = 0; entryIndex < size; entryIndex++) {
            Map.Entry<?, ?> entry = theEntries[entryIndex];
            Object key = entry.getKey();
            Object value = entry.getValue();
            CollectPreconditions.checkEntryNotNull(key, value);
            int tableIndex = Hashing.smear(key.hashCode()) & this.mask;
            ImmutableMapEntry<K, V> existing = this.table[tableIndex];
            ImmutableMapEntry<K, V> newEntry = existing == null ? new ImmutableMapEntry.TerminalEntry<>(key, value) : new NonTerminalMapEntry<>(key, value, existing);
            this.table[tableIndex] = newEntry;
            this.entries[entryIndex] = newEntry;
            checkNoConflictInBucket(key, newEntry, existing);
        }
    }

    private void checkNoConflictInBucket(K key, ImmutableMapEntry<K, V> entry, ImmutableMapEntry<K, V> bucketHead) {
        while (bucketHead != null) {
            checkNoConflict(!key.equals(bucketHead.getKey()), "key", entry, bucketHead);
            bucketHead = bucketHead.getNextInKeyBucket();
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/RegularImmutableMap$NonTerminalMapEntry.class */
    private static final class NonTerminalMapEntry<K, V> extends ImmutableMapEntry<K, V> {
        private final ImmutableMapEntry<K, V> nextInKeyBucket;

        NonTerminalMapEntry(K key, V value, ImmutableMapEntry<K, V> nextInKeyBucket) {
            super(key, value);
            this.nextInKeyBucket = nextInKeyBucket;
        }

        NonTerminalMapEntry(ImmutableMapEntry<K, V> contents, ImmutableMapEntry<K, V> nextInKeyBucket) {
            super(contents);
            this.nextInKeyBucket = nextInKeyBucket;
        }

        @Override // com.google.common.collect.ImmutableMapEntry
        ImmutableMapEntry<K, V> getNextInKeyBucket() {
            return this.nextInKeyBucket;
        }

        @Override // com.google.common.collect.ImmutableMapEntry
        @Nullable
        ImmutableMapEntry<K, V> getNextInValueBucket() {
            return null;
        }
    }

    private ImmutableMapEntry<K, V>[] createEntryArray(int size) {
        return new ImmutableMapEntry[size];
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public V get(@Nullable Object key) {
        if (key == null) {
            return null;
        }
        int index = Hashing.smear(key.hashCode()) & this.mask;
        ImmutableMapEntry<K, V> nextInKeyBucket = this.table[index];
        while (true) {
            ImmutableMapEntry<K, V> entry = nextInKeyBucket;
            if (entry != null) {
                K candidateKey = entry.getKey();
                if (!key.equals(candidateKey)) {
                    nextInKeyBucket = entry.getNextInKeyBucket();
                } else {
                    return entry.getValue();
                }
            } else {
                return null;
            }
        }
    }

    @Override // java.util.Map
    public int size() {
        return this.entries.length;
    }

    @Override // com.google.common.collect.ImmutableMap
    boolean isPartialView() {
        return false;
    }

    @Override // com.google.common.collect.ImmutableMap
    ImmutableSet<Map.Entry<K, V>> createEntrySet() {
        return new EntrySet();
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/RegularImmutableMap$EntrySet.class */
    private class EntrySet extends ImmutableMapEntrySet<K, V> {
        private EntrySet() {
        }

        @Override // com.google.common.collect.ImmutableMapEntrySet
        ImmutableMap<K, V> map() {
            return RegularImmutableMap.this;
        }

        @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
            return asList().iterator();
        }

        @Override // com.google.common.collect.ImmutableCollection
        ImmutableList<Map.Entry<K, V>> createAsList() {
            return new RegularImmutableAsList(this, RegularImmutableMap.this.entries);
        }
    }
}
