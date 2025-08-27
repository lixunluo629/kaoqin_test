package com.zaxxer.hikari.util;

import com.zaxxer.hikari.util.ConcurrentBag.IConcurrentBagEntry;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/util/ConcurrentBag.class */
public class ConcurrentBag<T extends IConcurrentBagEntry> implements AutoCloseable {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) ConcurrentBag.class);
    private final ThreadLocal<List<Object>> threadList;
    private final IBagStateListener listener;
    private volatile boolean closed;
    private final boolean weakThreadLocals = useWeakThreadLocals();
    private final SynchronousQueue<T> handoffQueue = new SynchronousQueue<>(true);
    private final AtomicInteger waiters = new AtomicInteger();
    private final CopyOnWriteArrayList<T> sharedList = new CopyOnWriteArrayList<>();

    /* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/util/ConcurrentBag$IBagStateListener.class */
    public interface IBagStateListener {
        void addBagItem(int i);
    }

    /* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/util/ConcurrentBag$IConcurrentBagEntry.class */
    public interface IConcurrentBagEntry {
        public static final int STATE_NOT_IN_USE = 0;
        public static final int STATE_IN_USE = 1;
        public static final int STATE_REMOVED = -1;
        public static final int STATE_RESERVED = -2;

        boolean compareAndSet(int i, int i2);

        void setState(int i);

        int getState();
    }

    public ConcurrentBag(IBagStateListener listener) {
        this.listener = listener;
        if (this.weakThreadLocals) {
            this.threadList = ThreadLocal.withInitial(() -> {
                return new ArrayList(16);
            });
        } else {
            this.threadList = ThreadLocal.withInitial(() -> {
                return new FastList(IConcurrentBagEntry.class, 16);
            });
        }
    }

    public T borrow(long j, TimeUnit timeUnit) throws InterruptedException {
        List<Object> list = this.threadList.get();
        for (int size = list.size() - 1; size >= 0; size--) {
            Object objRemove = list.remove(size);
            T t = (T) (this.weakThreadLocals ? (IConcurrentBagEntry) ((WeakReference) objRemove).get() : (IConcurrentBagEntry) objRemove);
            if (t != null && t.compareAndSet(0, 1)) {
                return t;
            }
        }
        int iIncrementAndGet = this.waiters.incrementAndGet();
        try {
            Iterator<T> it = this.sharedList.iterator();
            while (it.hasNext()) {
                T next = it.next();
                if (next.compareAndSet(0, 1)) {
                    if (iIncrementAndGet > 1) {
                        this.listener.addBagItem(iIncrementAndGet - 1);
                    }
                    return next;
                }
            }
            this.listener.addBagItem(iIncrementAndGet);
            long nanos = timeUnit.toNanos(j);
            do {
                long jCurrentTime = ClockSource.currentTime();
                T tPoll = this.handoffQueue.poll(nanos, TimeUnit.NANOSECONDS);
                if (tPoll == null || tPoll.compareAndSet(0, 1)) {
                    this.waiters.decrementAndGet();
                    return tPoll;
                }
                nanos -= ClockSource.elapsedNanos(jCurrentTime);
            } while (nanos > 10000);
            this.waiters.decrementAndGet();
            return null;
        } finally {
            this.waiters.decrementAndGet();
        }
    }

    public void requite(T bagEntry) {
        bagEntry.setState(0);
        int i = 0;
        while (this.waiters.get() > 0) {
            if (bagEntry.getState() != 0 || this.handoffQueue.offer(bagEntry)) {
                return;
            }
            if ((i & 255) == 255) {
                LockSupport.parkNanos(TimeUnit.MICROSECONDS.toNanos(10L));
            } else {
                Thread.yield();
            }
            i++;
        }
        List<Object> threadLocalList = this.threadList.get();
        if (threadLocalList.size() < 50) {
            threadLocalList.add(this.weakThreadLocals ? new WeakReference(bagEntry) : bagEntry);
        }
    }

    public void add(T bagEntry) {
        if (this.closed) {
            LOGGER.info("ConcurrentBag has been closed, ignoring add()");
            throw new IllegalStateException("ConcurrentBag has been closed, ignoring add()");
        }
        this.sharedList.add(bagEntry);
        while (this.waiters.get() > 0 && bagEntry.getState() == 0 && !this.handoffQueue.offer(bagEntry)) {
            Thread.yield();
        }
    }

    public boolean remove(T bagEntry) {
        if (!bagEntry.compareAndSet(1, -1) && !bagEntry.compareAndSet(-2, -1) && !this.closed) {
            LOGGER.warn("Attempt to remove an object from the bag that was not borrowed or reserved: {}", bagEntry);
            return false;
        }
        boolean removed = this.sharedList.remove(bagEntry);
        if (!removed && !this.closed) {
            LOGGER.warn("Attempt to remove an object from the bag that does not exist: {}", bagEntry);
        }
        this.threadList.get().remove(bagEntry);
        return removed;
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        this.closed = true;
    }

    public List<T> values(int state) {
        List<T> list = (List) this.sharedList.stream().filter(e -> {
            return e.getState() == state;
        }).collect(Collectors.toList());
        Collections.reverse(list);
        return list;
    }

    public List<T> values() {
        return (List) this.sharedList.clone();
    }

    public boolean reserve(T bagEntry) {
        return bagEntry.compareAndSet(0, -2);
    }

    public void unreserve(T bagEntry) {
        if (bagEntry.compareAndSet(-2, 0)) {
            while (this.waiters.get() > 0 && !this.handoffQueue.offer(bagEntry)) {
                Thread.yield();
            }
            return;
        }
        LOGGER.warn("Attempt to relinquish an object to the bag that was not reserved: {}", bagEntry);
    }

    public int getWaitingThreadCount() {
        return this.waiters.get();
    }

    public int getCount(int state) {
        int count = 0;
        Iterator<T> it = this.sharedList.iterator();
        while (it.hasNext()) {
            IConcurrentBagEntry e = it.next();
            if (e.getState() == state) {
                count++;
            }
        }
        return count;
    }

    public int[] getStateCounts() {
        int[] states = new int[6];
        Iterator<T> it = this.sharedList.iterator();
        while (it.hasNext()) {
            IConcurrentBagEntry e = it.next();
            int state = e.getState();
            states[state] = states[state] + 1;
        }
        states[4] = this.sharedList.size();
        states[5] = this.waiters.get();
        return states;
    }

    public int size() {
        return this.sharedList.size();
    }

    public void dumpState() {
        this.sharedList.forEach(entry -> {
            LOGGER.info(entry.toString());
        });
    }

    private boolean useWeakThreadLocals() {
        try {
            if (System.getProperty("com.zaxxer.hikari.useWeakReferences") != null) {
                return Boolean.getBoolean("com.zaxxer.hikari.useWeakReferences");
            }
            return getClass().getClassLoader() != ClassLoader.getSystemClassLoader();
        } catch (SecurityException e) {
            return true;
        }
    }
}
