package org.apache.commons.collections4.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.iterators.AbstractIteratorDecorator;
import org.apache.commons.collections4.iterators.AbstractListIteratorDecorator;
import org.apache.commons.collections4.set.UnmodifiableSet;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/list/SetUniqueList.class */
public class SetUniqueList<E> extends AbstractSerializableListDecorator<E> {
    private static final long serialVersionUID = 7196982186153478694L;
    private final Set<E> set;

    public static <E> SetUniqueList<E> setUniqueList(List<E> list) {
        if (list == null) {
            throw new NullPointerException("List must not be null");
        }
        if (list.isEmpty()) {
            return new SetUniqueList<>(list, new HashSet());
        }
        List<E> temp = new ArrayList<>(list);
        list.clear();
        SetUniqueList<E> sl = new SetUniqueList<>(list, new HashSet());
        sl.addAll(temp);
        return sl;
    }

    protected SetUniqueList(List<E> list, Set<E> set) {
        super(list);
        if (set == null) {
            throw new NullPointerException("Set must not be null");
        }
        this.set = set;
    }

    public Set<E> asSet() {
        return UnmodifiableSet.unmodifiableSet(this.set);
    }

    @Override // org.apache.commons.collections4.collection.AbstractCollectionDecorator, java.util.Collection, org.apache.commons.collections4.Bag
    public boolean add(E object) {
        int sizeBefore = size();
        add(size(), object);
        return sizeBefore != size();
    }

    @Override // org.apache.commons.collections4.list.AbstractListDecorator, java.util.List
    public void add(int index, E object) {
        if (!this.set.contains(object)) {
            super.add(index, object);
            this.set.add(object);
        }
    }

    @Override // org.apache.commons.collections4.collection.AbstractCollectionDecorator, java.util.Collection
    public boolean addAll(Collection<? extends E> coll) {
        return addAll(size(), coll);
    }

    @Override // org.apache.commons.collections4.list.AbstractListDecorator, java.util.List
    public boolean addAll(int index, Collection<? extends E> coll) {
        List<E> temp = new ArrayList<>();
        for (E e : coll) {
            if (this.set.add(e)) {
                temp.add(e);
            }
        }
        return super.addAll(index, temp);
    }

    @Override // org.apache.commons.collections4.list.AbstractListDecorator, java.util.List
    public E set(int i, E e) {
        int iIndexOf = indexOf(e);
        E e2 = (E) super.set(i, e);
        if (iIndexOf != -1 && iIndexOf != i) {
            super.remove(iIndexOf);
        }
        this.set.remove(e2);
        this.set.add(e);
        return e2;
    }

    @Override // org.apache.commons.collections4.collection.AbstractCollectionDecorator, java.util.Collection, org.apache.commons.collections4.Bag
    public boolean remove(Object object) {
        boolean result = this.set.remove(object);
        if (result) {
            super.remove(object);
        }
        return result;
    }

    @Override // org.apache.commons.collections4.list.AbstractListDecorator, java.util.List
    public E remove(int i) {
        E e = (E) super.remove(i);
        this.set.remove(e);
        return e;
    }

    @Override // org.apache.commons.collections4.collection.AbstractCollectionDecorator, java.util.Collection, org.apache.commons.collections4.Bag
    public boolean removeAll(Collection<?> coll) {
        boolean result = false;
        for (Object name : coll) {
            result |= remove(name);
        }
        return result;
    }

    @Override // org.apache.commons.collections4.collection.AbstractCollectionDecorator, java.util.Collection, org.apache.commons.collections4.Bag
    public boolean retainAll(Collection<?> coll) {
        boolean result = this.set.retainAll(coll);
        if (!result) {
            return false;
        }
        if (this.set.size() == 0) {
            super.clear();
        } else {
            super.retainAll(this.set);
        }
        return result;
    }

    @Override // org.apache.commons.collections4.collection.AbstractCollectionDecorator, java.util.Collection
    public void clear() {
        super.clear();
        this.set.clear();
    }

    @Override // org.apache.commons.collections4.collection.AbstractCollectionDecorator, java.util.Collection, java.util.Set
    public boolean contains(Object object) {
        return this.set.contains(object);
    }

    @Override // org.apache.commons.collections4.collection.AbstractCollectionDecorator, java.util.Collection, org.apache.commons.collections4.Bag
    public boolean containsAll(Collection<?> coll) {
        return this.set.containsAll(coll);
    }

    @Override // org.apache.commons.collections4.collection.AbstractCollectionDecorator, java.util.Collection, java.lang.Iterable, org.apache.commons.collections4.Bag
    public Iterator<E> iterator() {
        return new SetListIterator(super.iterator(), this.set);
    }

    @Override // org.apache.commons.collections4.list.AbstractListDecorator, java.util.List
    public ListIterator<E> listIterator() {
        return new SetListListIterator(super.listIterator(), this.set);
    }

    @Override // org.apache.commons.collections4.list.AbstractListDecorator, java.util.List
    public ListIterator<E> listIterator(int index) {
        return new SetListListIterator(super.listIterator(index), this.set);
    }

    @Override // org.apache.commons.collections4.list.AbstractListDecorator, java.util.List
    public List<E> subList(int fromIndex, int toIndex) {
        List<E> superSubList = super.subList(fromIndex, toIndex);
        Set<E> subSet = createSetBasedOnList(this.set, superSubList);
        return ListUtils.unmodifiableList(new SetUniqueList(superSubList, subSet));
    }

    protected Set<E> createSetBasedOnList(Set<E> set, List<E> list) {
        Set<E> subSet;
        if (set.getClass().equals(HashSet.class)) {
            subSet = new HashSet<>(list.size());
        } else {
            try {
                subSet = (Set) set.getClass().newInstance();
            } catch (IllegalAccessException e) {
                subSet = new HashSet<>();
            } catch (InstantiationException e2) {
                subSet = new HashSet<>();
            }
        }
        subSet.addAll(list);
        return subSet;
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/list/SetUniqueList$SetListIterator.class */
    static class SetListIterator<E> extends AbstractIteratorDecorator<E> {
        private final Set<E> set;
        private E last;

        protected SetListIterator(Iterator<E> it, Set<E> set) {
            super(it);
            this.last = null;
            this.set = set;
        }

        @Override // org.apache.commons.collections4.iterators.AbstractIteratorDecorator, java.util.Iterator
        public E next() {
            this.last = (E) super.next();
            return this.last;
        }

        @Override // org.apache.commons.collections4.iterators.AbstractUntypedIteratorDecorator, java.util.Iterator
        public void remove() {
            super.remove();
            this.set.remove(this.last);
            this.last = null;
        }
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/list/SetUniqueList$SetListListIterator.class */
    static class SetListListIterator<E> extends AbstractListIteratorDecorator<E> {
        private final Set<E> set;
        private E last;

        protected SetListListIterator(ListIterator<E> it, Set<E> set) {
            super(it);
            this.last = null;
            this.set = set;
        }

        @Override // org.apache.commons.collections4.iterators.AbstractListIteratorDecorator, java.util.ListIterator, java.util.Iterator
        public E next() {
            this.last = (E) super.next();
            return this.last;
        }

        @Override // org.apache.commons.collections4.iterators.AbstractListIteratorDecorator, java.util.ListIterator
        public E previous() {
            this.last = (E) super.previous();
            return this.last;
        }

        @Override // org.apache.commons.collections4.iterators.AbstractListIteratorDecorator, java.util.ListIterator, java.util.Iterator
        public void remove() {
            super.remove();
            this.set.remove(this.last);
            this.last = null;
        }

        @Override // org.apache.commons.collections4.iterators.AbstractListIteratorDecorator, java.util.ListIterator
        public void add(E object) {
            if (!this.set.contains(object)) {
                super.add(object);
                this.set.add(object);
            }
        }

        @Override // org.apache.commons.collections4.iterators.AbstractListIteratorDecorator, java.util.ListIterator
        public void set(E object) {
            throw new UnsupportedOperationException("ListIterator does not support set");
        }
    }
}
