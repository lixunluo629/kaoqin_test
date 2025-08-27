package org.apache.commons.collections4;

import java.util.Comparator;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/SortedBag.class */
public interface SortedBag<E> extends Bag<E> {
    Comparator<? super E> comparator();

    E first();

    E last();
}
