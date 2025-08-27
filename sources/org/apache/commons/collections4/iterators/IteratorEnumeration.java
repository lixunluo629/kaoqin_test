package org.apache.commons.collections4.iterators;

import java.util.Enumeration;
import java.util.Iterator;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/iterators/IteratorEnumeration.class */
public class IteratorEnumeration<E> implements Enumeration<E> {
    private Iterator<? extends E> iterator;

    public IteratorEnumeration() {
    }

    public IteratorEnumeration(Iterator<? extends E> iterator) {
        this.iterator = iterator;
    }

    @Override // java.util.Enumeration
    public boolean hasMoreElements() {
        return this.iterator.hasNext();
    }

    @Override // java.util.Enumeration
    public E nextElement() {
        return this.iterator.next();
    }

    public Iterator<? extends E> getIterator() {
        return this.iterator;
    }

    public void setIterator(Iterator<? extends E> iterator) {
        this.iterator = iterator;
    }
}
