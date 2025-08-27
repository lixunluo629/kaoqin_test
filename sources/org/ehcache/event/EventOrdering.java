package org.ehcache.event;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/event/EventOrdering.class */
public enum EventOrdering {
    UNORDERED(false),
    ORDERED(true);

    private final boolean ordered;

    EventOrdering(boolean ordered) {
        this.ordered = ordered;
    }

    public boolean isOrdered() {
        return this.ordered;
    }
}
