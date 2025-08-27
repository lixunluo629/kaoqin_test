package org.apache.commons.collections4.set;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.iterators.EmptyIterator;
import org.apache.commons.collections4.iterators.IteratorChain;
import org.apache.commons.collections4.list.UnmodifiableList;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/set/CompositeSet.class */
public class CompositeSet<E> implements Set<E>, Serializable {
    private static final long serialVersionUID = 5185069727540378940L;
    private SetMutator<E> mutator;
    private final List<Set<E>> all = new ArrayList();

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/set/CompositeSet$SetMutator.class */
    public interface SetMutator<E> extends Serializable {
        boolean add(CompositeSet<E> compositeSet, List<Set<E>> list, E e);

        boolean addAll(CompositeSet<E> compositeSet, List<Set<E>> list, Collection<? extends E> collection);

        void resolveCollision(CompositeSet<E> compositeSet, Set<E> set, Set<E> set2, Collection<E> collection);
    }

    public CompositeSet() {
    }

    public CompositeSet(Set<E> set) {
        addComposited(set);
    }

    public CompositeSet(Set<E>... sets) {
        addComposited(sets);
    }

    @Override // java.util.Set, java.util.Collection
    public int size() {
        int size = 0;
        for (Set<E> item : this.all) {
            size += item.size();
        }
        return size;
    }

    @Override // java.util.Set, java.util.Collection
    public boolean isEmpty() {
        for (Set<E> item : this.all) {
            if (!item.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.Set, java.util.Collection
    public boolean contains(Object obj) {
        for (Set<E> item : this.all) {
            if (item.contains(obj)) {
                return true;
            }
        }
        return false;
    }

    @Override // java.util.Set, java.util.Collection, java.lang.Iterable
    public Iterator<E> iterator() {
        if (this.all.isEmpty()) {
            return EmptyIterator.emptyIterator();
        }
        IteratorChain<E> chain = new IteratorChain<>();
        for (Set<E> item : this.all) {
            chain.addIterator(item.iterator());
        }
        return chain;
    }

    @Override // java.util.Set, java.util.Collection
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

    @Override // java.util.Set, java.util.Collection
    public <T> T[] toArray(T[] array) {
        Object[] result;
        int size = size();
        if (array.length >= size) {
            result = array;
        } else {
            result = (Object[]) Array.newInstance(array.getClass().getComponentType(), size);
        }
        int offset = 0;
        for (Set<E> item : this.all) {
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

    @Override // java.util.Set, java.util.Collection
    public boolean add(E obj) {
        if (this.mutator == null) {
            throw new UnsupportedOperationException("add() is not supported on CompositeSet without a SetMutator strategy");
        }
        return this.mutator.add(this, this.all, obj);
    }

    @Override // java.util.Set, java.util.Collection
    public boolean remove(Object obj) {
        for (Set<E> set : getSets()) {
            if (set.contains(obj)) {
                return set.remove(obj);
            }
        }
        return false;
    }

    @Override // java.util.Set, java.util.Collection
    public boolean containsAll(Collection<?> coll) {
        for (Object item : coll) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.Set, java.util.Collection
    public boolean addAll(Collection<? extends E> coll) {
        if (this.mutator == null) {
            throw new UnsupportedOperationException("addAll() is not supported on CompositeSet without a SetMutator strategy");
        }
        return this.mutator.addAll(this, this.all, coll);
    }

    @Override // java.util.Set, java.util.Collection
    public boolean removeAll(Collection<?> coll) {
        if (coll.size() == 0) {
            return false;
        }
        boolean changed = false;
        for (Set<E> item : this.all) {
            changed |= item.removeAll(coll);
        }
        return changed;
    }

    @Override // java.util.Set, java.util.Collection
    public boolean retainAll(Collection<?> coll) {
        boolean changed = false;
        for (Set<E> item : this.all) {
            changed |= item.retainAll(coll);
        }
        return changed;
    }

    @Override // java.util.Set, java.util.Collection
    public void clear() {
        for (Set<E> coll : this.all) {
            coll.clear();
        }
    }

    public void setMutator(SetMutator<E> mutator) {
        this.mutator = mutator;
    }

    public synchronized void addComposited(Set<E> set) {
        for (Set<E> existingSet : getSets()) {
            Collection<E> intersects = CollectionUtils.intersection(existingSet, set);
            if (intersects.size() > 0) {
                if (this.mutator == null) {
                    throw new UnsupportedOperationException("Collision adding composited set with no SetMutator set");
                }
                getMutator().resolveCollision(this, existingSet, set, intersects);
                if (CollectionUtils.intersection(existingSet, set).size() > 0) {
                    throw new IllegalArgumentException("Attempt to add illegal entry unresolved by SetMutator.resolveCollision()");
                }
            }
        }
        this.all.add(set);
    }

    public void addComposited(Set<E> set1, Set<E> set2) {
        addComposited(set1);
        addComposited(set2);
    }

    public void addComposited(Set<E>... sets) {
        for (Set<E> set : sets) {
            addComposited(set);
        }
    }

    public void removeComposited(Set<E> set) {
        this.all.remove(set);
    }

    public Set<E> toSet() {
        return new HashSet(this);
    }

    public List<Set<E>> getSets() {
        return UnmodifiableList.unmodifiableList(this.all);
    }

    protected SetMutator<E> getMutator() {
        return this.mutator;
    }

    @Override // java.util.Set, java.util.Collection
    public boolean equals(Object obj) {
        if (obj instanceof Set) {
            Set<?> set = (Set) obj;
            return set.size() == size() && set.containsAll(this);
        }
        return false;
    }

    @Override // java.util.Set, java.util.Collection
    public int hashCode() {
        int code = 0;
        Iterator i$ = iterator();
        while (i$.hasNext()) {
            E e = i$.next();
            code += e == null ? 0 : e.hashCode();
        }
        return code;
    }
}
