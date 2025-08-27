package org.ehcache.impl.internal.events;

import org.ehcache.core.events.StoreEventSink;
import org.ehcache.core.spi.store.events.StoreEventFilter;
import org.ehcache.core.spi.store.events.StoreEventListener;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/events/ScopedStoreEventDispatcher.class */
public class ScopedStoreEventDispatcher<K, V> extends AbstractStoreEventDispatcher<K, V> {
    @Override // org.ehcache.impl.internal.events.AbstractStoreEventDispatcher, org.ehcache.core.events.StoreEventDispatcher
    public /* bridge */ /* synthetic */ void reset(StoreEventSink x0) {
        super.reset(x0);
    }

    @Override // org.ehcache.impl.internal.events.AbstractStoreEventDispatcher, org.ehcache.core.events.StoreEventDispatcher
    public /* bridge */ /* synthetic */ void releaseEventSinkAfterFailure(StoreEventSink x0, Throwable x1) {
        super.releaseEventSinkAfterFailure(x0, x1);
    }

    @Override // org.ehcache.impl.internal.events.AbstractStoreEventDispatcher, org.ehcache.core.events.StoreEventDispatcher
    public /* bridge */ /* synthetic */ void releaseEventSink(StoreEventSink x0) {
        super.releaseEventSink(x0);
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

    public ScopedStoreEventDispatcher(int dispatcherConcurrency) {
        super(dispatcherConcurrency);
    }

    @Override // org.ehcache.core.events.StoreEventDispatcher
    public StoreEventSink<K, V> eventSink() {
        if (getListeners().isEmpty()) {
            return (StoreEventSink<K, V>) NO_OP_EVENT_SINK;
        }
        return new InvocationScopedEventSink(getFilters(), isEventOrdering(), getOrderedQueues(), getListeners());
    }
}
