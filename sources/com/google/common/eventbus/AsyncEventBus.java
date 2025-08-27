package com.google.common.eventbus;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;

@Beta
/* loaded from: guava-18.0.jar:com/google/common/eventbus/AsyncEventBus.class */
public class AsyncEventBus extends EventBus {
    private final Executor executor;
    private final ConcurrentLinkedQueue<EventBus.EventWithSubscriber> eventsToDispatch;

    public AsyncEventBus(String identifier, Executor executor) {
        super(identifier);
        this.eventsToDispatch = new ConcurrentLinkedQueue<>();
        this.executor = (Executor) Preconditions.checkNotNull(executor);
    }

    public AsyncEventBus(Executor executor, SubscriberExceptionHandler subscriberExceptionHandler) {
        super(subscriberExceptionHandler);
        this.eventsToDispatch = new ConcurrentLinkedQueue<>();
        this.executor = (Executor) Preconditions.checkNotNull(executor);
    }

    public AsyncEventBus(Executor executor) {
        super("default");
        this.eventsToDispatch = new ConcurrentLinkedQueue<>();
        this.executor = (Executor) Preconditions.checkNotNull(executor);
    }

    @Override // com.google.common.eventbus.EventBus
    void enqueueEvent(Object event, EventSubscriber subscriber) {
        this.eventsToDispatch.offer(new EventBus.EventWithSubscriber(event, subscriber));
    }

    @Override // com.google.common.eventbus.EventBus
    protected void dispatchQueuedEvents() {
        while (true) {
            EventBus.EventWithSubscriber eventWithSubscriber = this.eventsToDispatch.poll();
            if (eventWithSubscriber != null) {
                dispatch(eventWithSubscriber.event, eventWithSubscriber.subscriber);
            } else {
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.eventbus.EventBus
    public void dispatch(final Object event, final EventSubscriber subscriber) {
        Preconditions.checkNotNull(event);
        Preconditions.checkNotNull(subscriber);
        this.executor.execute(new Runnable() { // from class: com.google.common.eventbus.AsyncEventBus.1
            @Override // java.lang.Runnable
            public void run() throws IllegalAccessException, IllegalArgumentException {
                AsyncEventBus.super.dispatch(event, subscriber);
            }
        });
    }
}
