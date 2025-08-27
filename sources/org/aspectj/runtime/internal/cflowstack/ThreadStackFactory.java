package org.aspectj.runtime.internal.cflowstack;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/runtime/internal/cflowstack/ThreadStackFactory.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/runtime/internal/cflowstack/ThreadStackFactory.class */
public interface ThreadStackFactory {
    ThreadStack getNewThreadStack();

    ThreadCounter getNewThreadCounter();
}
