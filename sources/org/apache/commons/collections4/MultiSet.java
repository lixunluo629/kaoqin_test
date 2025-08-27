package org.apache.commons.collections4;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/MultiSet.class */
public interface MultiSet<E> extends Collection<E> {

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/MultiSet$Entry.class */
    public interface Entry<E> {
        E getElement();

        int getCount();

        boolean equals(Object obj);

        int hashCode();
    }

    int getCount(Object obj);

    int setCount(E e, int i);

    @Override // java.util.Collection
    boolean add(E e);

    int add(E e, int i);

    @Override // java.util.Collection
    boolean remove(Object obj);

    int remove(Object obj, int i);

    Set<E> uniqueSet();

    Set<Entry<E>> entrySet();

    @Override // java.util.Collection, java.lang.Iterable
    Iterator<E> iterator();

    @Override // java.util.Collection
    int size();

    @Override // java.util.Collection
    boolean containsAll(Collection<?> collection);

    @Override // java.util.Collection
    boolean removeAll(Collection<?> collection);

    @Override // java.util.Collection
    boolean retainAll(Collection<?> collection);

    @Override // java.util.Collection
    boolean equals(Object obj);

    @Override // java.util.Collection
    int hashCode();
}
