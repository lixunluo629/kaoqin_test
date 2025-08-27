package org.ehcache.impl.internal.events;

import org.ehcache.core.events.StoreEventSink;
import org.ehcache.core.spi.store.events.StoreEventFilter;
import org.ehcache.core.spi.store.events.StoreEventListener;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/events/ThreadLocalStoreEventDispatcher.class */
public class ThreadLocalStoreEventDispatcher<K, V> extends AbstractStoreEventDispatcher<K, V> {
    private final ThreadLocal<StoreEventSink<K, V>> tlEventSink;
    private final ThreadLocal<Integer> usageDepth;

    @Override // org.ehcache.impl.internal.events.AbstractStoreEventDispatcher, org.ehcache.core.events.StoreEventDispatcher
    public /* bridge */ /* synthetic */ void reset(StoreEventSink x0) {
        super.reset(x0);
    }

    @Override // org.ehcache.impl.internal.events.AbstractStoreEventDispatcher, org.ehcache.core.spi.store.events.StoreEventSource
    public /* bridge */ /* synthetic */ boolean isEventOrdering() {
        return super.isEventOrdering();
    }

    @Override // org.ehcache.impl.internal.events.AbstractStoreEventDispatcher, org.ehcache.core.spi.store.events.StoreEventSource
    public /* bridge */ /* synthetic */ void setEventOrdering(boolean x0) {
        super.setEventOrdering(x0);
    }

    @Override // org.ehcache.impl.internal.events.AbstractStoreEventDispatcher, org.ehcache.core.spi.store.events.StoreEventSource
    public /* bridge */ /* synthetic */ void addEventFilter(StoreEventFilter x0) {
        super.addEventFilter(x0);
    }

    @Override // org.ehcache.impl.internal.events.AbstractStoreEventDispatcher, org.ehcache.core.spi.store.events.StoreEventSource
    public /* bridge */ /* synthetic */ void removeEventListener(StoreEventListener x0) {
        super.removeEventListener(x0);
    }

    @Override // org.ehcache.impl.internal.events.AbstractStoreEventDispatcher, org.ehcache.core.spi.store.events.StoreEventSource
    public /* bridge */ /* synthetic */ void addEventListener(StoreEventListener x0) {
        super.addEventListener(x0);
    }

    public ThreadLocalStoreEventDispatcher(int dispatcherConcurrency) {
        super(dispatcherConcurrency);
        this.tlEventSink = new ThreadLocal<>();
        this.usageDepth = new ThreadLocal<>();
    }

    @Override // org.ehcache.core.events.StoreEventDispatcher
    public StoreEventSink<K, V> eventSink() {
        if (getListeners().isEmpty()) {
            return (StoreEventSink<K, V>) NO_OP_EVENT_SINK;
        }
        StoreEventSink<K, V> fudgingInvocationScopedEventSink = this.tlEventSink.get();
        if (fudgingInvocationScopedEventSink == null) {
            fudgingInvocationScopedEventSink = new FudgingInvocationScopedEventSink(getFilters(), isEventOrdering(), getOrderedQueues(), getListeners());
            this.tlEventSink.set(fudgingInvocationScopedEventSink);
            this.usageDepth.set(0);
        } else {
            this.usageDepth.set(Integer.valueOf(this.usageDepth.get().intValue() + 1));
        }
        return fudgingInvocationScopedEventSink;
    }

    @Override // org.ehcache.impl.internal.events.AbstractStoreEventDispatcher, org.ehcache.core.events.StoreEventDispatcher
    public void releaseEventSink(StoreEventSink<K, V> eventSink) {
        if (eventSink != NO_OP_EVENT_SINK) {
            int depthValue = this.usageDepth.get().intValue();
            if (depthValue == 0) {
                try {
                    super.releaseEventSink(eventSink);
                    this.tlEventSink.remove();
                    this.usageDepth.remove();
                    return;
                } catch (Throwable th) {
                    this.tlEventSink.remove();
                    this.usageDepth.remove();
                    throw th;
                }
            }
            this.usageDepth.set(Integer.valueOf(depthValue - 1));
        }
    }

    @Override // org.ehcache.impl.internal.events.AbstractStoreEventDispatcher, org.ehcache.core.events.StoreEventDispatcher
    public void releaseEventSinkAfterFailure(StoreEventSink<K, V> eventSink, Throwable throwable) {
        if (eventSink != NO_OP_EVENT_SINK) {
            int depthValue = this.usageDepth.get().intValue();
            if (depthValue == 0) {
                try {
                    super.releaseEventSinkAfterFailure(eventSink, throwable);
                    this.tlEventSink.remove();
                    this.usageDepth.remove();
                    return;
                } catch (Throwable th) {
                    this.tlEventSink.remove();
                    this.usageDepth.remove();
                    throw th;
                }
            }
            this.usageDepth.set(Integer.valueOf(depthValue - 1));
        }
    }
}
