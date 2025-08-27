package com.itextpdf.kernel.counter.data;

import com.itextpdf.io.util.SystemUtil;
import com.itextpdf.kernel.counter.data.EventData;
import com.itextpdf.kernel.counter.event.IEvent;
import com.itextpdf.kernel.counter.event.IMetaInfo;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/counter/data/EventDataHandler.class */
public abstract class EventDataHandler<T, V extends EventData<T>> {
    private final IEventDataCache<T, V> cache;
    private final IEventDataFactory<T, V> factory;
    private volatile WaitTime waitTime;
    private final Object processLock = new Object();
    private final AtomicLong lastProcessedTime = new AtomicLong();

    protected abstract boolean process(V v);

    public EventDataHandler(IEventDataCache<T, V> cache, IEventDataFactory<T, V> factory, long initialWaitTimeMillis, long maxWaitTimeMillis) {
        this.cache = cache;
        this.factory = factory;
        this.waitTime = new WaitTime(initialWaitTimeMillis, maxWaitTimeMillis);
    }

    public List<V> clear() {
        List<V> all;
        synchronized (this.cache) {
            all = this.cache.clear();
        }
        this.lastProcessedTime.set(0L);
        resetWaitTime();
        return all != null ? all : Collections.emptyList();
    }

    public void register(IEvent event, IMetaInfo metaInfo) {
        EventData eventDataCreate;
        synchronized (this.factory) {
            eventDataCreate = this.factory.create(event, metaInfo);
        }
        if (eventDataCreate != null) {
            synchronized (this.cache) {
                this.cache.put(eventDataCreate);
            }
            tryProcessNextAsync();
        }
    }

    public void tryProcessNext() {
        EventData eventDataRetrieveNext;
        boolean successful;
        long currentTime = SystemUtil.getRelativeTimeMillis();
        if (currentTime - this.lastProcessedTime.get() > this.waitTime.getTime()) {
            this.lastProcessedTime.set(SystemUtil.getRelativeTimeMillis());
            synchronized (this.cache) {
                eventDataRetrieveNext = this.cache.retrieveNext();
            }
            if (eventDataRetrieveNext != null) {
                synchronized (this.processLock) {
                    successful = tryProcess(eventDataRetrieveNext);
                }
                if (successful) {
                    onSuccess(eventDataRetrieveNext);
                    return;
                }
                synchronized (this.cache) {
                    this.cache.put(eventDataRetrieveNext);
                }
                onFailure(eventDataRetrieveNext);
            }
        }
    }

    public void tryProcessNextAsync() {
        tryProcessNextAsync(null);
    }

    public void tryProcessNextAsync(Boolean daemon) {
        long currentTime = SystemUtil.getRelativeTimeMillis();
        if (currentTime - this.lastProcessedTime.get() > this.waitTime.getTime()) {
            Thread thread = new Thread() { // from class: com.itextpdf.kernel.counter.data.EventDataHandler.1
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    EventDataHandler.this.tryProcessNext();
                }
            };
            if (daemon != null) {
                thread.setDaemon(daemon.booleanValue());
            }
            thread.start();
        }
    }

    public void tryProcessRest() {
        List<V> unprocessedEvents = clear();
        if (!unprocessedEvents.isEmpty()) {
            try {
                synchronized (this.processLock) {
                    for (V data : unprocessedEvents) {
                        process(data);
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    public void resetWaitTime() {
        WaitTime local = this.waitTime;
        this.waitTime = new WaitTime(local.getInitial(), local.getMaximum());
    }

    public void increaseWaitTime() {
        WaitTime local = this.waitTime;
        this.waitTime = new WaitTime(local.getInitial(), local.getMaximum(), Math.min(local.getTime() * 2, local.getMaximum()));
    }

    public void setNoWaitTime() {
        WaitTime local = this.waitTime;
        this.waitTime = new WaitTime(local.getInitial(), local.getMaximum(), 0L);
    }

    public WaitTime getWaitTime() {
        return this.waitTime;
    }

    protected void onSuccess(V data) {
        resetWaitTime();
    }

    protected void onFailure(V data) {
        increaseWaitTime();
    }

    protected boolean onProcessException(Exception exception) {
        return false;
    }

    private boolean tryProcess(V data) {
        try {
            return process(data);
        } catch (Exception any) {
            return onProcessException(any);
        }
    }
}
