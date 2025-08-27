package org.apache.commons.collections4.iterators;

import java.util.Iterator;
import org.apache.commons.collections4.ResettableIterator;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/iterators/IteratorIterable.class */
public class IteratorIterable<E> implements Iterable<E> {
    private final Iterator<? extends E> iterator;
    private final Iterator<E> typeSafeIterator;

    private static <E> Iterator<E> createTypesafeIterator(final Iterator<? extends E> iterator) {
        return new Iterator<E>() { // from class: org.apache.commons.collections4.iterators.IteratorIterable.1
            @Override // java.util.Iterator
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override // java.util.Iterator
            public E next() {
                return (E) iterator.next();
            }

            @Override // java.util.Iterator
            public void remove() {
                iterator.remove();
            }
        };
    }

    public IteratorIterable(Iterator<? extends E> iterator) {
        this(iterator, false);
    }

    public IteratorIterable(Iterator<? extends E> iterator, boolean multipleUse) {
        if (multipleUse && !(iterator instanceof ResettableIterator)) {
            this.iterator = new ListIteratorWrapper(iterator);
        } else {
            this.iterator = iterator;
        }
        this.typeSafeIterator = createTypesafeIterator(this.iterator);
    }

    @Override // java.lang.Iterable
    public Iterator<E> iterator() {
        if (this.iterator instanceof ResettableIterator) {
            ((ResettableIterator) this.iterator).reset();
        }
        return this.typeSafeIterator;
    }
}
