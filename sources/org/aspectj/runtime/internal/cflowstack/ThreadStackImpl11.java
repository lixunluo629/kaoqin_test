package org.aspectj.runtime.internal.cflowstack;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Stack;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/runtime/internal/cflowstack/ThreadStackImpl11.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/runtime/internal/cflowstack/ThreadStackImpl11.class */
public class ThreadStackImpl11 implements ThreadStack {
    private Thread cached_thread;
    private Stack cached_stack;
    private static final int COLLECT_AT = 20000;
    private static final int MIN_COLLECT_AT = 100;
    private Hashtable stacks = new Hashtable();
    private int change_count = 0;

    @Override // org.aspectj.runtime.internal.cflowstack.ThreadStack
    public synchronized Stack getThreadStack() {
        if (Thread.currentThread() != this.cached_thread) {
            this.cached_thread = Thread.currentThread();
            this.cached_stack = (Stack) this.stacks.get(this.cached_thread);
            if (this.cached_stack == null) {
                this.cached_stack = new Stack();
                this.stacks.put(this.cached_thread, this.cached_stack);
            }
            this.change_count++;
            int size = Math.max(1, this.stacks.size());
            if (this.change_count > Math.max(100, 20000 / size)) {
                Stack dead_stacks = new Stack();
                Enumeration e = this.stacks.keys();
                while (e.hasMoreElements()) {
                    Thread t = (Thread) e.nextElement();
                    if (!t.isAlive()) {
                        dead_stacks.push(t);
                    }
                }
                Enumeration e2 = dead_stacks.elements();
                while (e2.hasMoreElements()) {
                    this.stacks.remove((Thread) e2.nextElement());
                }
                this.change_count = 0;
            }
        }
        return this.cached_stack;
    }

    @Override // org.aspectj.runtime.internal.cflowstack.ThreadStack
    public void removeThreadStack() {
    }
}
