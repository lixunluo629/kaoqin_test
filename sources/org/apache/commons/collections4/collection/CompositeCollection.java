package org.apache.commons.collections4.collection;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections4.iterators.EmptyIterator;
import org.apache.commons.collections4.iterators.IteratorChain;
import org.apache.commons.collections4.list.UnmodifiableList;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/collection/CompositeCollection.class */
public class CompositeCollection<E> implements Collection<E>, Serializable {
    private static final long serialVersionUID = 8417515734108306801L;
    private CollectionMutator<E> mutator;
    private final List<Collection<E>> all = new ArrayList();

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/collection/CompositeCollection$CollectionMutator.class */
    public interface CollectionMutator<E> extends Serializable {
        boolean add(CompositeCollection<E> compositeCollection, List<Collection<E>> list, E e);

        boolean addAll(CompositeCollection<E> compositeCollection, List<Collection<E>> list, Collection<? extends E> collection);

        boolean remove(CompositeCollection<E> compositeCollection, List<Collection<E>> list, Object obj);
    }

    public CompositeCollection() {
    }

    public CompositeCollection(Collection<E> compositeCollection) {
        addComposited(compositeCollection);
    }

    public CompositeCollection(Collection<E> compositeCollection1, Collection<E> compositeCollection2) {
        addComposited(compositeCollection1, compositeCollection2);
    }

    public CompositeCollection(Collection<E>... compositeCollections) {
        addComposited(compositeCollections);
    }

    @Override // java.util.Collection
    public int size() {
        int size = 0;
        for (Collection<E> item : this.all) {
            size += item.size();
        }
        return size;
    }

    @Override // java.util.Collection
    public boolean isEmpty() {
        for (Collection<E> item : this.all) {
            if (!item.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.Collection
    public boolean contains(Object obj) {
        for (Collection<E> item : this.all) {
            if (item.contains(obj)) {
                return true;
            }
        }
        return false;
    }

    @Override // java.util.Collection, java.lang.Iterable
    public Iterator<E> iterator() {
        if (this.all.isEmpty()) {
            return EmptyIterator.emptyIterator();
        }
        IteratorChain<E> chain = new IteratorChain<>();
        for (Collection<E> item : this.all) {
            chain.addIterator(item.iterator());
        }
        return chain;
    }

    @Override // java.util.Collection
    public Object[] toArray() {
        Object[] result = new Object[size()];
        int i = 0;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            result[i] = it.next();
            i++;
        }
        return result;
    }

    @Override // java.util.Collection
    public <T> T[] toArray(T[] array) {
        Object[] result;
        int size = size();
        if (array.length >= size) {
            result = array;
        } else {
            result = (Object[]) Array.newInstance(array.getClass().getComponentType(), size);
        }
        int offset = 0;
        for (Collection<E> item : this.all) {
            Iterator i$ = item.iterator();
            while (i$.hasNext()) {
                int i = offset;
                offset++;
                result[i] = i$.next();
            }
        }
        if (result.length > size) {
            result[size] = null;
        }
        return result;
    }

    @Override // java.util.Collection
    public boolean add(E obj) {
        if (this.mutator == null) {
            throw new UnsupportedOperationException("add() is not supported on CompositeCollection without a CollectionMutator strategy");
        }
        return this.mutator.add(this, this.all, obj);
    }

    @Override // java.util.Collection
    public boolean remove(Object obj) {
        if (this.mutator == null) {
            throw new UnsupportedOperationException("remove() is not supported on CompositeCollection without a CollectionMutator strategy");
        }
        return this.mutator.remove(this, this.all, obj);
    }

    @Override // java.util.Collection
    public boolean containsAll(Collection<?> coll) {
        for (Object item : coll) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.Collection
    public boolean addAll(Collection<? extends E> coll) {
        if (this.mutator == null) {
            throw new UnsupportedOperationException("addAll() is not supported on CompositeCollection without a CollectionMutator strategy");
        }
        return this.mutator.addAll(this, this.all, coll);
    }

    @Override // java.util.Collection
    public boolean removeAll(Collection<?> coll) {
        if (coll.size() == 0) {
            return false;
        }
        boolean changed = false;
        for (Collection<E> item : this.all) {
            changed |= item.removeAll(coll);
        }
        return changed;
    }

    @Override // java.util.Collection
    public boolean retainAll(Collection<?> coll) {
        boolean changed = false;
        for (Collection<E> item : this.all) {
            changed |= item.retainAll(coll);
        }
        return changed;
    }

    @Override // java.util.Collection
    public void clear() {
        for (Collection<E> coll : this.all) {
            coll.clear();
        }
    }

    public void setMutator(CollectionMutator<E> mutator) {
        this.mutator = mutator;
    }

    public void addComposited(Collection<E> compositeCollection) {
        this.all.add(compositeCollection);
    }

    public void addComposited(Collection<E> compositeCollection1, Collection<E> compositeCollection2) {
        this.all.add(compositeCollection1);
        this.all.add(compositeCollection2);
    }

    public void addComposited(Collection<E>... compositeCollections) {
        this.all.addAll(Arrays.asList(compositeCollections));
    }

    public void removeComposited(Collection<E> coll) {
        this.all.remove(coll);
    }

    public Collection<E> toCollection() {
        return new ArrayList(this);
    }

    public List<Collection<E>> getCollections() {
        return UnmodifiableList.unmodifiableList(this.all);
    }

    protected CollectionMutator<E> getMutator() {
        return this.mutator;
    }
}
