package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible
/* loaded from: guava-18.0.jar:com/google/common/collect/ForwardingSortedSet.class */
public abstract class ForwardingSortedSet<E> extends ForwardingSet<E> implements SortedSet<E> {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    public abstract SortedSet<E> delegate();

    protected ForwardingSortedSet() {
    }

    @Override // java.util.SortedSet
    public Comparator<? super E> comparator() {
        return delegate().comparator();
    }

    @Override // java.util.SortedSet
    public E first() {
        return delegate().first();
    }

    public SortedSet<E> headSet(E toElement) {
        return delegate().headSet(toElement);
    }

    @Override // java.util.SortedSet
    public E last() {
        return delegate().last();
    }

    public SortedSet<E> subSet(E fromElement, E toElement) {
        return delegate().subSet(fromElement, toElement);
    }

    public SortedSet<E> tailSet(E fromElement) {
        return delegate().tailSet(fromElement);
    }

    private int unsafeCompare(Object o1, Object o2) {
        Comparator<? super E> comparator = comparator();
        return comparator == null ? ((Comparable) o1).compareTo(o2) : comparator.compare(o1, o2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.ForwardingCollection
    @Beta
    protected boolean standardContains(@Nullable Object obj) {
        try {
            Object ceiling = tailSet(obj).first();
            return unsafeCompare(ceiling, obj) == 0;
        } catch (ClassCastException e) {
            return false;
        } catch (NullPointerException e2) {
            return false;
        } catch (NoSuchElementException e3) {
            return false;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.ForwardingCollection
    @Beta
    protected boolean standardRemove(@Nullable Object obj) {
        try {
            Iterator<Object> iterator = tailSet(obj).iterator();
            if (iterator.hasNext()) {
                Object ceiling = iterator.next();
                if (unsafeCompare(ceiling, obj) == 0) {
                    iterator.remove();
                    return true;
                }
                return false;
            }
            return false;
        } catch (ClassCastException e) {
            return false;
        } catch (NullPointerException e2) {
            return false;
        }
    }

    @Beta
    protected SortedSet<E> standardSubSet(E fromElement, E toElement) {
        return tailSet(fromElement).headSet(toElement);
    }
}
