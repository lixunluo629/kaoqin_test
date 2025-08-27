package org.ehcache.impl.events;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import org.ehcache.Cache;
import org.ehcache.core.CacheConfigurationChangeEvent;
import org.ehcache.core.CacheConfigurationChangeListener;
import org.ehcache.core.CacheConfigurationProperty;
import org.ehcache.core.events.CacheEventDispatcher;
import org.ehcache.core.events.CacheEvents;
import org.ehcache.core.internal.events.EventListenerWrapper;
import org.ehcache.core.spi.store.events.StoreEvent;
import org.ehcache.core.spi.store.events.StoreEventListener;
import org.ehcache.core.spi.store.events.StoreEventSource;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.ehcache.event.EventFiring;
import org.ehcache.event.EventOrdering;
import org.ehcache.event.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/events/CacheEventDispatcherImpl.class */
public class CacheEventDispatcherImpl<K, V> implements CacheEventDispatcher<K, V> {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) CacheEventDispatcherImpl.class);
    private final ExecutorService unOrderedExectuor;
    private final ExecutorService orderedExecutor;
    private int listenersCount = 0;
    private int orderedListenerCount = 0;
    private final List<EventListenerWrapper<K, V>> syncListenersList = new CopyOnWriteArrayList();
    private final List<EventListenerWrapper<K, V>> aSyncListenersList = new CopyOnWriteArrayList();
    private final StoreEventListener<K, V> eventListener = new StoreListener();
    private volatile Cache<K, V> listenerSource;
    private volatile StoreEventSource<K, V> storeEventSource;

    public CacheEventDispatcherImpl(ExecutorService unOrderedExecutor, ExecutorService orderedExecutor) {
        this.unOrderedExectuor = unOrderedExecutor;
        this.orderedExecutor = orderedExecutor;
    }

    @Override // org.ehcache.core.events.CacheEventDispatcher
    public void registerCacheEventListener(CacheEventListener<? super K, ? super V> listener, EventOrdering ordering, EventFiring firing, EnumSet<EventType> forEventTypes) {
        EventListenerWrapper<K, V> wrapper = new EventListenerWrapper<>(listener, firing, ordering, forEventTypes);
        registerCacheEventListener(wrapper);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void registerCacheEventListener(EventListenerWrapper<K, V> wrapper) {
        if (this.aSyncListenersList.contains(wrapper) || this.syncListenersList.contains(wrapper)) {
            throw new IllegalStateException("Cache Event Listener already registered: " + wrapper.getListener());
        }
        if (wrapper.isOrdered()) {
            int i = this.orderedListenerCount;
            this.orderedListenerCount = i + 1;
            if (i == 0) {
                this.storeEventSource.setEventOrdering(true);
            }
        }
        switch (wrapper.getFiringMode()) {
            case ASYNCHRONOUS:
                this.aSyncListenersList.add(wrapper);
                break;
            case SYNCHRONOUS:
                this.syncListenersList.add(wrapper);
                break;
            default:
                throw new AssertionError("Unhandled EventFiring value: " + wrapper.getFiringMode());
        }
        int i2 = this.listenersCount;
        this.listenersCount = i2 + 1;
        if (i2 == 0) {
            this.storeEventSource.addEventListener(this.eventListener);
        }
    }

    @Override // org.ehcache.core.events.CacheEventDispatcher
    public void deregisterCacheEventListener(CacheEventListener<? super K, ? super V> listener) {
        EventListenerWrapper<K, V> wrapper = new EventListenerWrapper<>(listener);
        if (!removeWrapperFromList(wrapper, this.aSyncListenersList) && !removeWrapperFromList(wrapper, this.syncListenersList)) {
            throw new IllegalStateException("Unknown cache event listener: " + listener);
        }
    }

    private synchronized boolean removeWrapperFromList(EventListenerWrapper wrapper, List<EventListenerWrapper<K, V>> listenersList) {
        int index = listenersList.indexOf(wrapper);
        if (index != -1) {
            EventListenerWrapper containedWrapper = listenersList.remove(index);
            if (containedWrapper.isOrdered()) {
                int i = this.orderedListenerCount - 1;
                this.orderedListenerCount = i;
                if (i == 0) {
                    this.storeEventSource.setEventOrdering(false);
                }
            }
            int i2 = this.listenersCount - 1;
            this.listenersCount = i2;
            if (i2 == 0) {
                this.storeEventSource.removeEventListener(this.eventListener);
                return true;
            }
            return true;
        }
        return false;
    }

    @Override // org.ehcache.core.events.CacheEventDispatcher
    public synchronized void shutdown() {
        this.storeEventSource.removeEventListener(this.eventListener);
        this.storeEventSource.setEventOrdering(false);
        this.syncListenersList.clear();
        this.aSyncListenersList.clear();
        this.unOrderedExectuor.shutdown();
        this.orderedExecutor.shutdown();
    }

    @Override // org.ehcache.core.events.CacheEventDispatcher
    public synchronized void setListenerSource(Cache<K, V> source) {
        this.listenerSource = source;
    }

    void onEvent(CacheEvent<K, V> event) throws ExecutionException, InterruptedException {
        ExecutorService executor;
        if (this.storeEventSource.isEventOrdering()) {
            executor = this.orderedExecutor;
        } else {
            executor = this.unOrderedExectuor;
        }
        if (!this.aSyncListenersList.isEmpty()) {
            executor.submit(new EventDispatchTask(event, this.aSyncListenersList));
        }
        if (!this.syncListenersList.isEmpty()) {
            Future<?> future = executor.submit(new EventDispatchTask(event, this.syncListenersList));
            try {
                future.get();
            } catch (Exception e) {
                LOGGER.error("Exception received as result from synchronous listeners", (Throwable) e);
            }
        }
    }

    @Override // org.ehcache.core.spi.store.ConfigurationChangeSupport
    public List<CacheConfigurationChangeListener> getConfigurationChangeListeners() {
        List<CacheConfigurationChangeListener> configurationChangeListenerList = new ArrayList<>();
        configurationChangeListenerList.add(new CacheConfigurationChangeListener() { // from class: org.ehcache.impl.events.CacheEventDispatcherImpl.1
            @Override // org.ehcache.core.CacheConfigurationChangeListener
            public void cacheConfigurationChange(CacheConfigurationChangeEvent event) {
                if (event.getProperty().equals(CacheConfigurationProperty.ADD_LISTENER)) {
                    CacheEventDispatcherImpl.this.registerCacheEventListener((EventListenerWrapper) event.getNewValue());
                } else if (event.getProperty().equals(CacheConfigurationProperty.REMOVE_LISTENER)) {
                    CacheEventListener<? super K, ? super V> oldListener = (CacheEventListener) event.getOldValue();
                    CacheEventDispatcherImpl.this.deregisterCacheEventListener(oldListener);
                }
            }
        });
        return configurationChangeListenerList;
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/events/CacheEventDispatcherImpl$StoreListener.class */
    private final class StoreListener implements StoreEventListener<K, V> {
        private StoreListener() {
        }

        @Override // org.ehcache.core.spi.store.events.StoreEventListener
        public void onEvent(StoreEvent<K, V> event) throws ExecutionException, InterruptedException {
            switch (event.getType()) {
                case CREATED:
                    CacheEventDispatcherImpl.this.onEvent(CacheEvents.creation(event.getKey(), event.getNewValue(), CacheEventDispatcherImpl.this.listenerSource));
                    return;
                case UPDATED:
                    CacheEventDispatcherImpl.this.onEvent(CacheEvents.update(event.getKey(), event.getOldValue(), event.getNewValue(), CacheEventDispatcherImpl.this.listenerSource));
                    return;
                case REMOVED:
                    CacheEventDispatcherImpl.this.onEvent(CacheEvents.removal(event.getKey(), event.getOldValue(), CacheEventDispatcherImpl.this.listenerSource));
                    return;
                case EXPIRED:
                    CacheEventDispatcherImpl.this.onEvent(CacheEvents.expiry(event.getKey(), event.getOldValue(), CacheEventDispatcherImpl.this.listenerSource));
                    return;
                case EVICTED:
                    CacheEventDispatcherImpl.this.onEvent(CacheEvents.eviction(event.getKey(), event.getOldValue(), CacheEventDispatcherImpl.this.listenerSource));
                    return;
                default:
                    throw new AssertionError("Unexpected StoreEvent value: " + event.getType());
            }
        }
    }

    @Override // org.ehcache.core.events.CacheEventDispatcher
    public synchronized void setStoreEventSource(StoreEventSource<K, V> eventSource) {
        this.storeEventSource = eventSource;
    }
}
