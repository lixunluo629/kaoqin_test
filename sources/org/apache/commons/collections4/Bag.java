package org.apache.commons.collections4;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/Bag.class */
public interface Bag<E> extends Collection<E> {
    int getCount(Object obj);

    @Override // java.util.Collection
    boolean add(E e);

    boolean add(E e, int i);

    @Override // java.util.Collection
    boolean remove(Object obj);

    boolean remove(Object obj, int i);

    Set<E> uniqueSet();

    @Override // java.util.Collection
    int size();

    @Override // java.util.Collection
    boolean containsAll(Collection<?> collection);

    @Override // java.util.Collection
    boolean removeAll(Collection<?> collection);

    @Override // java.util.Collection
    boolean retainAll(Collection<?> collection);

    @Override // java.util.Collection, java.lang.Iterable
    Iterator<E> iterator();
}
