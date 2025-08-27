package org.apache.commons.collections4.iterators;

import java.util.NoSuchElementException;
import org.apache.commons.collections4.ResettableIterator;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/iterators/SingletonIterator.class */
public class SingletonIterator<E> implements ResettableIterator<E> {
    private final boolean removeAllowed;
    private boolean beforeFirst;
    private boolean removed;
    private E object;

    public SingletonIterator(E object) {
        this(object, true);
    }

    public SingletonIterator(E object, boolean removeAllowed) {
        this.beforeFirst = true;
        this.removed = false;
        this.object = object;
        this.removeAllowed = removeAllowed;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.beforeFirst && !this.removed;
    }

    @Override // java.util.Iterator
    public E next() {
        if (!this.beforeFirst || this.removed) {
            throw new NoSuchElementException();
        }
        this.beforeFirst = false;
        return this.object;
    }

    @Override // java.util.Iterator
    public void remove() {
        if (this.removeAllowed) {
            if (this.removed || this.beforeFirst) {
                throw new IllegalStateException();
            }
            this.object = null;
            this.removed = true;
            return;
        }
        throw new UnsupportedOperationException();
    }

    @Override // org.apache.commons.collections4.ResettableIterator
    public void reset() {
        this.beforeFirst = true;
    }
}
