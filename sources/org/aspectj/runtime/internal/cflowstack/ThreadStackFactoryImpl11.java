package org.aspectj.runtime.internal.cflowstack;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/runtime/internal/cflowstack/ThreadStackFactoryImpl11.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/runtime/internal/cflowstack/ThreadStackFactoryImpl11.class */
public class ThreadStackFactoryImpl11 implements ThreadStackFactory {
    @Override // org.aspectj.runtime.internal.cflowstack.ThreadStackFactory
    public ThreadStack getNewThreadStack() {
        return new ThreadStackImpl11();
    }

    @Override // org.aspectj.runtime.internal.cflowstack.ThreadStackFactory
    public ThreadCounter getNewThreadCounter() {
        return new ThreadCounterImpl11();
    }
}
