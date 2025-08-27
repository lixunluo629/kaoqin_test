package org.ehcache.impl.internal.events;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.ehcache.core.spi.store.events.StoreEvent;
import org.ehcache.core.spi.store.events.StoreEventListener;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/events/FireableStoreEventHolder.class */
class FireableStoreEventHolder<K, V> {
    private final StoreEvent<K, V> event;
    private final Lock lock = new ReentrantLock();
    private final AtomicReference<Status> status = new AtomicReference<>(Status.CREATED);
    private volatile boolean failed = false;
    private final Condition condition = this.lock.newCondition();

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/events/FireableStoreEventHolder$Status.class */
    enum Status {
        CREATED,
        FIREABLE,
        FIRED
    }

    FireableStoreEventHolder(StoreEvent<K, V> event) {
        this.event = event;
    }

    void markFireable() {
        this.status.compareAndSet(Status.CREATED, Status.FIREABLE);
    }

    boolean isFireable() {
        return this.status.get().equals(Status.FIREABLE);
    }

    void waitTillFired() {
        while (!isFired()) {
            this.lock.lock();
            try {
                if (!isFired()) {
                    this.condition.awaitUninterruptibly();
                }
            } finally {
                this.lock.unlock();
            }
        }
    }

    private boolean isFired() {
        return this.status.get() == Status.FIRED;
    }

    boolean markFired() {
        boolean didIt = this.status.compareAndSet(Status.FIREABLE, Status.FIRED);
        if (didIt) {
            this.lock.lock();
            try {
                this.condition.signal();
                this.lock.unlock();
            } catch (Throwable th) {
                this.lock.unlock();
                throw th;
            }
        }
        return didIt;
    }

    void markFailed() {
        this.failed = true;
    }

    void fireOn(StoreEventListener<K, V> listener) {
        if (!this.failed) {
            listener.onEvent(this.event);
        }
    }

    int eventKeyHash() {
        return this.event.getKey().hashCode();
    }

    StoreEvent<K, V> getEvent() {
        return this.event;
    }

    public String toString() {
        return "FireableStoreEventHolder in state " + this.status.get() + " of " + this.event + (this.failed ? " (failed)" : " (not failed)");
    }
}
