package org.apache.commons.collections4.collection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.map.MultiValueMap;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/collection/IndexedCollection.class */
public class IndexedCollection<K, C> extends AbstractCollectionDecorator<C> {
    private static final long serialVersionUID = -5512610452568370038L;
    private final Transformer<C, K> keyTransformer;
    private final MultiMap<K, C> index;
    private final boolean uniqueIndex;

    public static <K, C> IndexedCollection<K, C> uniqueIndexedCollection(Collection<C> coll, Transformer<C, K> keyTransformer) {
        return new IndexedCollection<>(coll, keyTransformer, MultiValueMap.multiValueMap(new HashMap()), true);
    }

    public static <K, C> IndexedCollection<K, C> nonUniqueIndexedCollection(Collection<C> coll, Transformer<C, K> keyTransformer) {
        return new IndexedCollection<>(coll, keyTransformer, MultiValueMap.multiValueMap(new HashMap()), false);
    }

    public IndexedCollection(Collection<C> coll, Transformer<C, K> keyTransformer, MultiMap<K, C> map, boolean uniqueIndex) {
        super(coll);
        this.keyTransformer = keyTransformer;
        this.index = map;
        this.uniqueIndex = uniqueIndex;
        reindex();
    }

    @Override // org.apache.commons.collections4.collection.AbstractCollectionDecorator, java.util.Collection, org.apache.commons.collections4.Bag
    public boolean add(C object) {
        boolean added = super.add(object);
        if (added) {
            addToIndex(object);
        }
        return added;
    }

    @Override // org.apache.commons.collections4.collection.AbstractCollectionDecorator, java.util.Collection
    public boolean addAll(Collection<? extends C> coll) {
        boolean changed = false;
        for (C c : coll) {
            changed |= add(c);
        }
        return changed;
    }

    @Override // org.apache.commons.collections4.collection.AbstractCollectionDecorator, java.util.Collection
    public void clear() {
        super.clear();
        this.index.clear();
    }

    @Override // org.apache.commons.collections4.collection.AbstractCollectionDecorator, java.util.Collection, java.util.Set
    public boolean contains(Object object) {
        return this.index.containsKey(this.keyTransformer.transform(object));
    }

    @Override // org.apache.commons.collections4.collection.AbstractCollectionDecorator, java.util.Collection, org.apache.commons.collections4.Bag
    public boolean containsAll(Collection<?> coll) {
        for (Object o : coll) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    public C get(K key) {
        Collection<C> coll = (Collection) this.index.get(key);
        if (coll == null) {
            return null;
        }
        return coll.iterator().next();
    }

    public Collection<C> values(K key) {
        return (Collection) this.index.get(key);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void reindex() {
        this.index.clear();
        Iterator i$ = decorated().iterator();
        while (i$.hasNext()) {
            addToIndex(i$.next());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.collections4.collection.AbstractCollectionDecorator, java.util.Collection, org.apache.commons.collections4.Bag
    public boolean remove(Object obj) {
        boolean removed = super.remove(obj);
        if (removed) {
            removeFromIndex(obj);
        }
        return removed;
    }

    @Override // org.apache.commons.collections4.collection.AbstractCollectionDecorator, java.util.Collection, org.apache.commons.collections4.Bag
    public boolean removeAll(Collection<?> coll) {
        boolean changed = false;
        for (Object o : coll) {
            changed |= remove(o);
        }
        return changed;
    }

    @Override // org.apache.commons.collections4.collection.AbstractCollectionDecorator, java.util.Collection, org.apache.commons.collections4.Bag
    public boolean retainAll(Collection<?> coll) {
        boolean changed = super.retainAll(coll);
        if (changed) {
            reindex();
        }
        return changed;
    }

    private void addToIndex(C object) {
        K key = this.keyTransformer.transform(object);
        if (this.uniqueIndex && this.index.containsKey(key)) {
            throw new IllegalArgumentException("Duplicate key in uniquely indexed collection.");
        }
        this.index.put(key, object);
    }

    private void removeFromIndex(C object) {
        this.index.remove(this.keyTransformer.transform(object));
    }
}
