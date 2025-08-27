package org.apache.commons.collections4;

import java.util.Iterator;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/OrderedIterator.class */
public interface OrderedIterator<E> extends Iterator<E> {
    boolean hasPrevious();

    E previous();
}
