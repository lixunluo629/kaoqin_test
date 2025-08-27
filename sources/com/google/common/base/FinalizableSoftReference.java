package com.google.common.base;

import java.lang.ref.SoftReference;

/* loaded from: guava-18.0.jar:com/google/common/base/FinalizableSoftReference.class */
public abstract class FinalizableSoftReference<T> extends SoftReference<T> implements FinalizableReference {
    protected FinalizableSoftReference(T referent, FinalizableReferenceQueue queue) {
        super(referent, queue.queue);
        queue.cleanUp();
    }
}
