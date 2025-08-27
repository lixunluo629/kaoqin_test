package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
/* loaded from: guava-18.0.jar:com/google/common/collect/Multiset.class */
public interface Multiset<E> extends Collection<E> {

    /* loaded from: guava-18.0.jar:com/google/common/collect/Multiset$Entry.class */
    public interface Entry<E> {
        E getElement();

        int getCount();

        boolean equals(Object obj);

        int hashCode();

        String toString();
    }

    int count(@Nullable Object obj);

    int add(@Nullable E e, int i);

    int remove(@Nullable Object obj, int i);

    int setCount(E e, int i);

    boolean setCount(E e, int i, int i2);

    Set<E> elementSet();

    Set<Entry<E>> entrySet();

    boolean equals(@Nullable Object obj);

    int hashCode();

    String toString();

    Iterator<E> iterator();

    boolean contains(@Nullable Object obj);

    boolean containsAll(Collection<?> collection);

    boolean add(E e);

    boolean remove(@Nullable Object obj);

    boolean removeAll(Collection<?> collection);

    boolean retainAll(Collection<?> collection);
}
