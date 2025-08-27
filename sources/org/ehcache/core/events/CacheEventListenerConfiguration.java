package org.ehcache.core.events;

import java.util.EnumSet;
import org.ehcache.event.EventFiring;
import org.ehcache.event.EventOrdering;
import org.ehcache.event.EventType;
import org.ehcache.spi.service.ServiceConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/events/CacheEventListenerConfiguration.class */
public interface CacheEventListenerConfiguration extends ServiceConfiguration<CacheEventListenerProvider> {
    EventFiring firingMode();

    EventOrdering orderingMode();

    EnumSet<EventType> fireOn();
}
