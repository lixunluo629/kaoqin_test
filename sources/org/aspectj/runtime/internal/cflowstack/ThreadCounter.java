package org.aspectj.runtime.internal.cflowstack;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/runtime/internal/cflowstack/ThreadCounter.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/runtime/internal/cflowstack/ThreadCounter.class */
public interface ThreadCounter {
    void inc();

    void dec();

    boolean isNotZero();

    void removeThreadCounter();
}
