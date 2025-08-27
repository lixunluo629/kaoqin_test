package org.ehcache.impl.config.event;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import org.ehcache.core.events.CacheEventListenerConfiguration;
import org.ehcache.core.events.CacheEventListenerProvider;
import org.ehcache.event.CacheEventListener;
import org.ehcache.event.EventFiring;
import org.ehcache.event.EventOrdering;
import org.ehcache.event.EventType;
import org.ehcache.impl.internal.classes.ClassInstanceConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/config/event/DefaultCacheEventListenerConfiguration.class */
public class DefaultCacheEventListenerConfiguration extends ClassInstanceConfiguration<CacheEventListener<?, ?>> implements CacheEventListenerConfiguration {
    private final EnumSet<EventType> eventsToFireOn;
    private EventFiring eventFiringMode;
    private EventOrdering eventOrderingMode;

    public DefaultCacheEventListenerConfiguration(Set<EventType> fireOn, Class<? extends CacheEventListener<?, ?>> clazz, Object... arguments) {
        super(clazz, arguments);
        this.eventFiringMode = EventFiring.ASYNCHRONOUS;
        this.eventOrderingMode = EventOrdering.UNORDERED;
        if (fireOn.isEmpty()) {
            throw new IllegalArgumentException("Set of event types to fire on must not be empty");
        }
        this.eventsToFireOn = EnumSet.copyOf((Collection) fireOn);
    }

    public DefaultCacheEventListenerConfiguration(Set<EventType> fireOn, CacheEventListener<?, ?> listener) {
        super(listener);
        this.eventFiringMode = EventFiring.ASYNCHRONOUS;
        this.eventOrderingMode = EventOrdering.UNORDERED;
        if (fireOn.isEmpty()) {
            throw new IllegalArgumentException("Set of event types to fire on must not be empty");
        }
        this.eventsToFireOn = EnumSet.copyOf((Collection) fireOn);
    }

    @Override // org.ehcache.spi.service.ServiceConfiguration
    public Class<CacheEventListenerProvider> getServiceType() {
        return CacheEventListenerProvider.class;
    }

    public void setEventFiringMode(EventFiring firingMode) {
        this.eventFiringMode = firingMode;
    }

    public void setEventOrderingMode(EventOrdering orderingMode) {
        this.eventOrderingMode = orderingMode;
    }

    @Override // org.ehcache.core.events.CacheEventListenerConfiguration
    public EventFiring firingMode() {
        return this.eventFiringMode;
    }

    @Override // org.ehcache.core.events.CacheEventListenerConfiguration
    public EventOrdering orderingMode() {
        return this.eventOrderingMode;
    }

    @Override // org.ehcache.core.events.CacheEventListenerConfiguration
    public EnumSet<EventType> fireOn() {
        return this.eventsToFireOn;
    }
}
