package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
/* loaded from: guava-18.0.jar:com/google/common/cache/LongAddable.class */
interface LongAddable {
    void increment();

    void add(long j);

    long sum();
}
