package org.apache.commons.collections4.iterators;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/iterators/PushbackIterator.class */
public class PushbackIterator<E> implements Iterator<E> {
    private final Iterator<? extends E> iterator;
    private Deque<E> items = new ArrayDeque();

    public static <E> PushbackIterator<E> pushbackIterator(Iterator<? extends E> iterator) {
        if (iterator == null) {
            throw new NullPointerException("Iterator must not be null");
        }
        if (iterator instanceof PushbackIterator) {
            PushbackIterator<E> it = (PushbackIterator) iterator;
            return it;
        }
        return new PushbackIterator<>(iterator);
    }

    public PushbackIterator(Iterator<? extends E> iterator) {
        this.iterator = iterator;
    }

    public void pushback(E item) {
        this.items.push(item);
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        if (this.items.isEmpty()) {
            return this.iterator.hasNext();
        }
        return true;
    }

    @Override // java.util.Iterator
    public E next() {
        return !this.items.isEmpty() ? this.items.pop() : this.iterator.next();
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
