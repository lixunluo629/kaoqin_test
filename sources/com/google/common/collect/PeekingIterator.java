package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Iterator;

@GwtCompatible
/* loaded from: guava-18.0.jar:com/google/common/collect/PeekingIterator.class */
public interface PeekingIterator<E> extends Iterator<E> {
    E peek();

    E next();

    @Override // java.util.Iterator
    void remove();
}
