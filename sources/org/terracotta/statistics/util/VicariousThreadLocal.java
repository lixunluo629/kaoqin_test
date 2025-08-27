package org.terracotta.statistics.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/util/VicariousThreadLocal.class */
public class VicariousThreadLocal<T> extends ThreadLocal<T> {
    private volatile Holder strongRefs;
    private static final ThreadLocal<WeakReference<Thread>> weakThread = new ThreadLocal<>();
    private static final Object UNINITIALISED = new Object();
    private static final AtomicReferenceFieldUpdater<VicariousThreadLocal, Holder> strongRefsUpdater = AtomicReferenceFieldUpdater.newUpdater(VicariousThreadLocal.class, Holder.class, "strongRefs");
    private final ThreadLocal<WeakReference<Holder>> local = new ThreadLocal<>();
    private final ReferenceQueue<Object> queue = new ReferenceQueue<>();

    static WeakReference<Thread> currentThreadRef() {
        WeakReference<Thread> ref = weakThread.get();
        if (ref == null) {
            ref = new WeakReference<>(Thread.currentThread());
            weakThread.set(ref);
        }
        return ref;
    }

    @Override // java.lang.ThreadLocal
    public T get() {
        Holder holderCreateHolder;
        WeakReference<Holder> weakReference = this.local.get();
        if (weakReference != null) {
            holderCreateHolder = weakReference.get();
            T t = (T) holderCreateHolder.value;
            if (t != UNINITIALISED) {
                return t;
            }
        } else {
            holderCreateHolder = createHolder();
        }
        T tInitialValue = initialValue();
        holderCreateHolder.value = tInitialValue;
        return tInitialValue;
    }

    @Override // java.lang.ThreadLocal
    public void set(T value) {
        WeakReference<Holder> ref = this.local.get();
        Holder holder = ref != null ? ref.get() : createHolder();
        holder.value = value;
    }

    private Holder createHolder() {
        Holder old;
        poll();
        Holder holder = new Holder(this.queue);
        WeakReference<Holder> ref = new WeakReference<>(holder);
        do {
            old = this.strongRefs;
            holder.next = old;
        } while (!strongRefsUpdater.compareAndSet(this, old, holder));
        this.local.set(ref);
        return holder;
    }

    @Override // java.lang.ThreadLocal
    public void remove() {
        WeakReference<Holder> ref = this.local.get();
        if (ref != null) {
            ref.get().value = UNINITIALISED;
        }
    }

    public void poll() {
        synchronized (this.queue) {
            if (this.queue.poll() == null) {
                return;
            }
            while (this.queue.poll() != null) {
            }
            Holder first = this.strongRefs;
            if (first == null) {
                return;
            }
            Holder link = first;
            Holder next = link.next;
            while (next != null) {
                if (next.get() == null) {
                    next = next.next;
                    link.next = next;
                } else {
                    link = next;
                    next = next.next;
                }
            }
            if (first.get() == null && !strongRefsUpdater.weakCompareAndSet(this, first, first.next)) {
                first.value = null;
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/util/VicariousThreadLocal$Holder.class */
    private static class Holder extends WeakReference<Object> {
        Holder next;
        Object value;

        Holder(ReferenceQueue<Object> queue) {
            super(VicariousThreadLocal.currentThreadRef(), queue);
            this.value = VicariousThreadLocal.UNINITIALISED;
        }
    }
}
