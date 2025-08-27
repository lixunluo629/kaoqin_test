package org.aspectj.runtime.internal.cflowstack;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/runtime/internal/cflowstack/ThreadCounterImpl11.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/runtime/internal/cflowstack/ThreadCounterImpl11.class */
public class ThreadCounterImpl11 implements ThreadCounter {
    private Thread cached_thread;
    private Counter cached_counter;
    private static final int COLLECT_AT = 20000;
    private static final int MIN_COLLECT_AT = 100;
    private Hashtable counters = new Hashtable();
    private int change_count = 0;

    /* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/runtime/internal/cflowstack/ThreadCounterImpl11$Counter.class
 */
    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/runtime/internal/cflowstack/ThreadCounterImpl11$Counter.class */
    static class Counter {
        protected int value = 0;

        Counter() {
        }
    }

    private synchronized Counter getThreadCounter() {
        if (Thread.currentThread() != this.cached_thread) {
            this.cached_thread = Thread.currentThread();
            this.cached_counter = (Counter) this.counters.get(this.cached_thread);
            if (this.cached_counter == null) {
                this.cached_counter = new Counter();
                this.counters.put(this.cached_thread, this.cached_counter);
            }
            this.change_count++;
            int size = Math.max(1, this.counters.size());
            if (this.change_count > Math.max(100, 20000 / size)) {
                List dead_stacks = new ArrayList();
                Enumeration e = this.counters.keys();
                while (e.hasMoreElements()) {
                    Thread t = (Thread) e.nextElement();
                    if (!t.isAlive()) {
                        dead_stacks.add(t);
                    }
                }
                Iterator e2 = dead_stacks.iterator();
                while (e2.hasNext()) {
                    this.counters.remove((Thread) e2.next());
                }
                this.change_count = 0;
            }
        }
        return this.cached_counter;
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

    @Override // org.aspectj.runtime.internal.cflowstack.ThreadCounter
    public void removeThreadCounter() {
    }
}
