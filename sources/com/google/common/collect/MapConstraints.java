package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import javax.annotation.Nullable;

@Beta
@GwtCompatible
/* loaded from: guava-18.0.jar:com/google/common/collect/MapConstraints.class */
public final class MapConstraints {
    private MapConstraints() {
    }

    public static MapConstraint<Object, Object> notNull() {
        return NotNullMapConstraint.INSTANCE;
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/MapConstraints$NotNullMapConstraint.class */
    private enum NotNullMapConstraint implements MapConstraint<Object, Object> {
        INSTANCE;

        @Override // com.google.common.collect.MapConstraint
        public void checkKeyValue(Object key, Object value) {
            Preconditions.checkNotNull(key);
            Preconditions.checkNotNull(value);
        }

        @Override // java.lang.Enum, com.google.common.collect.MapConstraint
        public String toString() {
            return "Not null";
        }
    }

    public static <K, V> Map<K, V> constrainedMap(Map<K, V> map, MapConstraint<? super K, ? super V> constraint) {
        return new ConstrainedMap(map, constraint);
    }

    public static <K, V> Multimap<K, V> constrainedMultimap(Multimap<K, V> multimap, MapConstraint<? super K, ? super V> constraint) {
        return new ConstrainedMultimap(multimap, constraint);
    }

    public static <K, V> ListMultimap<K, V> constrainedListMultimap(ListMultimap<K, V> multimap, MapConstraint<? super K, ? super V> constraint) {
        return new ConstrainedListMultimap(multimap, constraint);
    }

    public static <K, V> SetMultimap<K, V> constrainedSetMultimap(SetMultimap<K, V> multimap, MapConstraint<? super K, ? super V> constraint) {
        return new ConstrainedSetMultimap(multimap, constraint);
    }

    public static <K, V> SortedSetMultimap<K, V> constrainedSortedSetMultimap(SortedSetMultimap<K, V> multimap, MapConstraint<? super K, ? super V> constraint) {
        return new ConstrainedSortedSetMultimap(multimap, constraint);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <K, V> Map.Entry<K, V> constrainedEntry(final Map.Entry<K, V> entry, final MapConstraint<? super K, ? super V> constraint) {
        Preconditions.checkNotNull(entry);
        Preconditions.checkNotNull(constraint);
        return new ForwardingMapEntry<K, V>() { // from class: com.google.common.collect.MapConstraints.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.google.common.collect.ForwardingMapEntry, com.google.common.collect.ForwardingObject
            public Map.Entry<K, V> delegate() {
                return entry;
            }

            @Override // com.google.common.collect.ForwardingMapEntry, java.util.Map.Entry
            public V setValue(V v) {
                constraint.checkKeyValue(getKey(), v);
                return (V) entry.setValue(v);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <K, V> Map.Entry<K, Collection<V>> constrainedAsMapEntry(final Map.Entry<K, Collection<V>> entry, final MapConstraint<? super K, ? super V> constraint) {
        Preconditions.checkNotNull(entry);
        Preconditions.checkNotNull(constraint);
        return new ForwardingMapEntry<K, Collection<V>>() { // from class: com.google.common.collect.MapConstraints.2
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.google.common.collect.ForwardingMapEntry, com.google.common.collect.ForwardingObject
            public Map.Entry<K, Collection<V>> delegate() {
                return entry;
            }

            @Override // com.google.common.collect.ForwardingMapEntry, java.util.Map.Entry
            public Collection<V> getValue() {
                return Constraints.constrainedTypePreservingCollection((Collection) entry.getValue(), new Constraint<V>() { // from class: com.google.common.collect.MapConstraints.2.1
                    @Override // com.google.common.collect.Constraint
                    public V checkElement(V value) {
                        constraint.checkKeyValue(getKey(), value);
                        return value;
                    }
                });
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <K, V> Set<Map.Entry<K, Collection<V>>> constrainedAsMapEntries(Set<Map.Entry<K, Collection<V>>> entries, MapConstraint<? super K, ? super V> constraint) {
        return new ConstrainedAsMapEntries(entries, constraint);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <K, V> Collection<Map.Entry<K, V>> constrainedEntries(Collection<Map.Entry<K, V>> entries, MapConstraint<? super K, ? super V> constraint) {
        if (entries instanceof Set) {
            return constrainedEntrySet((Set) entries, constraint);
        }
        return new ConstrainedEntries(entries, constraint);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <K, V> Set<Map.Entry<K, V>> constrainedEntrySet(Set<Map.Entry<K, V>> entries, MapConstraint<? super K, ? super V> constraint) {
        return new ConstrainedEntrySet(entries, constraint);
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/MapConstraints$ConstrainedMap.class */
    static class ConstrainedMap<K, V> extends ForwardingMap<K, V> {
        private final Map<K, V> delegate;
        final MapConstraint<? super K, ? super V> constraint;
        private transient Set<Map.Entry<K, V>> entrySet;

        ConstrainedMap(Map<K, V> delegate, MapConstraint<? super K, ? super V> constraint) {
            this.delegate = (Map) Preconditions.checkNotNull(delegate);
            this.constraint = (MapConstraint) Preconditions.checkNotNull(constraint);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
        public Map<K, V> delegate() {
            return this.delegate;
        }

        @Override // com.google.common.collect.ForwardingMap, java.util.Map
        public Set<Map.Entry<K, V>> entrySet() {
            Set<Map.Entry<K, V>> result = this.entrySet;
            if (result == null) {
                Set<Map.Entry<K, V>> setConstrainedEntrySet = MapConstraints.constrainedEntrySet(this.delegate.entrySet(), this.constraint);
                result = setConstrainedEntrySet;
                this.entrySet = setConstrainedEntrySet;
            }
            return result;
        }

        @Override // com.google.common.collect.ForwardingMap, java.util.Map, com.google.common.collect.BiMap
        public V put(K key, V value) {
            this.constraint.checkKeyValue(key, value);
            return this.delegate.put(key, value);
        }

        @Override // com.google.common.collect.ForwardingMap, java.util.Map, com.google.common.collect.BiMap
        public void putAll(Map<? extends K, ? extends V> map) {
            this.delegate.putAll(MapConstraints.checkMap(map, this.constraint));
        }
    }

    public static <K, V> BiMap<K, V> constrainedBiMap(BiMap<K, V> map, MapConstraint<? super K, ? super V> constraint) {
        return new ConstrainedBiMap(map, null, constraint);
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/MapConstraints$ConstrainedBiMap.class */
    private static class ConstrainedBiMap<K, V> extends ConstrainedMap<K, V> implements BiMap<K, V> {
        volatile BiMap<V, K> inverse;

        ConstrainedBiMap(BiMap<K, V> delegate, @Nullable BiMap<V, K> inverse, MapConstraint<? super K, ? super V> constraint) {
            super(delegate, constraint);
            this.inverse = inverse;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.collect.MapConstraints.ConstrainedMap, com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
        public BiMap<K, V> delegate() {
            return (BiMap) super.delegate();
        }

        @Override // com.google.common.collect.BiMap
        public V forcePut(K key, V value) {
            this.constraint.checkKeyValue(key, value);
            return delegate().forcePut(key, value);
        }

        @Override // com.google.common.collect.BiMap
        public BiMap<V, K> inverse() {
            if (this.inverse == null) {
                this.inverse = new ConstrainedBiMap(delegate().inverse(), this, new InverseConstraint(this.constraint));
            }
            return this.inverse;
        }

        @Override // com.google.common.collect.ForwardingMap, java.util.Map, com.google.common.collect.BiMap
        public Set<V> values() {
            return delegate().values();
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/MapConstraints$InverseConstraint.class */
    private static class InverseConstraint<K, V> implements MapConstraint<K, V> {
        final MapConstraint<? super V, ? super K> constraint;

        public InverseConstraint(MapConstraint<? super V, ? super K> constraint) {
            this.constraint = (MapConstraint) Preconditions.checkNotNull(constraint);
        }

        @Override // com.google.common.collect.MapConstraint
        public void checkKeyValue(K key, V value) {
            this.constraint.checkKeyValue(value, key);
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/MapConstraints$ConstrainedMultimap.class */
    private static class ConstrainedMultimap<K, V> extends ForwardingMultimap<K, V> implements Serializable {
        final MapConstraint<? super K, ? super V> constraint;
        final Multimap<K, V> delegate;
        transient Collection<Map.Entry<K, V>> entries;
        transient Map<K, Collection<V>> asMap;

        public ConstrainedMultimap(Multimap<K, V> delegate, MapConstraint<? super K, ? super V> constraint) {
            this.delegate = (Multimap) Preconditions.checkNotNull(delegate);
            this.constraint = (MapConstraint) Preconditions.checkNotNull(constraint);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.collect.ForwardingMultimap, com.google.common.collect.ForwardingObject
        public Multimap<K, V> delegate() {
            return this.delegate;
        }

        @Override // com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public Map<K, Collection<V>> asMap() {
            Map<K, Collection<V>> result = this.asMap;
            if (result == null) {
                final Map<K, Collection<V>> asMapDelegate = this.delegate.asMap();
                ForwardingMap<K, Collection<V>> forwardingMap = new ForwardingMap<K, Collection<V>>() { // from class: com.google.common.collect.MapConstraints.ConstrainedMultimap.1
                    Set<Map.Entry<K, Collection<V>>> entrySet;
                    Collection<Collection<V>> values;

                    /* JADX INFO: Access modifiers changed from: protected */
                    @Override // com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
                    public Map<K, Collection<V>> delegate() {
                        return asMapDelegate;
                    }

                    @Override // com.google.common.collect.ForwardingMap, java.util.Map
                    public Set<Map.Entry<K, Collection<V>>> entrySet() {
                        Set<Map.Entry<K, Collection<V>>> result2 = this.entrySet;
                        if (result2 == null) {
                            Set<Map.Entry<K, Collection<V>>> setConstrainedAsMapEntries = MapConstraints.constrainedAsMapEntries(asMapDelegate.entrySet(), ConstrainedMultimap.this.constraint);
                            result2 = setConstrainedAsMapEntries;
                            this.entrySet = setConstrainedAsMapEntries;
                        }
                        return result2;
                    }

                    @Override // com.google.common.collect.ForwardingMap, java.util.Map
                    public Collection<V> get(Object key) {
                        try {
                            Collection<V> collection = ConstrainedMultimap.this.get(key);
                            if (collection.isEmpty()) {
                                return null;
                            }
                            return collection;
                        } catch (ClassCastException e) {
                            return null;
                        }
                    }

                    @Override // com.google.common.collect.ForwardingMap, java.util.Map, com.google.common.collect.BiMap
                    public Collection<Collection<V>> values() {
                        Collection<Collection<V>> result2 = this.values;
                        if (result2 == null) {
                            ConstrainedAsMapValues constrainedAsMapValues = new ConstrainedAsMapValues(delegate().values(), entrySet());
                            result2 = constrainedAsMapValues;
                            this.values = constrainedAsMapValues;
                        }
                        return result2;
                    }

                    @Override // com.google.common.collect.ForwardingMap, java.util.Map
                    public boolean containsValue(Object o) {
                        return values().contains(o);
                    }
                };
                result = forwardingMap;
                this.asMap = forwardingMap;
            }
            return result;
        }

        @Override // com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public Collection<Map.Entry<K, V>> entries() {
            Collection<Map.Entry<K, V>> result = this.entries;
            if (result == null) {
                Collection<Map.Entry<K, V>> collectionConstrainedEntries = MapConstraints.constrainedEntries(this.delegate.entries(), this.constraint);
                result = collectionConstrainedEntries;
                this.entries = collectionConstrainedEntries;
            }
            return result;
        }

        @Override // com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public Collection<V> get(final K key) {
            return Constraints.constrainedTypePreservingCollection(this.delegate.get(key), new Constraint<V>() { // from class: com.google.common.collect.MapConstraints.ConstrainedMultimap.2
                @Override // com.google.common.collect.Constraint
                public V checkElement(V v) {
                    ConstrainedMultimap.this.constraint.checkKeyValue((Object) key, v);
                    return v;
                }
            });
        }

        @Override // com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public boolean put(K key, V value) {
            this.constraint.checkKeyValue(key, value);
            return this.delegate.put(key, value);
        }

        @Override // com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public boolean putAll(K key, Iterable<? extends V> values) {
            return this.delegate.putAll(key, MapConstraints.checkValues(key, values, this.constraint));
        }

        @Override // com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
            boolean changed = false;
            for (Map.Entry<? extends K, ? extends V> entry : multimap.entries()) {
                changed |= put(entry.getKey(), entry.getValue());
            }
            return changed;
        }

        @Override // com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public Collection<V> replaceValues(K key, Iterable<? extends V> values) {
            return this.delegate.replaceValues(key, MapConstraints.checkValues(key, values, this.constraint));
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/MapConstraints$ConstrainedAsMapValues.class */
    private static class ConstrainedAsMapValues<K, V> extends ForwardingCollection<Collection<V>> {
        final Collection<Collection<V>> delegate;
        final Set<Map.Entry<K, Collection<V>>> entrySet;

        ConstrainedAsMapValues(Collection<Collection<V>> delegate, Set<Map.Entry<K, Collection<V>>> entrySet) {
            this.delegate = delegate;
            this.entrySet = entrySet;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        public Collection<Collection<V>> delegate() {
            return this.delegate;
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Collection<V>> iterator() {
            final Iterator<Map.Entry<K, Collection<V>>> iterator = this.entrySet.iterator();
            return new Iterator<Collection<V>>() { // from class: com.google.common.collect.MapConstraints.ConstrainedAsMapValues.1
                @Override // java.util.Iterator
                public boolean hasNext() {
                    return iterator.hasNext();
                }

                @Override // java.util.Iterator
                public Collection<V> next() {
                    return (Collection) ((Map.Entry) iterator.next()).getValue();
                }

                @Override // java.util.Iterator
                public void remove() {
                    iterator.remove();
                }
            };
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public Object[] toArray() {
            return standardToArray();
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public <T> T[] toArray(T[] tArr) {
            return (T[]) standardToArray(tArr);
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            return standardContains(o);
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean containsAll(Collection<?> c) {
            return standardContainsAll(c);
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            return standardRemove(o);
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean removeAll(Collection<?> c) {
            return standardRemoveAll(c);
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean retainAll(Collection<?> c) {
            return standardRetainAll(c);
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/MapConstraints$ConstrainedEntries.class */
    private static class ConstrainedEntries<K, V> extends ForwardingCollection<Map.Entry<K, V>> {
        final MapConstraint<? super K, ? super V> constraint;
        final Collection<Map.Entry<K, V>> entries;

        ConstrainedEntries(Collection<Map.Entry<K, V>> entries, MapConstraint<? super K, ? super V> constraint) {
            this.entries = entries;
            this.constraint = constraint;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        public Collection<Map.Entry<K, V>> delegate() {
            return this.entries;
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<K, V>> iterator() {
            final Iterator<Map.Entry<K, V>> iterator = this.entries.iterator();
            return new ForwardingIterator<Map.Entry<K, V>>() { // from class: com.google.common.collect.MapConstraints.ConstrainedEntries.1
                @Override // com.google.common.collect.ForwardingIterator, java.util.Iterator
                public Map.Entry<K, V> next() {
                    return MapConstraints.constrainedEntry((Map.Entry) iterator.next(), ConstrainedEntries.this.constraint);
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // com.google.common.collect.ForwardingIterator, com.google.common.collect.ForwardingObject
                public Iterator<Map.Entry<K, V>> delegate() {
                    return iterator;
                }
            };
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public Object[] toArray() {
            return standardToArray();
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public <T> T[] toArray(T[] tArr) {
            return (T[]) standardToArray(tArr);
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            return Maps.containsEntryImpl(delegate(), o);
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean containsAll(Collection<?> c) {
            return standardContainsAll(c);
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            return Maps.removeEntryImpl(delegate(), o);
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean removeAll(Collection<?> c) {
            return standardRemoveAll(c);
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean retainAll(Collection<?> c) {
            return standardRetainAll(c);
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/MapConstraints$ConstrainedEntrySet.class */
    static class ConstrainedEntrySet<K, V> extends ConstrainedEntries<K, V> implements Set<Map.Entry<K, V>> {
        ConstrainedEntrySet(Set<Map.Entry<K, V>> entries, MapConstraint<? super K, ? super V> constraint) {
            super(entries, constraint);
        }

        @Override // java.util.Collection, java.util.Set
        public boolean equals(@Nullable Object object) {
            return Sets.equalsImpl(this, object);
        }

        @Override // java.util.Collection, java.util.Set
        public int hashCode() {
            return Sets.hashCodeImpl(this);
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/MapConstraints$ConstrainedAsMapEntries.class */
    static class ConstrainedAsMapEntries<K, V> extends ForwardingSet<Map.Entry<K, Collection<V>>> {
        private final MapConstraint<? super K, ? super V> constraint;
        private final Set<Map.Entry<K, Collection<V>>> entries;

        ConstrainedAsMapEntries(Set<Map.Entry<K, Collection<V>>> entries, MapConstraint<? super K, ? super V> constraint) {
            this.entries = entries;
            this.constraint = constraint;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        public Set<Map.Entry<K, Collection<V>>> delegate() {
            return this.entries;
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<K, Collection<V>>> iterator() {
            final Iterator<Map.Entry<K, Collection<V>>> iterator = this.entries.iterator();
            return new ForwardingIterator<Map.Entry<K, Collection<V>>>() { // from class: com.google.common.collect.MapConstraints.ConstrainedAsMapEntries.1
                @Override // com.google.common.collect.ForwardingIterator, java.util.Iterator
                public Map.Entry<K, Collection<V>> next() {
                    return MapConstraints.constrainedAsMapEntry((Map.Entry) iterator.next(), ConstrainedAsMapEntries.this.constraint);
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // com.google.common.collect.ForwardingIterator, com.google.common.collect.ForwardingObject
                public Iterator<Map.Entry<K, Collection<V>>> delegate() {
                    return iterator;
                }
            };
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public Object[] toArray() {
            return standardToArray();
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public <T> T[] toArray(T[] tArr) {
            return (T[]) standardToArray(tArr);
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            return Maps.containsEntryImpl(delegate(), o);
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean containsAll(Collection<?> c) {
            return standardContainsAll(c);
        }

        @Override // com.google.common.collect.ForwardingSet, java.util.Collection, java.util.Set
        public boolean equals(@Nullable Object object) {
            return standardEquals(object);
        }

        @Override // com.google.common.collect.ForwardingSet, java.util.Collection, java.util.Set
        public int hashCode() {
            return standardHashCode();
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            return Maps.removeEntryImpl(delegate(), o);
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean removeAll(Collection<?> c) {
            return standardRemoveAll(c);
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean retainAll(Collection<?> c) {
            return standardRetainAll(c);
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/MapConstraints$ConstrainedListMultimap.class */
    private static class ConstrainedListMultimap<K, V> extends ConstrainedMultimap<K, V> implements ListMultimap<K, V> {
        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.MapConstraints.ConstrainedMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public /* bridge */ /* synthetic */ Collection replaceValues(Object obj, Iterable x1) {
            return replaceValues((ConstrainedListMultimap<K, V>) obj, x1);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.MapConstraints.ConstrainedMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public /* bridge */ /* synthetic */ Collection get(Object obj) {
            return get((ConstrainedListMultimap<K, V>) obj);
        }

        ConstrainedListMultimap(ListMultimap<K, V> delegate, MapConstraint<? super K, ? super V> constraint) {
            super(delegate, constraint);
        }

        @Override // com.google.common.collect.MapConstraints.ConstrainedMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public List<V> get(K key) {
            return (List) super.get((ConstrainedListMultimap<K, V>) key);
        }

        @Override // com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public List<V> removeAll(Object key) {
            return (List) super.removeAll(key);
        }

        @Override // com.google.common.collect.MapConstraints.ConstrainedMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public List<V> replaceValues(K key, Iterable<? extends V> values) {
            return (List) super.replaceValues((ConstrainedListMultimap<K, V>) key, (Iterable) values);
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/MapConstraints$ConstrainedSetMultimap.class */
    private static class ConstrainedSetMultimap<K, V> extends ConstrainedMultimap<K, V> implements SetMultimap<K, V> {
        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.MapConstraints.ConstrainedMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public /* bridge */ /* synthetic */ Collection replaceValues(Object obj, Iterable x1) {
            return replaceValues((ConstrainedSetMultimap<K, V>) obj, x1);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.MapConstraints.ConstrainedMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public /* bridge */ /* synthetic */ Collection get(Object obj) {
            return get((ConstrainedSetMultimap<K, V>) obj);
        }

        ConstrainedSetMultimap(SetMultimap<K, V> delegate, MapConstraint<? super K, ? super V> constraint) {
            super(delegate, constraint);
        }

        @Override // com.google.common.collect.MapConstraints.ConstrainedMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public Set<V> get(K key) {
            return (Set) super.get((ConstrainedSetMultimap<K, V>) key);
        }

        @Override // com.google.common.collect.MapConstraints.ConstrainedMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap
        public Set<Map.Entry<K, V>> entries() {
            return (Set) super.entries();
        }

        @Override // com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public Set<V> removeAll(Object key) {
            return (Set) super.removeAll(key);
        }

        @Override // com.google.common.collect.MapConstraints.ConstrainedMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public Set<V> replaceValues(K key, Iterable<? extends V> values) {
            return (Set) super.replaceValues((ConstrainedSetMultimap<K, V>) key, (Iterable) values);
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/MapConstraints$ConstrainedSortedSetMultimap.class */
    private static class ConstrainedSortedSetMultimap<K, V> extends ConstrainedSetMultimap<K, V> implements SortedSetMultimap<K, V> {
        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.MapConstraints.ConstrainedSetMultimap, com.google.common.collect.MapConstraints.ConstrainedMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public /* bridge */ /* synthetic */ Set replaceValues(Object obj, Iterable x1) {
            return replaceValues((ConstrainedSortedSetMultimap<K, V>) obj, x1);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.MapConstraints.ConstrainedSetMultimap, com.google.common.collect.MapConstraints.ConstrainedMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public /* bridge */ /* synthetic */ Set get(Object obj) {
            return get((ConstrainedSortedSetMultimap<K, V>) obj);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.MapConstraints.ConstrainedSetMultimap, com.google.common.collect.MapConstraints.ConstrainedMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public /* bridge */ /* synthetic */ Collection get(Object obj) {
            return get((ConstrainedSortedSetMultimap<K, V>) obj);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.MapConstraints.ConstrainedSetMultimap, com.google.common.collect.MapConstraints.ConstrainedMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public /* bridge */ /* synthetic */ Collection replaceValues(Object obj, Iterable x1) {
            return replaceValues((ConstrainedSortedSetMultimap<K, V>) obj, x1);
        }

        ConstrainedSortedSetMultimap(SortedSetMultimap<K, V> delegate, MapConstraint<? super K, ? super V> constraint) {
            super(delegate, constraint);
        }

        @Override // com.google.common.collect.MapConstraints.ConstrainedSetMultimap, com.google.common.collect.MapConstraints.ConstrainedMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public SortedSet<V> get(K key) {
            return (SortedSet) super.get((ConstrainedSortedSetMultimap<K, V>) key);
        }

        @Override // com.google.common.collect.MapConstraints.ConstrainedSetMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public SortedSet<V> removeAll(Object key) {
            return (SortedSet) super.removeAll(key);
        }

        @Override // com.google.common.collect.MapConstraints.ConstrainedSetMultimap, com.google.common.collect.MapConstraints.ConstrainedMultimap, com.google.common.collect.ForwardingMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
        public SortedSet<V> replaceValues(K key, Iterable<? extends V> values) {
            return (SortedSet) super.replaceValues((ConstrainedSortedSetMultimap<K, V>) key, (Iterable) values);
        }

        @Override // com.google.common.collect.SortedSetMultimap
        public Comparator<? super V> valueComparator() {
            return ((SortedSetMultimap) delegate()).valueComparator();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <K, V> Collection<V> checkValues(K k, Iterable<? extends V> iterable, MapConstraint<? super K, ? super V> mapConstraint) {
        ArrayList arrayListNewArrayList = Lists.newArrayList(iterable);
        Iterator it = arrayListNewArrayList.iterator();
        while (it.hasNext()) {
            mapConstraint.checkKeyValue(k, (Object) it.next());
        }
        return arrayListNewArrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <K, V> Map<K, V> checkMap(Map<? extends K, ? extends V> map, MapConstraint<? super K, ? super V> mapConstraint) {
        LinkedHashMap linkedHashMap = new LinkedHashMap(map);
        for (Map.Entry<K, V> entry : linkedHashMap.entrySet()) {
            mapConstraint.checkKeyValue(entry.getKey(), entry.getValue());
        }
        return linkedHashMap;
    }
}
