package org.aspectj.runtime.internal.cflowstack;

import java.util.Stack;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/runtime/internal/cflowstack/ThreadStackFactoryImpl.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/runtime/internal/cflowstack/ThreadStackFactoryImpl.class */
public class ThreadStackFactoryImpl implements ThreadStackFactory {

    /* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/runtime/internal/cflowstack/ThreadStackFactoryImpl$1.class
 */
    /* renamed from: org.aspectj.runtime.internal.cflowstack.ThreadStackFactoryImpl$1, reason: invalid class name */
    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/runtime/internal/cflowstack/ThreadStackFactoryImpl$1.class */
    static class AnonymousClass1 {
    }

    /* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/runtime/internal/cflowstack/ThreadStackFactoryImpl$ThreadStackImpl.class
 */
    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/runtime/internal/cflowstack/ThreadStackFactoryImpl$ThreadStackImpl.class */
    private static class ThreadStackImpl extends ThreadLocal implements ThreadStack {
        private ThreadStackImpl() {
        }

        ThreadStackImpl(AnonymousClass1 x0) {
            this();
        }

        @Override // java.lang.ThreadLocal
        public Object initialValue() {
            return new Stack();
        }

        @Override // org.aspectj.runtime.internal.cflowstack.ThreadStack
        public Stack getThreadStack() {
            return (Stack) get();
        }

        @Override // org.aspectj.runtime.internal.cflowstack.ThreadStack
        public void removeThreadStack() {
            remove();
        }
    }

    @Override // org.aspectj.runtime.internal.cflowstack.ThreadStackFactory
    public ThreadStack getNewThreadStack() {
        return new ThreadStackImpl(null);
    }

    /* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/runtime/internal/cflowstack/ThreadStackFactoryImpl$ThreadCounterImpl.class
 */
    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/runtime/internal/cflowstack/ThreadStackFactoryImpl$ThreadCounterImpl.class */
    private static class ThreadCounterImpl extends ThreadLocal implements ThreadCounter {
        private ThreadCounterImpl() {
        }

        ThreadCounterImpl(AnonymousClass1 x0) {
            this();
        }

        @Override // java.lang.ThreadLocal
        public Object initialValue() {
            return new Counter();
        }

        public Counter getThreadCounter() {
            return (Counter) get();
        }

        @Override // org.aspectj.runtime.internal.cflowstack.ThreadCounter
        public void removeThreadCounter() {
            remove();
        }

        @Override // org.aspectj.runtime.internal.cflowstack.ThreadCounter
        public void inc() {
            getThreadCounter().value++;
        }

        @Override // org.aspectj.runtime.internal.cflowstack.ThreadCounter
        public void dec() {
            getThreadCounter().value--;
        }

        @Override // org.aspectj.runtime.internal.cflowstack.ThreadCounter
        public boolean isNotZero() {
            return getThreadCounter().value != 0;
        }

        /* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/runtime/internal/cflowstack/ThreadStackFactoryImpl$ThreadCounterImpl$Counter.class
 */
        /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/runtime/internal/cflowstack/ThreadStackFactoryImpl$ThreadCounterImpl$Counter.class */
        static class Counter {
            protected int value = 0;

            Counter() {
            }
        }
    }

    @Override // org.aspectj.runtime.internal.cflowstack.ThreadStackFactory
    public ThreadCounter getNewThreadCounter() {
        return new ThreadCounterImpl(null);
    }
}
