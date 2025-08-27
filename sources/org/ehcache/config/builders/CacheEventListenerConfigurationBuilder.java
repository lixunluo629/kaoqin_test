package org.ehcache.config.builders;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import org.ehcache.config.Builder;
import org.ehcache.core.events.CacheEventListenerConfiguration;
import org.ehcache.event.CacheEventListener;
import org.ehcache.event.EventFiring;
import org.ehcache.event.EventOrdering;
import org.ehcache.event.EventType;
import org.ehcache.impl.config.event.DefaultCacheEventListenerConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/config/builders/CacheEventListenerConfigurationBuilder.class */
public class CacheEventListenerConfigurationBuilder implements Builder<CacheEventListenerConfiguration> {
    private EventOrdering eventOrdering;
    private EventFiring eventFiringMode;
    private Object[] listenerArguments;
    private final EnumSet<EventType> eventsToFireOn;
    private final Class<? extends CacheEventListener<?, ?>> listenerClass;
    private final CacheEventListener<?, ?> listenerInstance;

    private CacheEventListenerConfigurationBuilder(EnumSet<EventType> eventsToFireOn, Class<? extends CacheEventListener<?, ?>> listenerClass) {
        this.listenerArguments = new Object[0];
        this.eventsToFireOn = eventsToFireOn;
        this.listenerClass = listenerClass;
        this.listenerInstance = null;
    }

    private CacheEventListenerConfigurationBuilder(EnumSet<EventType> eventsToFireOn, CacheEventListener<?, ?> listenerInstance) {
        this.listenerArguments = new Object[0];
        this.eventsToFireOn = eventsToFireOn;
        this.listenerClass = null;
        this.listenerInstance = listenerInstance;
    }

    private CacheEventListenerConfigurationBuilder(CacheEventListenerConfigurationBuilder other) {
        this.listenerArguments = new Object[0];
        this.eventFiringMode = other.eventFiringMode;
        this.eventOrdering = other.eventOrdering;
        this.eventsToFireOn = EnumSet.copyOf((EnumSet) other.eventsToFireOn);
        this.listenerClass = other.listenerClass;
        this.listenerInstance = other.listenerInstance;
        this.listenerArguments = other.listenerArguments;
    }

    public static CacheEventListenerConfigurationBuilder newEventListenerConfiguration(Class<? extends CacheEventListener<?, ?>> listenerClass, EventType eventType, EventType... eventTypes) {
        return new CacheEventListenerConfigurationBuilder((EnumSet<EventType>) EnumSet.of(eventType, eventTypes), listenerClass);
    }

    public static CacheEventListenerConfigurationBuilder newEventListenerConfiguration(CacheEventListener<?, ?> listener, EventType eventType, EventType... eventTypes) {
        return new CacheEventListenerConfigurationBuilder((EnumSet<EventType>) EnumSet.of(eventType, eventTypes), listener);
    }

    public static CacheEventListenerConfigurationBuilder newEventListenerConfiguration(Class<? extends CacheEventListener<?, ?>> listenerClass, Set<EventType> eventSetToFireOn) throws IllegalArgumentException {
        if (eventSetToFireOn.isEmpty()) {
            throw new IllegalArgumentException("EventType Set cannot be empty");
        }
        return new CacheEventListenerConfigurationBuilder((EnumSet<EventType>) EnumSet.copyOf((Collection) eventSetToFireOn), listenerClass);
    }

    public static CacheEventListenerConfigurationBuilder newEventListenerConfiguration(CacheEventListener<?, ?> listener, Set<EventType> eventSetToFireOn) throws IllegalArgumentException {
        if (eventSetToFireOn.isEmpty()) {
            throw new IllegalArgumentException("EventType Set cannot be empty");
        }
        return new CacheEventListenerConfigurationBuilder((EnumSet<EventType>) EnumSet.copyOf((Collection) eventSetToFireOn), listener);
    }

    public CacheEventListenerConfigurationBuilder constructedWith(Object... arguments) {
        if (this.listenerClass == null) {
            throw new IllegalArgumentException("Arguments only are meaningful with class-based builder, this one seems to be an instance-based one");
        }
        CacheEventListenerConfigurationBuilder otherBuilder = new CacheEventListenerConfigurationBuilder(this);
        otherBuilder.listenerArguments = arguments;
        return otherBuilder;
    }

    public CacheEventListenerConfigurationBuilder eventOrdering(EventOrdering eventOrdering) {
        CacheEventListenerConfigurationBuilder otherBuilder = new CacheEventListenerConfigurationBuilder(this);
        otherBuilder.eventOrdering = eventOrdering;
        return otherBuilder;
    }

    public CacheEventListenerConfigurationBuilder ordered() {
        return eventOrdering(EventOrdering.ORDERED);
    }

    public CacheEventListenerConfigurationBuilder unordered() {
        return eventOrdering(EventOrdering.UNORDERED);
    }

    public CacheEventListenerConfigurationBuilder firingMode(EventFiring eventFiringMode) {
        CacheEventListenerConfigurationBuilder otherBuilder = new CacheEventListenerConfigurationBuilder(this);
        otherBuilder.eventFiringMode = eventFiringMode;
        return otherBuilder;
    }

    public CacheEventListenerConfigurationBuilder synchronous() {
        return firingMode(EventFiring.SYNCHRONOUS);
    }

    public CacheEventListenerConfigurationBuilder asynchronous() {
        return firingMode(EventFiring.ASYNCHRONOUS);
    }

    @Override // org.ehcache.config.Builder
    /* renamed from: build, reason: merged with bridge method [inline-methods] */
    public CacheEventListenerConfiguration build2() {
        DefaultCacheEventListenerConfiguration defaultCacheEventListenerConfiguration;
        if (this.listenerClass != null) {
            defaultCacheEventListenerConfiguration = new DefaultCacheEventListenerConfiguration(this.eventsToFireOn, this.listenerClass, this.listenerArguments);
        } else {
            defaultCacheEventListenerConfiguration = new DefaultCacheEventListenerConfiguration(this.eventsToFireOn, this.listenerInstance);
        }
        if (this.eventOrdering != null) {
            defaultCacheEventListenerConfiguration.setEventOrderingMode(this.eventOrdering);
        }
        if (this.eventFiringMode != null) {
            defaultCacheEventListenerConfiguration.setEventFiringMode(this.eventFiringMode);
        }
        return defaultCacheEventListenerConfiguration;
    }
}
