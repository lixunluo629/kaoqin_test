package org.apache.commons.collections4.multimap;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.iterators.AbstractIteratorDecorator;
import org.apache.commons.collections4.iterators.EmptyMapIterator;
import org.apache.commons.collections4.iterators.IteratorChain;
import org.apache.commons.collections4.iterators.LazyIteratorChain;
import org.apache.commons.collections4.iterators.TransformIterator;
import org.apache.commons.collections4.keyvalue.AbstractMapEntry;
import org.apache.commons.collections4.keyvalue.UnmodifiableMapEntry;
import org.apache.commons.collections4.multiset.AbstractMultiSet;
import org.apache.commons.collections4.multiset.UnmodifiableMultiSet;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/multimap/AbstractMultiValuedMap.class */
public abstract class AbstractMultiValuedMap<K, V> implements MultiValuedMap<K, V> {
    private transient Collection<V> valuesView;
    private transient AbstractMultiValuedMap<K, V>.EntryValues entryValuesView;
    private transient MultiSet<K> keysMultiSetView;
    private transient AbstractMultiValuedMap<K, V>.AsMap asMapView;
    private transient Map<K, Collection<V>> map;

    protected abstract Collection<V> createCollection();

    protected AbstractMultiValuedMap() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected AbstractMultiValuedMap(Map<K, ? extends Collection<V>> map) {
        if (map == 0) {
            throw new NullPointerException("Map must not be null.");
        }
        this.map = map;
    }

    protected Map<K, ? extends Collection<V>> getMap() {
        return this.map;
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void setMap(Map<K, ? extends Collection<V>> map) {
        this.map = map;
    }

    @Override // org.apache.commons.collections4.MultiValuedMap
    public boolean containsKey(Object key) {
        return getMap().containsKey(key);
    }

    @Override // org.apache.commons.collections4.MultiValuedMap
    public boolean containsValue(Object value) {
        return values().contains(value);
    }

    @Override // org.apache.commons.collections4.MultiValuedMap
    public boolean containsMapping(Object key, Object value) {
        Collection<V> coll = getMap().get(key);
        return coll != null && coll.contains(value);
    }

    @Override // org.apache.commons.collections4.MultiValuedMap
    public Collection<Map.Entry<K, V>> entries() {
        if (this.entryValuesView != null) {
            return this.entryValuesView;
        }
        AbstractMultiValuedMap<K, V>.EntryValues entryValues = new EntryValues();
        this.entryValuesView = entryValues;
        return entryValues;
    }

    @Override // org.apache.commons.collections4.MultiValuedMap
    public Collection<V> get(K key) {
        return wrappedCollection(key);
    }

    Collection<V> wrappedCollection(K key) {
        return new WrappedCollection(key);
    }

    @Override // org.apache.commons.collections4.MultiValuedMap
    public Collection<V> remove(Object key) {
        return CollectionUtils.emptyIfNull(getMap().remove(key));
    }

    @Override // org.apache.commons.collections4.MultiValuedMap
    public boolean removeMapping(Object key, Object value) {
        Collection<V> coll = getMap().get(key);
        if (coll == null) {
            return false;
        }
        boolean changed = coll.remove(value);
        if (coll.isEmpty()) {
            getMap().remove(key);
        }
        return changed;
    }

    @Override // org.apache.commons.collections4.MultiValuedMap
    public boolean isEmpty() {
        return getMap().isEmpty();
    }

    @Override // org.apache.commons.collections4.MultiValuedMap
    public Set<K> keySet() {
        return getMap().keySet();
    }

    @Override // org.apache.commons.collections4.MultiValuedMap
    public int size() {
        int size = 0;
        for (Collection<V> col : getMap().values()) {
            size += col.size();
        }
        return size;
    }

    @Override // org.apache.commons.collections4.MultiValuedMap
    public Collection<V> values() {
        Collection<V> vs = this.valuesView;
        if (vs != null) {
            return vs;
        }
        Values values = new Values();
        this.valuesView = values;
        return values;
    }

    @Override // org.apache.commons.collections4.MultiValuedMap
    public void clear() {
        getMap().clear();
    }

    @Override // org.apache.commons.collections4.MultiValuedMap
    public boolean put(K key, V value) {
        Collection<V> coll = getMap().get(key);
        if (coll == null) {
            Collection<V> coll2 = createCollection();
            if (coll2.add(value)) {
                this.map.put(key, coll2);
                return true;
            }
            return false;
        }
        return coll.add(value);
    }

    @Override // org.apache.commons.collections4.MultiValuedMap
    public boolean putAll(Map<? extends K, ? extends V> map) {
        if (map == null) {
            throw new NullPointerException("Map must not be null.");
        }
        boolean changed = false;
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            changed |= put(entry.getKey(), entry.getValue());
        }
        return changed;
    }

    @Override // org.apache.commons.collections4.MultiValuedMap
    public boolean putAll(MultiValuedMap<? extends K, ? extends V> map) {
        if (map == null) {
            throw new NullPointerException("Map must not be null.");
        }
        boolean changed = false;
        for (Map.Entry<? extends K, ? extends V> entry : map.entries()) {
            changed |= put(entry.getKey(), entry.getValue());
        }
        return changed;
    }

    @Override // org.apache.commons.collections4.MultiValuedMap
    public MultiSet<K> keys() {
        if (this.keysMultiSetView == null) {
            this.keysMultiSetView = UnmodifiableMultiSet.unmodifiableMultiSet(new KeysMultiSet());
        }
        return this.keysMultiSetView;
    }

    @Override // org.apache.commons.collections4.MultiValuedMap
    public Map<K, Collection<V>> asMap() {
        if (this.asMapView != null) {
            return this.asMapView;
        }
        AbstractMultiValuedMap<K, V>.AsMap asMap = new AsMap(this.map);
        this.asMapView = asMap;
        return asMap;
    }

    @Override // org.apache.commons.collections4.MultiValuedMap
    public boolean putAll(K key, Iterable<? extends V> values) {
        if (values == null) {
            throw new NullPointerException("Values must not be null.");
        }
        if (values instanceof Collection) {
            Collection<? extends V> valueCollection = (Collection) values;
            return !valueCollection.isEmpty() && get(key).addAll(valueCollection);
        }
        Iterator<? extends V> it = values.iterator();
        return it.hasNext() && CollectionUtils.addAll(get(key), it);
    }

    @Override // org.apache.commons.collections4.MultiValuedMap
    public MapIterator<K, V> mapIterator() {
        if (size() == 0) {
            return EmptyMapIterator.emptyMapIterator();
        }
        return new MultiValuedMapIterator();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof MultiValuedMap) {
            return asMap().equals(((MultiValuedMap) obj).asMap());
        }
        return false;
    }

    public int hashCode() {
        return getMap().hashCode();
    }

    public String toString() {
        return getMap().toString();
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/multimap/AbstractMultiValuedMap$WrappedCollection.class */
    class WrappedCollection implements Collection<V> {
        protected final K key;

        public WrappedCollection(K key) {
            this.key = key;
        }

        protected Collection<V> getMapping() {
            return AbstractMultiValuedMap.this.getMap().get(this.key);
        }

        @Override // java.util.Collection
        public boolean add(V value) {
            Collection<V> coll = getMapping();
            if (coll == null) {
                coll = AbstractMultiValuedMap.this.createCollection();
                AbstractMultiValuedMap.this.map.put(this.key, coll);
            }
            return coll.add(value);
        }

        @Override // java.util.Collection
        public boolean addAll(Collection<? extends V> other) {
            Collection<V> coll = getMapping();
            if (coll == null) {
                coll = AbstractMultiValuedMap.this.createCollection();
                AbstractMultiValuedMap.this.map.put(this.key, coll);
            }
            return coll.addAll(other);
        }

        @Override // java.util.Collection
        public void clear() {
            Collection<V> coll = getMapping();
            if (coll != null) {
                coll.clear();
                AbstractMultiValuedMap.this.remove(this.key);
            }
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Iterator<V> iterator() {
            Collection<V> coll = getMapping();
            if (coll == null) {
                return IteratorUtils.EMPTY_ITERATOR;
            }
            return new ValuesIterator(this.key);
        }

        @Override // java.util.Collection
        public int size() {
            Collection<V> coll = getMapping();
            if (coll == null) {
                return 0;
            }
            return coll.size();
        }

        @Override // java.util.Collection
        public boolean contains(Object obj) {
            Collection<V> coll = getMapping();
            if (coll == null) {
                return false;
            }
            return coll.contains(obj);
        }

        @Override // java.util.Collection
        public boolean containsAll(Collection<?> other) {
            Collection<V> coll = getMapping();
            if (coll == null) {
                return false;
            }
            return coll.containsAll(other);
        }

        @Override // java.util.Collection
        public boolean isEmpty() {
            Collection<V> coll = getMapping();
            if (coll == null) {
                return true;
            }
            return coll.isEmpty();
        }

        @Override // java.util.Collection
        public boolean remove(Object item) {
            Collection<V> coll = getMapping();
            if (coll == null) {
                return false;
            }
            boolean result = coll.remove(item);
            if (coll.isEmpty()) {
                AbstractMultiValuedMap.this.remove(this.key);
            }
            return result;
        }

        @Override // java.util.Collection
        public boolean removeAll(Collection<?> c) {
            Collection<V> coll = getMapping();
            if (coll == null) {
                return false;
            }
            boolean result = coll.removeAll(c);
            if (coll.isEmpty()) {
                AbstractMultiValuedMap.this.remove(this.key);
            }
            return result;
        }

        @Override // java.util.Collection
        public boolean retainAll(Collection<?> c) {
            Collection<V> coll = getMapping();
            if (coll == null) {
                return false;
            }
            boolean result = coll.retainAll(c);
            if (coll.isEmpty()) {
                AbstractMultiValuedMap.this.remove(this.key);
            }
            return result;
        }

        @Override // java.util.Collection
        public Object[] toArray() {
            Collection<V> coll = getMapping();
            if (coll == null) {
                return CollectionUtils.EMPTY_COLLECTION.toArray();
            }
            return coll.toArray();
        }

        @Override // java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            Collection<V> mapping = getMapping();
            if (mapping == null) {
                return (T[]) CollectionUtils.EMPTY_COLLECTION.toArray(tArr);
            }
            return (T[]) mapping.toArray(tArr);
        }

        public String toString() {
            Collection<V> coll = getMapping();
            if (coll == null) {
                return CollectionUtils.EMPTY_COLLECTION.toString();
            }
            return coll.toString();
        }
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/multimap/AbstractMultiValuedMap$KeysMultiSet.class */
    private class KeysMultiSet extends AbstractMultiSet<K> {
        private KeysMultiSet() {
        }

        @Override // org.apache.commons.collections4.multiset.AbstractMultiSet, java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object o) {
            return AbstractMultiValuedMap.this.getMap().containsKey(o);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return AbstractMultiValuedMap.this.getMap().isEmpty();
        }

        @Override // org.apache.commons.collections4.multiset.AbstractMultiSet, java.util.AbstractCollection, java.util.Collection, org.apache.commons.collections4.MultiSet
        public int size() {
            return AbstractMultiValuedMap.this.size();
        }

        @Override // org.apache.commons.collections4.multiset.AbstractMultiSet
        protected int uniqueElements() {
            return AbstractMultiValuedMap.this.getMap().size();
        }

        @Override // org.apache.commons.collections4.multiset.AbstractMultiSet, org.apache.commons.collections4.MultiSet
        public int getCount(Object object) {
            int count = 0;
            Collection<V> col = AbstractMultiValuedMap.this.getMap().get(object);
            if (col != null) {
                count = col.size();
            }
            return count;
        }

        @Override // org.apache.commons.collections4.multiset.AbstractMultiSet
        protected Iterator<MultiSet.Entry<K>> createEntrySetIterator() {
            AbstractMultiValuedMap<K, V>.KeysMultiSet.MapEntryTransformer transformer = new MapEntryTransformer();
            return IteratorUtils.transformedIterator(AbstractMultiValuedMap.this.map.entrySet().iterator(), transformer);
        }

        /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/multimap/AbstractMultiValuedMap$KeysMultiSet$MapEntryTransformer.class */
        private final class MapEntryTransformer implements Transformer<Map.Entry<K, Collection<V>>, MultiSet.Entry<K>> {
            private MapEntryTransformer() {
            }

            @Override // org.apache.commons.collections4.Transformer
            public MultiSet.Entry<K> transform(final Map.Entry<K, Collection<V>> mapEntry) {
                return new AbstractMultiSet.AbstractEntry<K>() { // from class: org.apache.commons.collections4.multimap.AbstractMultiValuedMap.KeysMultiSet.MapEntryTransformer.1
                    @Override // org.apache.commons.collections4.MultiSet.Entry
                    public K getElement() {
                        return (K) mapEntry.getKey();
                    }

                    @Override // org.apache.commons.collections4.MultiSet.Entry
                    public int getCount() {
                        return ((Collection) mapEntry.getValue()).size();
                    }
                };
            }
        }
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/multimap/AbstractMultiValuedMap$EntryValues.class */
    private class EntryValues extends AbstractCollection<Map.Entry<K, V>> {
        private EntryValues() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<Map.Entry<K, V>> iterator() {
            return new LazyIteratorChain<Map.Entry<K, V>>() { // from class: org.apache.commons.collections4.multimap.AbstractMultiValuedMap.EntryValues.1
                final Collection<K> keysCol;
                final Iterator<K> keyIterator;

                {
                    this.keysCol = new ArrayList(AbstractMultiValuedMap.this.getMap().keySet());
                    this.keyIterator = this.keysCol.iterator();
                }

                @Override // org.apache.commons.collections4.iterators.LazyIteratorChain
                protected Iterator<? extends Map.Entry<K, V>> nextIterator(int count) {
                    if (!this.keyIterator.hasNext()) {
                        return null;
                    }
                    final K key = this.keyIterator.next();
                    Transformer<V, Map.Entry<K, V>> entryTransformer = new Transformer<V, Map.Entry<K, V>>() { // from class: org.apache.commons.collections4.multimap.AbstractMultiValuedMap.EntryValues.1.1
                        @Override // org.apache.commons.collections4.Transformer
                        public /* bridge */ /* synthetic */ Object transform(Object x0) {
                            return transform((C00411) x0);
                        }

                        @Override // org.apache.commons.collections4.Transformer
                        public Map.Entry<K, V> transform(V input) {
                            return new MultiValuedMapEntry(key, input);
                        }
                    };
                    return new TransformIterator(new ValuesIterator(key), entryTransformer);
                }
            };
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return AbstractMultiValuedMap.this.size();
        }
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/multimap/AbstractMultiValuedMap$MultiValuedMapEntry.class */
    private class MultiValuedMapEntry extends AbstractMapEntry<K, V> {
        public MultiValuedMapEntry(K key, V value) {
            super(key, value);
        }

        @Override // org.apache.commons.collections4.keyvalue.AbstractMapEntry, org.apache.commons.collections4.keyvalue.AbstractKeyValue, java.util.Map.Entry
        public V setValue(V value) {
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/multimap/AbstractMultiValuedMap$MultiValuedMapIterator.class */
    private class MultiValuedMapIterator implements MapIterator<K, V> {
        private final Iterator<Map.Entry<K, V>> it;
        private Map.Entry<K, V> current = null;

        public MultiValuedMapIterator() {
            this.it = AbstractMultiValuedMap.this.entries().iterator();
        }

        @Override // org.apache.commons.collections4.MapIterator, java.util.Iterator
        public boolean hasNext() {
            return this.it.hasNext();
        }

        @Override // org.apache.commons.collections4.MapIterator, java.util.Iterator
        public K next() {
            this.current = this.it.next();
            return this.current.getKey();
        }

        @Override // org.apache.commons.collections4.MapIterator
        public K getKey() {
            if (this.current == null) {
                throw new IllegalStateException();
            }
            return this.current.getKey();
        }

        @Override // org.apache.commons.collections4.MapIterator
        public V getValue() {
            if (this.current == null) {
                throw new IllegalStateException();
            }
            return this.current.getValue();
        }

        @Override // org.apache.commons.collections4.MapIterator, java.util.Iterator
        public void remove() {
            this.it.remove();
        }

        @Override // org.apache.commons.collections4.MapIterator
        public V setValue(V value) {
            if (this.current == null) {
                throw new IllegalStateException();
            }
            return this.current.setValue(value);
        }
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/multimap/AbstractMultiValuedMap$Values.class */
    private class Values extends AbstractCollection<V> {
        private Values() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<V> iterator() {
            IteratorChain<V> chain = new IteratorChain<>();
            for (K k : AbstractMultiValuedMap.this.keySet()) {
                chain.addIterator(new ValuesIterator(k));
            }
            return chain;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return AbstractMultiValuedMap.this.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            AbstractMultiValuedMap.this.clear();
        }
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/multimap/AbstractMultiValuedMap$ValuesIterator.class */
    private class ValuesIterator implements Iterator<V> {
        private final Object key;
        private final Collection<V> values;
        private final Iterator<V> iterator;

        public ValuesIterator(Object key) {
            this.key = key;
            this.values = AbstractMultiValuedMap.this.getMap().get(key);
            this.iterator = this.values.iterator();
        }

        @Override // java.util.Iterator
        public void remove() {
            this.iterator.remove();
            if (this.values.isEmpty()) {
                AbstractMultiValuedMap.this.remove(this.key);
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        @Override // java.util.Iterator
        public V next() {
            return this.iterator.next();
        }
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/multimap/AbstractMultiValuedMap$AsMap.class */
    private class AsMap extends AbstractMap<K, Collection<V>> {
        final transient Map<K, Collection<V>> decoratedMap;

        AsMap(Map<K, Collection<V>> map) {
            this.decoratedMap = map;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<Map.Entry<K, Collection<V>>> entrySet() {
            return new AsMapEntrySet();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(Object key) {
            return this.decoratedMap.containsKey(key);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Collection<V> get(Object key) {
            Collection<V> collection = this.decoratedMap.get(key);
            if (collection == null) {
                return null;
            }
            return AbstractMultiValuedMap.this.wrappedCollection(key);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<K> keySet() {
            return AbstractMultiValuedMap.this.keySet();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public int size() {
            return this.decoratedMap.size();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Collection<V> remove(Object key) {
            Collection<V> collection = this.decoratedMap.remove(key);
            if (collection == null) {
                return null;
            }
            Collection<V> output = AbstractMultiValuedMap.this.createCollection();
            output.addAll(collection);
            collection.clear();
            return output;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean equals(Object object) {
            return this == object || this.decoratedMap.equals(object);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public int hashCode() {
            return this.decoratedMap.hashCode();
        }

        @Override // java.util.AbstractMap
        public String toString() {
            return this.decoratedMap.toString();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public void clear() {
            AbstractMultiValuedMap.this.clear();
        }

        /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/multimap/AbstractMultiValuedMap$AsMap$AsMapEntrySet.class */
        class AsMapEntrySet extends AbstractSet<Map.Entry<K, Collection<V>>> {
            AsMapEntrySet() {
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public Iterator<Map.Entry<K, Collection<V>>> iterator() {
                return AsMap.this.new AsMapEntrySetIterator(AsMap.this.decoratedMap.entrySet().iterator());
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return AsMap.this.size();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public void clear() {
                AsMap.this.clear();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean contains(Object o) {
                return AsMap.this.decoratedMap.entrySet().contains(o);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean remove(Object o) {
                if (!contains(o)) {
                    return false;
                }
                Map.Entry<?, ?> entry = (Map.Entry) o;
                AbstractMultiValuedMap.this.remove(entry.getKey());
                return true;
            }
        }

        /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/multimap/AbstractMultiValuedMap$AsMap$AsMapEntrySetIterator.class */
        class AsMapEntrySetIterator extends AbstractIteratorDecorator<Map.Entry<K, Collection<V>>> {
            AsMapEntrySetIterator(Iterator<Map.Entry<K, Collection<V>>> iterator) {
                super(iterator);
            }

            @Override // org.apache.commons.collections4.iterators.AbstractIteratorDecorator, java.util.Iterator
            public Map.Entry<K, Collection<V>> next() {
                Map.Entry<K, Collection<V>> entry = (Map.Entry) super.next();
                K key = entry.getKey();
                return new UnmodifiableMapEntry(key, AbstractMultiValuedMap.this.wrappedCollection(key));
            }
        }
    }

    protected void doWriteObject(ObjectOutputStream out) throws IOException {
        out.writeInt(this.map.size());
        for (Map.Entry<K, Collection<V>> entry : this.map.entrySet()) {
            out.writeObject(entry.getKey());
            out.writeInt(entry.getValue().size());
            for (V value : entry.getValue()) {
                out.writeObject(value);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void doReadObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        int entrySize = in.readInt();
        for (int i = 0; i < entrySize; i++) {
            Collection collection = get(in.readObject());
            int valueSize = in.readInt();
            for (int j = 0; j < valueSize; j++) {
                collection.add(in.readObject());
            }
        }
    }
}
