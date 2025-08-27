package org.apache.commons.collections4;

import java.util.Collection;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/BoundedCollection.class */
public interface BoundedCollection<E> extends Collection<E> {
    boolean isFull();

    int maxSize();
}
