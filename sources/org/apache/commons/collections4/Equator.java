package org.apache.commons.collections4;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/Equator.class */
public interface Equator<T> {
    boolean equate(T t, T t2);

    int hash(T t);
}
