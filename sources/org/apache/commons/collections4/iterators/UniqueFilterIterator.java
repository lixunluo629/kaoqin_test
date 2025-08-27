package org.apache.commons.collections4.iterators;

import java.util.Iterator;
import org.apache.commons.collections4.functors.UniquePredicate;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/iterators/UniqueFilterIterator.class */
public class UniqueFilterIterator<E> extends FilterIterator<E> {
    public UniqueFilterIterator(Iterator<? extends E> iterator) {
        super(iterator, UniquePredicate.uniquePredicate());
    }
}
