package org.apache.commons.collections4.iterators;

import java.util.Iterator;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/iterators/AbstractIteratorDecorator.class */
public abstract class AbstractIteratorDecorator<E> extends AbstractUntypedIteratorDecorator<E, E> {
    protected AbstractIteratorDecorator(Iterator<E> iterator) {
        super(iterator);
    }

    @Override // java.util.Iterator
    public E next() {
        return getIterator().next();
    }
}
