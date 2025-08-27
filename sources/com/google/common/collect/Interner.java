package com.google.common.collect;

import com.google.common.annotations.Beta;

@Beta
/* loaded from: guava-18.0.jar:com/google/common/collect/Interner.class */
public interface Interner<E> {
    E intern(E e);
}
