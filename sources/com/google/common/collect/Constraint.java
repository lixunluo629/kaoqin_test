package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
/* loaded from: guava-18.0.jar:com/google/common/collect/Constraint.class */
interface Constraint<E> {
    E checkElement(E e);

    String toString();
}
