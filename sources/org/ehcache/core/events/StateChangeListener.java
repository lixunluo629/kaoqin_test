package org.ehcache.core.events;

import org.ehcache.Status;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/events/StateChangeListener.class */
public interface StateChangeListener {
    void stateTransition(Status status, Status status2);
}
