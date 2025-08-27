package com.google.common.eventbus;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.SetMultimap;
import com.google.common.reflect.TypeToken;
import com.google.common.util.concurrent.UncheckedExecutionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

@Beta
/* loaded from: guava-18.0.jar:com/google/common/eventbus/EventBus.class */
public class EventBus {
    private static final LoadingCache<Class<?>, Set<Class<?>>> flattenHierarchyCache = CacheBuilder.newBuilder().weakKeys().build(new CacheLoader<Class<?>, Set<Class<?>>>() { // from class: com.google.common.eventbus.EventBus.1
        @Override // com.google.common.cache.CacheLoader
        public Set<Class<?>> load(Class<?> concreteClass) {
            return TypeToken.of((Class) concreteClass).getTypes().rawTypes();
        }
    });
    private final SetMultimap<Class<?>, EventSubscriber> subscribersByType;
    private final ReadWriteLock subscribersByTypeLock;
    private final SubscriberFindingStrategy finder;
    private final ThreadLocal<Queue<EventWithSubscriber>> eventsToDispatch;
    private final ThreadLocal<Boolean> isDispatching;
    private SubscriberExceptionHandler subscriberExceptionHandler;

    public EventBus() {
        this("default");
    }

    public EventBus(String identifier) {
        this(new LoggingSubscriberExceptionHandler(identifier));
    }

    public EventBus(SubscriberExceptionHandler subscriberExceptionHandler) {
        this.subscribersByType = HashMultimap.create();
        this.subscribersByTypeLock = new ReentrantReadWriteLock();
        this.finder = new AnnotatedSubscriberFinder();
        this.eventsToDispatch = new ThreadLocal<Queue<EventWithSubscriber>>() { // from class: com.google.common.eventbus.EventBus.2
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.lang.ThreadLocal
            public Queue<EventWithSubscriber> initialValue() {
                return new LinkedList();
            }
        };
        this.isDispatching = new ThreadLocal<Boolean>() { // from class: com.google.common.eventbus.EventBus.3
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.lang.ThreadLocal
            public Boolean initialValue() {
                return false;
            }
        };
        this.subscriberExceptionHandler = (SubscriberExceptionHandler) Preconditions.checkNotNull(subscriberExceptionHandler);
    }

    public void register(Object object) {
        Multimap<Class<?>, EventSubscriber> methodsInListener = this.finder.findAllSubscribers(object);
        this.subscribersByTypeLock.writeLock().lock();
        try {
            this.subscribersByType.putAll(methodsInListener);
            this.subscribersByTypeLock.writeLock().unlock();
        } catch (Throwable th) {
            this.subscribersByTypeLock.writeLock().unlock();
            throw th;
        }
    }

    public void unregister(Object object) {
        Multimap<Class<?>, EventSubscriber> methodsInListener = this.finder.findAllSubscribers(object);
        for (Map.Entry<Class<?>, Collection<EventSubscriber>> entry : methodsInListener.asMap().entrySet()) {
            Class<?> eventType = entry.getKey();
            Collection<EventSubscriber> eventMethodsInListener = entry.getValue();
            this.subscribersByTypeLock.writeLock().lock();
            try {
                Set<EventSubscriber> currentSubscribers = this.subscribersByType.get((SetMultimap<Class<?>, EventSubscriber>) eventType);
                if (!currentSubscribers.containsAll(eventMethodsInListener)) {
                    String strValueOf = String.valueOf(String.valueOf(object));
                    throw new IllegalArgumentException(new StringBuilder(65 + strValueOf.length()).append("missing event subscriber for an annotated method. Is ").append(strValueOf).append(" registered?").toString());
                }
                currentSubscribers.removeAll(eventMethodsInListener);
                this.subscribersByTypeLock.writeLock().unlock();
            } catch (Throwable th) {
                this.subscribersByTypeLock.writeLock().unlock();
                throw th;
            }
        }
    }

    public void post(Object event) {
        Set<Class<?>> dispatchTypes = flattenHierarchy(event.getClass());
        boolean dispatched = false;
        for (Class<?> eventType : dispatchTypes) {
            this.subscribersByTypeLock.readLock().lock();
            try {
                Set<EventSubscriber> wrappers = this.subscribersByType.get((SetMultimap<Class<?>, EventSubscriber>) eventType);
                if (!wrappers.isEmpty()) {
                    dispatched = true;
                    for (EventSubscriber wrapper : wrappers) {
                        enqueueEvent(event, wrapper);
                    }
                }
            } finally {
                this.subscribersByTypeLock.readLock().unlock();
            }
        }
        if (!dispatched && !(event instanceof DeadEvent)) {
            post(new DeadEvent(this, event));
        }
        dispatchQueuedEvents();
    }

    void enqueueEvent(Object event, EventSubscriber subscriber) {
        this.eventsToDispatch.get().offer(new EventWithSubscriber(event, subscriber));
    }

    void dispatchQueuedEvents() {
        if (this.isDispatching.get().booleanValue()) {
            return;
        }
        this.isDispatching.set(true);
        try {
            Queue<EventWithSubscriber> events = this.eventsToDispatch.get();
            while (true) {
                EventWithSubscriber eventWithSubscriber = events.poll();
                if (eventWithSubscriber != null) {
                    dispatch(eventWithSubscriber.event, eventWithSubscriber.subscriber);
                } else {
                    return;
                }
            }
        } finally {
            this.isDispatching.remove();
            this.eventsToDispatch.remove();
        }
    }

    void dispatch(Object event, EventSubscriber wrapper) throws IllegalAccessException, IllegalArgumentException {
        try {
            wrapper.handleEvent(event);
        } catch (InvocationTargetException e) {
            try {
                this.subscriberExceptionHandler.handleException(e.getCause(), new SubscriberExceptionContext(this, event, wrapper.getSubscriber(), wrapper.getMethod()));
            } catch (Throwable t) {
                Logger.getLogger(EventBus.class.getName()).log(Level.SEVERE, String.format("Exception %s thrown while handling exception: %s", t, e.getCause()), t);
            }
        }
    }

    @VisibleForTesting
    Set<Class<?>> flattenHierarchy(Class<?> concreteClass) {
        try {
            return flattenHierarchyCache.getUnchecked(concreteClass);
        } catch (UncheckedExecutionException e) {
            throw Throwables.propagate(e.getCause());
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/eventbus/EventBus$LoggingSubscriberExceptionHandler.class */
    private static final class LoggingSubscriberExceptionHandler implements SubscriberExceptionHandler {
        private final Logger logger;

        public LoggingSubscriberExceptionHandler(String identifier) {
            String strValueOf = String.valueOf(String.valueOf(EventBus.class.getName()));
            String strValueOf2 = String.valueOf(String.valueOf((String) Preconditions.checkNotNull(identifier)));
            this.logger = Logger.getLogger(new StringBuilder(1 + strValueOf.length() + strValueOf2.length()).append(strValueOf).append(".").append(strValueOf2).toString());
        }

        @Override // com.google.common.eventbus.SubscriberExceptionHandler
        public void handleException(Throwable exception, SubscriberExceptionContext context) {
            Logger logger = this.logger;
            Level level = Level.SEVERE;
            String strValueOf = String.valueOf(String.valueOf(context.getSubscriber()));
            String strValueOf2 = String.valueOf(String.valueOf(context.getSubscriberMethod()));
            logger.log(level, new StringBuilder(30 + strValueOf.length() + strValueOf2.length()).append("Could not dispatch event: ").append(strValueOf).append(" to ").append(strValueOf2).toString(), exception.getCause());
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/eventbus/EventBus$EventWithSubscriber.class */
    static class EventWithSubscriber {
        final Object event;
        final EventSubscriber subscriber;

        public EventWithSubscriber(Object event, EventSubscriber subscriber) {
            this.event = Preconditions.checkNotNull(event);
            this.subscriber = (EventSubscriber) Preconditions.checkNotNull(subscriber);
        }
    }
}
