package org.apache.commons.compress.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/utils/Iterators.class */
public class Iterators {
    public static <T> boolean addAll(Collection<T> collection, Iterator<? extends T> iterator) {
        Objects.requireNonNull(collection);
        Objects.requireNonNull(iterator);
        boolean zAdd = false;
        while (true) {
            boolean wasModified = zAdd;
            if (iterator.hasNext()) {
                zAdd = wasModified | collection.add(iterator.next());
            } else {
                return wasModified;
            }
        }
    }

    private Iterators() {
    }
}
